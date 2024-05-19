package com.example.transporte.controller;

/**
 * @author Alexander Montesdeoca Garcia
 * @since 11-03-24
 * @version 1.0
 * Aplicacion que emula una empresa de logistica a la cual le llegan unos pedidos a entregar y con conductores lo hacen llegar a los clientes.
 */

import com.example.transporte.HelloApplication;
import com.example.transporte.conexion.UsuarioCon;
import com.example.transporte.model.Cliente;
import com.example.transporte.model.Pedido;
import com.example.transporte.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.transporte.controller.HelloController.identifier;

public class ClienteController implements Initializable {

    public Label holatt;
    @FXML
    public ListView listview;
    @FXML
    public Circle circle;
    @FXML
    public TextField buscarTxt;
    @FXML
    public ImageView EditBtn;
    private UsuarioCon usuarioCon;
    private Stage clienteStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        holatt.setText(identifier);
        clienteStage=new Stage();
        usuarioCon=new UsuarioCon();
        llenarListViewPedidos(listview);
        String imagen= "src/main/resources/img/3135768.png";
        Image image= new Image(new File(imagen).toURI().toString());
        circle.setFill(new ImagePattern(image));
        usuarioCon.comprobarDisponibilidadConductores();

    }
    //Llena la lista con los pedidos del cliente
    public void llenarListViewPedidos(ListView<Pedido> listViewClientes) {
        List<Pedido> pedidos = usuarioCon.obtenerPedidosConCliente(identifier);

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
                    setText(pedido.getNombre());
                }
            }
        });
    }
    //Llena la lista con los pedidos por el id del pedido
    public void llenarListViewPedidoId(ListView<Pedido> listViewClientes) {
        List<Pedido> nombrePedido = usuarioCon.obtenerPedidosPorId(Integer.parseInt(buscarTxt.getText()));

        ObservableList<Pedido> items = FXCollections.observableArrayList(nombrePedido);

        listViewClientes.setItems(items);

        // Configurar el CellFactory para mostrar solo el nombre del pedido
        listViewClientes.setCellFactory(param -> new ListCell<Pedido>() {
            @Override
            protected void updateItem(Pedido pedido, boolean empty) {
                super.updateItem(pedido, empty);
                if (empty || pedido == null || pedido.getNombre() == null) {
                    setText(null);
                } else {
                    setText(pedido.getNombre());
                }
            }
        });
    }

    public void seleccionarPedido(Pedido pedido) {
        listview.getSelectionModel().select(pedido);
    }
    public void Seleccionar(MouseEvent mouseEvent) {
        Pedido pedidoSeleccionado = (Pedido) listview.getSelectionModel().getSelectedItem();
        if (pedidoSeleccionado != null) {
            System.out.println("Seleccionado");
            seleccionarPedido(pedidoSeleccionado);
            mostrarVentanaDatosPedido(pedidoSeleccionado);
            Stage stage = (Stage) listview.getScene().getWindow();
            stage.close();
        }
    }

    //Muestra los datos del producto
    public void mostrarVentanaDatosPedido(Pedido pedido) {
        // Inicializar y mostrar la ventana de enviar mensaje
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tracker.fxml"));
            Parent root = fxmlLoader.load();
            TrackerController controller = fxmlLoader.getController();
            controller.initData(pedido); // Pasar el pedido seleccionado al controlador de la ventana
            Scene scene = new Scene(root);
            stage.setTitle("Producto de " + identifier);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Buscar(ActionEvent actionEvent) {
      llenarListViewPedidoId(listview);
    }

    //Abre el editar perfil del cliente
    public void EditarPerfil(MouseEvent mouseEvent) {
        clienteStage = (Stage) EditBtn.getScene().getWindow();
        clienteStage.close();
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource("editarCliente.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clienteStage.setResizable(false);
        clienteStage.setTitle("Logistic24");
        clienteStage.setScene(scene);
        clienteStage.show();
    }

    //Abre la ventana de empresa/particular para enviar pedidos
    public void Empresa(ActionEvent actionEvent) {
        clienteStage = (Stage) EditBtn.getScene().getWindow();
        clienteStage.close();
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource("MandarPedido.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clienteStage.setResizable(false);
        clienteStage.setTitle("Logistic24");
        clienteStage.setScene(scene);
        clienteStage.show();
    }
}
