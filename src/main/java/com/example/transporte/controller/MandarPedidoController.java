package com.example.transporte.controller;

import com.example.transporte.HelloApplication;
import com.example.transporte.conexion.UsuarioCon;
import com.example.transporte.model.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.transporte.controller.HelloController.idCliente;
import static com.example.transporte.controller.HelloController.identifier;
import static com.example.transporte.model.TipoUsuario.Cliente;

public class MandarPedidoController implements Initializable {
    @FXML
    public TextField idTxt;
    @FXML
    public TextField origenTxt;
    @FXML
    public TextField destinoTxt;
    @FXML
    public TextField nombreTxt;
    @FXML
    public Button registrarBtn;
    private UsuarioCon usuarioCon;
    private Stage clienteStage;
    private Cliente cliente;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarioCon=new UsuarioCon();
        clienteStage=new Stage();


    }
    //Registra el pedido
    public void Registrar(ActionEvent actionEvent) {

            if (usuarioCon.ingresarPedidoConRuta(Integer.parseInt(idTxt.getText()), idCliente, origenTxt.getText(), destinoTxt.getText(), nombreTxt.getText()
            )) {
                usuarioCon.asignarPedidoAConductorDisponible(Integer.parseInt(idTxt.getText()));
                volverACliente();
            }


    }

    //Método para volver a cliente
    public void volverACliente(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("cliente.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clienteStage.setTitle("Logistic24");
        clienteStage.setScene(scene);
        String imagen= "src/main/resources/img/logo.png";
        Image image= new Image(new File(imagen).toURI().toString());
        clienteStage.getIcons().add(image);
        clienteStage.setResizable(false);
        clienteStage.show();
        clienteStage = (Stage) registrarBtn.getScene().getWindow();
        clienteStage.close();
    }

    //Volver a la interfaz de cliente
    public void Volver(MouseEvent mouseEvent) {
        volverACliente();
    }
}
