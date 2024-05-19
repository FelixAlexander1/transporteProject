package com.example.transporte.controller;

/**
 * @author Alexander Montesdeoca Garcia
 * @since 11-03-24
 * @version 1.0
 * Aplicacion que emula una empresa de logistica a la cual le llegan unos pedidos a entregar y con conductores lo hacen llegar a los clientes.
 */

import com.example.transporte.HelloApplication;
import com.example.transporte.model.TipoUsuario;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Objects;
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
                root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("cliente.fxml")));
                primaryStage.setTitle("Logistic24");
                primaryStage.close();
                String imagen= "src/main/resources/img/logo.png";
                Image image= new Image(new File(imagen).toURI().toString());
                primaryStage.getIcons().add(image);
            } else if (tipoUsuario == TipoUsuario.Conductor) {
                root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("conductor.fxml")));
                primaryStage.setTitle("Logistic24");
                primaryStage.close();
                String imagen= "src/main/resources/img/logo.png";
                Image image= new Image(new File(imagen).toURI().toString());
                primaryStage.getIcons().add(image);
            } else if (tipoUsuario == TipoUsuario.Administrador) {
                root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("administrador.fxml")));
                primaryStage.setTitle("Logistic24");
                primaryStage.close();
                String imagen= "src/main/resources/img/logo.png";
                Image image= new Image(new File(imagen).toURI().toString());
                primaryStage.getIcons().add(image);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Correo o contraseña incorrectos.");
                alert.showAndWait();
                return;
            }

            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
