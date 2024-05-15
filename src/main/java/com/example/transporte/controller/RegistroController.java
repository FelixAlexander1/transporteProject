package com.example.transporte.controller;

import com.example.transporte.HelloApplication;
import com.example.transporte.conexion.UsuarioCon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RegistroController implements Initializable {
    @FXML
    public TextField nombreTxt;
    @FXML
    public TextField contraseniaTxt;
    @FXML
    public TextField contraseniaTxt2;
    @FXML
    public Button registraseBtn;
    @FXML
    public Button cancelarBtn;
    @FXML
    public TextField emailTxt;
    @FXML
    public TextField telefonoTxt;
    @FXML
    public TextField direccionTxt;
    private UsuarioCon usuarioCon;
    private Stage primaryStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarioCon=new UsuarioCon();
        primaryStage=new Stage();
    }

    public void Registrar(ActionEvent actionEvent) throws IOException {
        String nombre = nombreTxt.getText();
        String contraseña = contraseniaTxt.getText();
        String contraseñaConfirmacion = contraseniaTxt2.getText();
        String email = emailTxt.getText();
        String telefono = telefonoTxt.getText();
        String direccion = direccionTxt.getText();

        if (!contraseña.equals(contraseñaConfirmacion)) {
            mostrarAlerta("Las contraseñas no coinciden");
        }
        // Validar que el nombre no esté vacío
        else if (nombre.isEmpty()) {
            mostrarAlerta("Por favor, ingresa un nombre");
        }

        // Validar que el email sea un formato válido
        else if (!isValidEmail(email)) {
            mostrarAlerta("Por favor, ingresa un email válido");
        }

        // Validar que el teléfono sea un formato válido
        else if (!isValidPhoneNumber(telefono)) {
            mostrarAlerta("Por favor, ingresa un número de teléfono válido");
        }
        else {usuarioCon.registrarUsuario(nombre,contraseña,email,direccion,telefono);

            primaryStage = (Stage) cancelarBtn.getScene().getWindow();
            primaryStage.close();
            Scene scene;
            scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource("hello-view.fxml")));
            primaryStage.setScene(scene);
            primaryStage.show();}



    }

    public void Cancelar(ActionEvent actionEvent) throws IOException {

        System.out.println("Ir a registrarse");
        primaryStage = (Stage) cancelarBtn.getScene().getWindow();
        primaryStage.close();
        Scene scene;
        scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource("hello-view.fxml")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Registro de Usuario");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Método para validar el formato de un email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // Método para validar el formato de un número de teléfono
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\d{10}$");
    }
}
