package com.example.transporte.controller;

import com.example.transporte.conexion.Conexion;
import com.example.transporte.conexion.UsuarioCon;
import com.example.transporte.model.TipoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    public TextField emailTxt;
    @FXML
    public Button IngrsarBtn;
    @FXML
    public PasswordField passTxt;
    private Conexion conexion;
    private UsuarioCon user;
    private Stage primaryStage;
    private SceneController sceneController;
    public static String identifier ;

    public HelloController() {
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        primaryStage = new Stage();
        this.sceneController = new SceneController(primaryStage);
        conexion = new Conexion();
        user = new UsuarioCon();

    }



    public void Ingresar(ActionEvent actionEvent) {
        identifier=user.recibirNombre(emailTxt.getText(),passTxt.getText());
        // Obtener el email y la contraseña ingresados por el usuario
        String email = emailTxt.getText();
        String contraseña = passTxt.getText();

        // Validar el inicio de sesión
        TipoUsuario tipoUsuario = user.obtenerTipoUsuario(email, contraseña);

        sceneController.abrirEscena(tipoUsuario);





        // Verificar el tipo de usuario obtenido
        if (tipoUsuario != null) {
            // El inicio de sesión es exitoso
            System.out.println("Inicio de sesión exitoso. Tipo de usuario: " + tipoUsuario);
            primaryStage = (Stage) IngrsarBtn.getScene().getWindow();
            primaryStage.close();
        } else {
            // El inicio de sesión falló
            System.out.println("Inicio de sesión fallido. Credenciales incorrectas.");
            // Mostrar un mensaje de error al usuario o tomar otras acciones según sea necesario
        }
    }


}