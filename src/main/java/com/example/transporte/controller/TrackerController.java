package com.example.transporte.controller;

/**
 * @author Alexander Montesdeoca Garcia
 * @since 11-03-24
 * @version 1.0
 * Aplicacion que emula una empresa de logistica a la cual le llegan unos pedidos a entregar y con conductores lo hacen llegar a los clientes.
 */

import com.example.transporte.HelloApplication;
import com.example.transporte.model.Pedido;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TrackerController implements Initializable {
    @FXML
    public Label pedidoidLbl;
    @FXML
    public Label nombreLbl;
    @FXML
    public Label origenLbl;
    @FXML
    public Label destinoLbl;
    @FXML
    public Label fechaPedidoLbl;
    @FXML
    public Label estadoLbl;
    public Pedido pedido;
    @FXML
    public ImageView backBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pedido=new Pedido();

    }
    //Datos de los pedidos
    public void initData(Pedido pedido) {
        this.pedido = pedido;
        pedidoidLbl.setText(String.valueOf(pedido.getId()));
        nombreLbl.setText(String.valueOf(pedido.getNombre()));
        origenLbl.setText(pedido.getOrigen());
        destinoLbl.setText(pedido.getDestino());
        fechaPedidoLbl.setText(String.valueOf(pedido.getFechaPedido()));
        estadoLbl.setText(String.valueOf(pedido.getEstado()));
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("cliente.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chat!");
        stage.setScene(scene);
        stage.show();
        stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }
}
