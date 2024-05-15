package com.example.transporte.controller;

import com.example.transporte.HelloApplication;
import com.example.transporte.conexion.Conexion;
import com.example.transporte.conexion.UsuarioCon;
import com.example.transporte.model.TipoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    public TextField emailTxt;
    @FXML
    public PasswordField passTxt;
    @FXML
    public Button IngrsarBtn;
    @FXML
    public Hyperlink registrarLink;

    private Conexion conexion;
    private UsuarioCon user;
    private Stage primaryStage;
    private SceneController sceneController;
    public static String identifier;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        primaryStage = new Stage();
        this.sceneController = new SceneController(primaryStage);
        conexion = new Conexion();
        user = new UsuarioCon();
    }

    public void Ingresar(ActionEvent actionEvent) {
        // Obtener el email y la contraseña ingresados por el usuario
        String email = emailTxt.getText();
        String contraseña = passTxt.getText();

        // Validar que los campos de email y contraseña no estén vacíos
        if (email.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Por favor, completa todos los campos");
        }

        // Realizar el inicio de sesión
        TipoUsuario tipoUsuario = user.obtenerTipoUsuario(email, contraseña);
        identifier= user.recibirNombre(email,contraseña);

        // Verificar el tipo de usuario obtenido
        if (tipoUsuario != null) {
            // El inicio de sesión es exitoso
            System.out.println("Inicio de sesión exitoso. Tipo de usuario: " + tipoUsuario + user.recibirNombre(email,contraseña));
            primaryStage = (Stage) IngrsarBtn.getScene().getWindow();
            primaryStage.close();
            sceneController.abrirEscena(tipoUsuario);
        } else {
            // El inicio de sesión falló
            System.out.println("Inicio de sesión fallido. Credenciales incorrectas.");
            // Mostrar un mensaje de error al usuario o tomar otras acciones según sea necesario
        }
    }

    public void Registrar(ActionEvent actionEvent) throws IOException {
        System.out.println("Ir a registrarse");
        primaryStage = (Stage) registrarLink.getScene().getWindow();
        primaryStage.close();
        Scene scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource("registro.fxml")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para mostrar una alerta con un mensaje
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Ingreso de Usuario");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}