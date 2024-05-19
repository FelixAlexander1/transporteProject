package com.example.transporte.controller;

/**
 * @author Alexander Montesdeoca Garcia
 * @since 11-03-24
 * @version 1.0
 * Aplicacion que emula una empresa de logistica a la cual le llegan unos pedidos a entregar y con conductores lo hacen llegar a los clientes.
 */

import com.example.transporte.conexion.UsuarioCon;
import com.example.transporte.model.EstadoPedido;
import com.example.transporte.model.Pedido;
import com.example.transporte.model.Ruta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.transporte.controller.HelloController.identifier;

public class ConductorController implements Initializable {
    @FXML
    public ListView listview1;
    @FXML
    public Label nombreLbl;
    @FXML
    public Circle circle;
    @FXML
    public ListView listview2;
    private UsuarioCon usuarioCon;
    private Pedido pedidoSeleccionado;
    private Ruta rutaSeleccionada;
    private EstadoPedido estado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarioCon=new UsuarioCon();
        nombreLbl.setText(identifier);
        llenarListViewPedidos(listview1);
        llenarListViewRuta(listview2);
        String imagen= "src/main/resources/img/3135768.png";
        Image image= new Image(new File(imagen).toURI().toString());
        circle.setFill(new ImagePattern(image));
    }

    //Llena la lista con las rutas
    public void llenarListViewRuta(ListView<Ruta> listViewRuta) {
        List<Ruta> rutas = usuarioCon.obtenerRutasPorConductor(identifier);
        // Crear un ObservableList a partir de la lista de pedidos
        ObservableList<Ruta> items = FXCollections.observableArrayList(rutas);
        // Establecer los pedidos en el ListView
        listViewRuta.setItems(items);
        // Configurar el CellFactory para mostrar solo el nombre del pedido
        listViewRuta.setCellFactory(param -> new ListCell<Ruta>() {
            @Override
            protected void updateItem(Ruta ruta, boolean empty) {
                super.updateItem(ruta, empty);
                if (empty || ruta == null || ruta.getId() == -1) {
                    setText(null);
                } else {
                    setText("Id: "+ruta.getId()+", Fecha Inicio: "+ruta.getFechaInicio()+", Fecha Fin: "+ruta.getFechaFin());
                }
            }
        });
    }

    //Llena la lista con los pedidos
    public void llenarListViewPedidos(ListView<Pedido> listViewClientes) {
        List<Pedido> pedidos = usuarioCon.obtenerNombresProductosConConductor(identifier);
        // Crear un ObservableList a partir de la lista de pedidos
        ObservableList<Pedido> items = FXCollections.observableArrayList(pedidos);
        // Establecer los pedidos en el ListView
        listViewClientes.setItems(items);
        // Configurar el CellFactory para mostrar solo el nombre del pedido
        listViewClientes.setCellFactory(param -> new ListCell<Pedido>() {
            @Override
            protected void updateItem(Pedido pedido, boolean empty) {
                super.updateItem(pedido, empty);
                if (empty || pedido == null || pedido.getNombre() == null) {
                    setText(null);
                } else {
                    setText("Id: "+pedido.getId()+", Nombre: "+pedido.getNombre()+", Estado: "+pedido.getEstado());
                }
            }
        });
    }

    //Selecciona el pedido
    public void seleccionarPedido(Pedido pedido) {
        listview1.getSelectionModel().select(pedido);
    }

    public void Seleccionar(MouseEvent mouseEvent) {
        pedidoSeleccionado = (Pedido) listview1.getSelectionModel().getSelectedItem();
        if (pedidoSeleccionado != null) {
            System.out.println("Seleccionado");
            seleccionarPedido(pedidoSeleccionado);

        }
    }

    //Pone el pedido en pendiente
    public void Pendiente(ActionEvent actionEvent) {
        usuarioCon.actualizarEstadoPedido(pedidoSeleccionado.getId(),EstadoPedido.Pendiente);
        listview1.getItems().clear();
        llenarListViewPedidos(listview1);

    }

    //Pone si el pedido esta en curso
    public void Cursar(ActionEvent actionEvent) {
        usuarioCon.actualizarEstadoPedido(pedidoSeleccionado.getId(),EstadoPedido.EnCurso);
        listview1.getItems().clear();
        llenarListViewPedidos(listview1);
        llenarListViewRuta(listview2);

    }

    //Se entrega el pedido
    public void Entregar(ActionEvent actionEvent) {
        usuarioCon.actualizarEstadoPedido(pedidoSeleccionado.getId(),EstadoPedido.Entregado);
        listview1.getItems().clear();
        llenarListViewPedidos(listview1);
    }

    //Se cancela el pedido
    public void Cancelar(ActionEvent actionEvent) {
        usuarioCon.actualizarEstadoPedido(pedidoSeleccionado.getId(),EstadoPedido.Cancelado);
        listview1.getItems().clear(); // Limpiar el ListView
        llenarListViewPedidos(listview1);
    }

    //Selecciona la ruta de la lista
    public void seleccionarRuta(Ruta ruta) {
        listview2.getSelectionModel().select(ruta);
    }

    public void Seleccionar2(MouseEvent mouseEvent) {
        rutaSeleccionada = (Ruta) listview2.getSelectionModel().getSelectedItem();
        if (rutaSeleccionada != null) {
            System.out.println("Seleccionado");
            seleccionarRuta(rutaSeleccionada);
        }
    }

    //Finaliza la ruta
    public void Finalizar(ActionEvent actionEvent) {
        usuarioCon.actualizarFechaFinRuta(rutaSeleccionada.getId(), LocalDateTime.now());
        listview2.getItems().clear();
        llenarListViewRuta(listview2);
    }
}
