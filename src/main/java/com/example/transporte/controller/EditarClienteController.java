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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.transporte.controller.HelloController.identifier;

public class EditarClienteController implements Initializable {
    @FXML
    public TextField direccionTxt;
    @FXML
    public TextField TelefonoTxt;
    @FXML
    public Button AceptarBtn;
    @FXML
    public PasswordField contraseñaAnttxt;
    @FXML
    public PasswordField contraseñaActTxt;
    private UsuarioCon usuarioCon;
    private Stage clienteStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarioCon=new UsuarioCon();
        clienteStage=new Stage();

    }

    public void Aceptar(ActionEvent actionEvent) {
        ActualizarDatosCliente();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Registro de Usuario");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void VolverBtn(MouseEvent mouseEvent) {
        volverACliente();
    }

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
        clienteStage = (Stage) AceptarBtn.getScene().getWindow();
        clienteStage.close();
    }

    //Actualiza los datos del cliente
    public void ActualizarDatosCliente(){
        try {
            // Verificar si los campos de contraseña no están vacíos
            if (!contraseñaAnttxt.getText().isEmpty() && !contraseñaActTxt.getText().isEmpty()) {
                // Actualizar la contraseña solo si los campos no están vacíos
                if (usuarioCon.actualizarContrasena(identifier, contraseñaAnttxt.getText(), contraseñaActTxt.getText())) {
                    mostrarAlerta("Contraseña actualizada correctamente.");
                    volverACliente();
                } else {
                    mostrarAlerta("Error al actualizar la contraseña.");
                }
            }

            // Verificar si los campos de dirección y teléfono no están vacíos
            if (!direccionTxt.getText().isEmpty() && !TelefonoTxt.getText().isEmpty()) {
                // Actualizar la dirección y el teléfono solo si los campos no están vacíos
                if (usuarioCon.actualizarDireccionYTelefono(identifier, direccionTxt.getText(), TelefonoTxt.getText())) {
                    mostrarAlerta("Dirección y teléfono actualizados correctamente.");
                    volverACliente();
                } else {
                    mostrarAlerta("Error al actualizar la dirección y el teléfono.");
                }
            }
        } catch (Exception e) {
            // Manejar otras excepciones
            mostrarAlerta("Error al actualizar la información del usuario: " + e.getMessage());
        }
    }
}
