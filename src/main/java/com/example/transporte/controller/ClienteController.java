package com.example.transporte.controller;

import com.example.transporte.HelloApplication;
import com.example.transporte.conexion.UsuarioCon;
import com.example.transporte.model.Cliente;
import com.example.transporte.model.Pedido;
import com.example.transporte.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    private UsuarioCon usuarioCon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        holatt.setText(identifier);
        usuarioCon=new UsuarioCon();
        llenarListViewClientes(listview);
        String imagen= "src/main/resources/img/3135768.png";
        Image image= new Image(new File(imagen).toURI().toString());
        circle.setFill(new ImagePattern(image));
    }

    public void llenarListViewClientes(ListView<Pedido> listViewClientes) {
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

    public void seleccionarPedido(Pedido pedido) {
        listview.getSelectionModel().select(pedido);
    }
    public void Seleccionar(MouseEvent mouseEvent) {
        Pedido pedidoSeleccionado = (Pedido) listview.getSelectionModel().getSelectedItem();
        if (pedidoSeleccionado != null) {
            System.out.println("Seleccionado");
            seleccionarPedido(pedidoSeleccionado);
            mostrarVentanaEnviarMensaje(pedidoSeleccionado);
            Stage stage = (Stage) listview.getScene().getWindow();
            stage.close();
        }
    }

    public void mostrarVentanaEnviarMensaje(Pedido pedido) {
        // Inicializar y mostrar la ventana de enviar mensaje
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tracker.fxml"));
            Parent root = fxmlLoader.load();
            TrackerController controller = fxmlLoader.getController();
            controller.initData(pedido); // Pasar el pedido seleccionado al controlador de la ventana
            Scene scene = new Scene(root);
            stage.setTitle("Producto de " + identifier);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
