package com.example.transporte.controller;

/**
 * @author Alexander Montesdeoca Garcia
 * @since 11-03-24
 * @version 1.0
 * Aplicacion que emula una empresa de logistica a la cual le llegan unos pedidos a entregar y con conductores lo hacen llegar a los clientes.
 */

import com.example.transporte.Ayuda;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
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
    @FXML
    public ImageView aydaBtn;

    private Conexion conexion;
    private UsuarioCon user;
    private Stage primaryStage;
    private SceneController sceneController;
    public static String identifier;
    private Stage ayudaStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ayudaStage=new Stage();
        primaryStage = new Stage();
        this.sceneController = new SceneController(primaryStage);
        conexion = new Conexion();
        user = new UsuarioCon();
    }

    //Si el usuario es correcto logea al usuario
    public void Ingresar(ActionEvent actionEvent) {
        ingresarLogin();
    }

    //Te lleva a la ventana para registrar
    public void Registrar(ActionEvent actionEvent) {
            registrarse();
    }

    // Método para mostrar una alerta con un mensaje
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Ingreso de Usuario");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    public void ingresarLogin(){
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
            mostrarAlerta("Inicio de sesión fallido. Credenciales incorrectas.");
            // Mostrar un mensaje de error al usuario o tomar otras acciones según sea necesario
        }
    }
    public void registrarse(){
        System.out.println("Ir a registrarse");
        primaryStage = (Stage) registrarLink.getScene().getWindow();
        primaryStage.close();
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource("registro.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void Ayuda(MouseEvent mouseEvent) {
        ayudaStage = (Stage) aydaBtn.getScene().getWindow();
        ayudaStage.close();

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("ayuda.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Ayuda ayudaController = loader.getController(); // Obtén el controlador del cargador
        ayudaController.setRutaHTML("src/main/resources/html/ayudahtml.html"); // Llama a un método para establecer el HTML

        Scene scene = new Scene(root);
        ayudaStage.setScene(scene);
        ayudaStage.setTitle("Logistic24");
        ayudaStage.setResizable(false);

        String imagen = "src/main/resources/img/logo.png";
        Image image = new Image(new File(imagen).toURI().toString());
        ayudaStage.getIcons().add(image);

        ayudaStage.show();
    }
}