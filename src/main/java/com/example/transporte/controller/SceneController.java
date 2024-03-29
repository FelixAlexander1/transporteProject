package com.example.transporte.controller;

import com.example.transporte.HelloApplication;
import com.example.transporte.model.TipoUsuario;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class SceneController implements Initializable {
    private Stage primaryStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        primaryStage=new Stage();
    }

    // Constructor
    public SceneController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Método para abrir la escena correspondiente según el tipo de usuario
    public void abrirEscena(TipoUsuario tipoUsuario) {
        try {
            Parent root = null;
            Scene scene;


            if (tipoUsuario == TipoUsuario.Cliente) {
                root = FXMLLoader.load(HelloApplication.class.getResource("cliente.fxml"));
                primaryStage.setTitle("LogisticTR!");
                primaryStage.close();
            } else if (tipoUsuario == TipoUsuario.Conductor) {
                root = FXMLLoader.load(HelloApplication.class.getResource("conductor.fxml"));
                primaryStage.setTitle("LogisticTR!");
                primaryStage.close();
            } else if (tipoUsuario == TipoUsuario.Administrador) {
                root = FXMLLoader.load(HelloApplication.class.getResource("administrador.fxml"));
                primaryStage.setTitle("LogisticTR!");
                primaryStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Correo o contraseña incorrectos.");
                alert.showAndWait();
                return;
            }

            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de excepciones, por ejemplo, mostrar un mensaje de error
        }
    }


}
