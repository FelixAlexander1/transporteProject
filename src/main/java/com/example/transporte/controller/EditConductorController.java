package com.example.transporte.controller;


import com.example.transporte.HelloApplication;
import com.example.transporte.conexion.UsuarioCon;
import com.example.transporte.model.Cliente;
import com.example.transporte.model.Conductor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.transporte.controller.HelloController.identifier;

public class EditConductorController implements Initializable {
    @FXML
    public TextField licenciaTxt;
    @FXML
    public TextField vehiculoTxt;
    @FXML
    public ChoiceBox<String> disponibilidadCB;
    @FXML
    public Button AcptarBtn;
    @FXML
    public TextField nombreTxt;
    @FXML
    public TextField emailTxt;
    @FXML
    public TextField contraseñaTxt;
    @FXML
    public TextField licenciaTxt2;
    @FXML
    public TextField vehiculoTxt2;
    @FXML
    public Button AñadirBtn;
    @FXML
    public ListView listview;
    @FXML
    public ImageView backBtn2;
    @FXML
    public ImageView backBtn1;
    @FXML
    private UsuarioCon usuarioCon;
    private Stage adminStage;
    private String nombre;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarioCon=new UsuarioCon();
        adminStage=new Stage();
        llenarListViewConductores(listview);
        // Crear una lista de opciones para el ChoiceBox
        ObservableList<String> opciones = FXCollections.observableArrayList("Disponible", "No Disponible");

        // Establecer las opciones en el ChoiceBox
        disponibilidadCB.setItems(opciones);

        // Establecer un valor predeterminado (opcional)
        disponibilidadCB.setValue("Disponible");
    }

    public void llenarListViewConductores(ListView<Conductor> listViewConductor) {
        List<Conductor> conductores = usuarioCon.obtenerConductores();
        // Crear un ObservableList a partir de la lista de clientes
        ObservableList<Conductor> items = FXCollections.observableArrayList(conductores);
        // Establecer los pedidos en el ListView
        listViewConductor.setItems(items);
        // Configurar el CellFactory para mostrar solo el nombre del cliente
        listViewConductor.setCellFactory(param -> new ListCell<Conductor>() {
            @Override
            protected void updateItem(Conductor conductor, boolean empty) {
                super.updateItem(conductor, empty);
                if (empty || conductor == null || conductor.getId() == -1) {
                    setText(null);
                } else {
                    setText("Nombre: "+conductor.getNombre());
                    nombre=conductor.getNombre();
                }
            }
        });
    }

    //Actualiza el conductor
    public void Aceptar(ActionEvent actionEvent) {
        // Obtener el conductor seleccionado en el ListView
        Conductor conductorSeleccionado = (Conductor) listview.getSelectionModel().getSelectedItem();
        if (conductorSeleccionado == null) {
            mostrarAlerta("No se ha seleccionado ningún conductor.");
            return;
        }

        // Obtener los valores de los campos
        String licencia = licenciaTxt.getText();
        String vehiculoStr = vehiculoTxt.getText();
        boolean disponible = disponibilidadCB.getValue().equals("Disponible");

        try {
            // Convertir el valor del vehículo a entero
            int vehiculo = Integer.parseInt(vehiculoStr);
            System.out.println(conductorSeleccionado.getId());
            // Actualizar el conductor en la base de datos
            boolean actualizacionExitosa = usuarioCon.actualizarConductor(
                    usuarioCon.obtenerIdConductorPorNombre(nombre), licencia, vehiculo, disponible);
            // Verificar si la actualización fue exitosa
            if (actualizacionExitosa) {
                volverAAdmin(AcptarBtn);
                System.out.println("Conductor actualizado correctamente.");
            } else {
                System.out.println("Error al actualizar el conductor.");

            }
        } catch (NumberFormatException e) {
            System.out.println("El campo 'Vehículo' debe ser un número entero.");
        }
    }

    //Añade el conductor
    public void añadir(ActionEvent actionEvent) {
        if(usuarioCon.registrarUsuarioConductor(nombreTxt.getText(),contraseñaTxt.getText(),emailTxt.getText(),licenciaTxt2.getText(), Integer.parseInt(vehiculoTxt2.getText()))){
            volverAAdmin(AñadirBtn);
        }
    }

    public void volverAAdmin(Button buton){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("administrador.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        adminStage.setTitle("Chat!");
        adminStage.setScene(scene);
        adminStage.show();
        adminStage = (Stage) buton.getScene().getWindow();
        adminStage.close();
    }

    public void volverAAdmin2(ImageView buton){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("administrador.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        adminStage.setTitle("Logistic24");
        adminStage.setScene(scene);
        String imagen= "src/main/resources/img/logo.png";
        Image image= new Image(new File(imagen).toURI().toString());
        adminStage.getIcons().add(image);
        adminStage.show();
        adminStage = (Stage) buton.getScene().getWindow();
        adminStage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Registro de Usuario");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void seleccionarConductor(Conductor conductor) {
        listview.getSelectionModel().select(conductor);
    }

    public void Seleccionar(MouseEvent mouseEvent) {

    }

    public void Seleccionar1(MouseEvent mouseEvent) {
        Conductor conductorSeleccionado = (Conductor) listview.getSelectionModel().getSelectedItem();
        if (conductorSeleccionado != null) {
            System.out.println("Cliente seleccionado");
            seleccionarConductor(conductorSeleccionado);
        }
    }

    //Vuelve a la ventana admin
    public void back1(MouseEvent mouseEvent) {
        volverAAdmin2(backBtn1);
    }
    public void back2(MouseEvent mouseEvent) {
        volverAAdmin2(backBtn2);
    }


}
