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
        if (validarDatos()) {
            if (ActualizarDatosCliente()) {
                mostrarAlerta("Datos actualizados correctamente.");
                volverACliente();
            } else {
                mostrarAlerta("Error al actualizar los datos del usuario.");
            }
        }
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
        clienteStage.show();
        clienteStage = (Stage) AceptarBtn.getScene().getWindow();
        clienteStage.close();
    }

    // Validar los datos antes de actualizar
    private boolean validarDatos() {

        if (direccionTxt.getText().isEmpty() || TelefonoTxt.getText().isEmpty()) {
            mostrarAlerta("Los campos de dirección y teléfono no pueden estar vacíos.");
            return false;
        }

        // Validar el número de teléfono
        if (!esNumeroTelefonoValido(TelefonoTxt.getText())) {
            mostrarAlerta("Número de teléfono no válido. Por favor, ingrese un número de teléfono correcto.");
            return false;
        }

        return true;
    }

    // Actualiza los datos del cliente y retorna true si se actualizó correctamente
    public boolean ActualizarDatosCliente() {
        try {
            boolean contrasenaActualizada = true;
            boolean datosActualizados = true;

            // Actualizar la contraseña si ambos campos están llenos
            if (!contraseñaAnttxt.getText().isEmpty() && !contraseñaActTxt.getText().isEmpty()) {
                contrasenaActualizada = usuarioCon.actualizarContrasena(identifier, contraseñaAnttxt.getText(), contraseñaActTxt.getText());
                if (!contrasenaActualizada) {
                    return false;
                }
            }

            // Actualizar la dirección y el teléfono
            datosActualizados = usuarioCon.actualizarDireccionYTelefono(identifier, direccionTxt.getText(), TelefonoTxt.getText());
            if (!datosActualizados) {
                return false;
            }

            return contrasenaActualizada && datosActualizados;

        } catch (Exception e) {
            // Manejar otras excepciones
            mostrarAlerta("Error al actualizar la información del usuario: " + e.getMessage());
            return false;
        }
    }

    // Método para validar el número de teléfono
    private boolean esNumeroTelefonoValido(String telefono) {
        // Puedes ajustar la expresión regular según el formato esperado de los números de teléfono
        String regex = "^[0-9]{9}$"; // Ejemplo: números de teléfono de 10 dígitos
        return telefono.matches(regex);
    }


}
