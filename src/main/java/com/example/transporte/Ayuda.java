package com.example.transporte;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Ayuda implements Initializable {
    @FXML
    public Button volverBtl;
    @FXML
    private WebView Cliente;
    @FXML
    private WebView Conductor;
    @FXML
    private WebView Admin;

    private Stage ayudaStage;
    private String contenidoHTMLCliente;
    private String contenidoHTMLConductor;
    private String contenidoHTMLAdmin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setRutaHTML(String rutaHTML) {
        String contenidoHTML = leerArchivoHTML(rutaHTML);
        dividirContenidoHTML(contenidoHTML);
        mostrarContenido();
    }

    private String leerArchivoHTML(String rutaHTML) {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaHTML))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contenido.toString();
    }

    private void dividirContenidoHTML(String contenidoHTML) {
        // Separador <!-- SEPARADOR -->
        String[] secciones = contenidoHTML.split("<!-- SEPARADOR -->");

        if (secciones.length >= 3) {
            contenidoHTMLCliente = secciones[0].trim();
            contenidoHTMLConductor = secciones[1].trim();
            contenidoHTMLAdmin = secciones[2].trim();
        } else {
            contenidoHTMLCliente = contenidoHTMLConductor = contenidoHTMLAdmin = contenidoHTML;
        }
    }

    public void mostrarContenido() {
        if (Cliente != null) {
            mostrarHTML(Cliente, contenidoHTMLCliente);
        }
        if (Conductor != null) {
            mostrarHTML(Conductor, contenidoHTMLConductor);
        }
        if (Admin != null) {
            mostrarHTML(Admin, contenidoHTMLAdmin);
        }
    }

    private void mostrarHTML(WebView webView, String contenidoHTML) {
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(contenidoHTML);
    }

    public void Volver(ActionEvent actionEvent) {
        ayudaStage = (Stage) volverBtl.getScene().getWindow();
        ayudaStage.close();
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource("hello-view.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ayudaStage.setTitle("Logistic24");
        ayudaStage.setScene(scene);
        ayudaStage.setResizable(false);
        String imagen= "src/main/resources/img/logo.png";
        Image image= new Image(new File(imagen).toURI().toString());
        ayudaStage.getIcons().add(image);
        ayudaStage.show();
    }


}
