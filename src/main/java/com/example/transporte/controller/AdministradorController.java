package com.example.transporte.controller;

import com.example.transporte.conexion.UsuarioCon;
import com.example.transporte.model.Cliente;
import com.example.transporte.model.Ruta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.transporte.controller.HelloController.identifier;

public class AdministradorController implements Initializable {
    @FXML
    public Circle circle;
    @FXML
    public Label labelNombre;
    @FXML
    public ListView listview1;
    @FXML
    public TextField textfield1;
    @FXML
    public ListView listview2;
    @FXML
    public TextField textfield11;
    private UsuarioCon usuarioCon;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarioCon=new UsuarioCon();
        labelNombre.setText(identifier);
        //llenarListViewPedidos(listview1);
        //llenarListViewRuta(listview2);
        llenarListViewClientes(listview2);
        String imagen= "src/main/resources/img/3135768.png";
        Image image= new Image(new File(imagen).toURI().toString());
        circle.setFill(new ImagePattern(image));
    }

    public void llenarListViewRuta(ListView<Ruta> listViewRuta) {
        List<Ruta> rutas = usuarioCon.obtenerRutasPorConductor(textfield1.getText());
        // Crear un ObservableList a partir de la lista de pedidos
        ObservableList<Ruta> items = FXCollections.observableArrayList(rutas);
        // Establecer los pedidos en el ListView
        listViewRuta.setItems(items);
        // Configurar el CellFactory para mostrar id, fecha inicio y fecha final
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

    public void llenarListViewClientes(ListView<Cliente> listViewRuta) {
        List<Cliente> clientes = usuarioCon.obtenerClientes();
        // Crear un ObservableList a partir de la lista de clientes
        ObservableList<Cliente> items = FXCollections.observableArrayList(clientes);
        // Establecer los pedidos en el ListView
        listViewRuta.setItems(items);
        // Configurar el CellFactory para mostrar solo el nombre del cliente
        listViewRuta.setCellFactory(param -> new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);
                if (empty || cliente == null || cliente.getId() == -1) {
                    setText(null);
                } else {
                    setText("Nombre: "+cliente.getNombre());
                }
            }
        });
    }

    public void Buscar(ActionEvent actionEvent) {
        llenarListViewRuta(listview1);
    }

    public void EliminarRuta(ActionEvent actionEvent) {
        usuarioCon.eliminarRuta(textfield1.getText());
        llenarListViewRuta(listview1);
    }

    public void seleccionarCliente(Cliente cliente) {
        listview2.getSelectionModel().select(cliente);
    }
    public void DarBaja(ActionEvent actionEvent) {
     /*   Cliente clienteSeleccionado = (Cliente) listview2.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            usuarioCon.eliminarUsuario(clienteSeleccionado.getNombre());
            // Volver a llenar el ListView después de eliminar el cliente
            llenarListViewClientes(listview2);
        }*/
    }

    public void BuscarClientes(ActionEvent actionEvent) {
    }

    public void Seleccionar(MouseEvent mouseEvent) {
        Cliente clienteSeleccionado = (Cliente) listview2.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            System.out.println("Cliente seleccionado");
            seleccionarCliente(clienteSeleccionado);
        }
    }
}
