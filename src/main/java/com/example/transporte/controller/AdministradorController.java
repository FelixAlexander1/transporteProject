package com.example.transporte.controller;

/**
 * @author Alexander Montesdeoca Garcia
 * @since 11-03-24
 * @version 1.0
 * Aplicacion que emula una empresa de logistica a la cual le llegan unos pedidos a entregar y con conductores lo hacen llegar a los clientes.
 */

import com.example.transporte.HelloApplication;
import com.example.transporte.conexion.UsuarioCon;
import com.example.transporte.model.Cliente;
import com.example.transporte.model.Ruta;
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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.example.transporte.controller.HelloController.identifier;

public class AdministradorController implements Initializable {
    @FXML
    public Circle circle;
    @FXML
    public Label labelNombre;
    @FXML
    public ListView listview1;
    @FXML
    public TextField textfield1;
    @FXML
    public ListView listview2;
    @FXML
    public TextField textfield11;
    @FXML
    public ImageView ImagenBtn;
    private UsuarioCon usuarioCon;
    private Stage adminStage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarioCon=new UsuarioCon();
        adminStage=new Stage();
        labelNombre.setText(identifier);
        llenarListViewRuta(listview1);
        List<Cliente> todosClientes = usuarioCon.obtenerClientes();
        llenarListViewClientes(listview2,todosClientes);
        String imagen= "src/main/resources/img/3135768.png";
        Image image= new Image(new File(imagen).toURI().toString());
        circle.setFill(new ImagePattern(image));
    }
    //Llena el listview con las rutas
    public void llenarListViewRuta(ListView<Ruta> listViewRuta) {
        List<Ruta> rutas = usuarioCon.obtenerRutasPorConductor(textfield1.getText());
        // Crear un ObservableList a partir de la lista de pedidos
        ObservableList<Ruta> items = FXCollections.observableArrayList(rutas);
        // Establecer los pedidos en el ListView
        listViewRuta.setItems(items);
        // Configurar el CellFactory para mostrar id, fecha inicio y fecha final
        listViewRuta.setCellFactory(param -> new ListCell<Ruta>() {
            @Override
            protected void updateItem(Ruta ruta, boolean empty) {
                super.updateItem(ruta, empty);
                if (empty || ruta == null || ruta.getId() == -1) {
                    setText(null);
                } else {
                    setText("Id: "+ruta.getId()+", Fecha Inicio: "+ruta.getFechaInicio()+", Fecha Fin: "+ruta.getFechaFin());
                }
            }
        });
    }
    //Llena el listview con los clientes
    public void llenarListViewClientes(ListView<Cliente> listViewClientes, List<Cliente> clientes) {
        ObservableList<Cliente> items = FXCollections.observableArrayList(clientes);
        // Establecer los clientes en el ListView
        listViewClientes.setItems(items);
        // Configurar el CellFactory para mostrar solo el nombre del cliente
        listViewClientes.setCellFactory(param -> new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);
                if (empty || cliente == null || cliente.getId() == -1) {
                    setText(null);
                } else {
                    setText("Nombre: " + cliente.getNombre());
                }
            }
        });
    }

    public void Buscar(ActionEvent actionEvent) {
        llenarListViewRuta(listview1);
    }
    //Elimina la ruta seleccionada
    public void EliminarRuta(ActionEvent actionEvent) {
        Ruta rutaSeleccionada = (Ruta) listview1.getSelectionModel().getSelectedItem();
        if (rutaSeleccionada != null) {
            // Obtener el nombre del conductor asociado a la ruta seleccionada
            String nombreConductor = usuarioCon.obtenerNombreConductorPorRuta(rutaSeleccionada.getId());

            // Obtener todas las rutas asociadas al conductor
            List<Ruta> rutasConductor = usuarioCon.obtenerRutasPorConductor(nombreConductor);

            // Eliminar cada ruta asociada al conductor de la base de datos
            for (Ruta ruta : rutasConductor) {
                usuarioCon.eliminarRuta(nombreConductor);
            }

            // Eliminar la ruta seleccionada del ListView
            listview1.getItems().remove(rutaSeleccionada);

            // Mostrar un mensaje de éxito
            mostrarAlerta("Rutas eliminadas exitosamente.");
        } else {
            // Si no se ha seleccionado ninguna ruta, mostrar un mensaje de advertencia
            mostrarAlerta("Por favor, seleccione una ruta para eliminar.");
        }

    }

    public void seleccionarRuta(Ruta ruta) {
        listview1.getSelectionModel().select(ruta);
    }

    public void seleccionarCliente(Cliente cliente) {
        listview2.getSelectionModel().select(cliente);
    }

    //Da de baja al cliente
    public void DarBaja(ActionEvent actionEvent) {
        Cliente clienteSeleccionado = (Cliente) listview2.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            usuarioCon.eliminarUsuario(clienteSeleccionado.getNombre());
            // Volver a llenar el ListView después de eliminar el cliente
            List<Cliente> todosClientes = usuarioCon.obtenerClientes();
            llenarListViewClientes(listview2,todosClientes);
        }
    }

    //Busca el cliente por el id
    public void BuscarClientes(ActionEvent actionEvent) {
        // Obtener el término de búsqueda ingresado por el usuario
        String terminoBusqueda = textfield11.getText().trim().toLowerCase();

        // Obtener todos los clientes
        List<Cliente> todosClientes = usuarioCon.obtenerClientes();

        // Filtrar los clientes que coincidan con el término de búsqueda
        List<Cliente> clientesFiltrados = todosClientes.stream()
                .filter(cliente -> cliente.getNombre().toLowerCase().contains(terminoBusqueda))
                .collect(Collectors.toList());

        // Actualizar el ListView con los resultados filtrados
        llenarListViewClientes(listview2, clientesFiltrados);
    }

    public void Seleccionar(MouseEvent mouseEvent) {
        Cliente clienteSeleccionado = (Cliente) listview2.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            System.out.println("Cliente seleccionado");
            seleccionarCliente(clienteSeleccionado);
        }
    }

    public void Seleccionar2(MouseEvent mouseEvent) {
        Ruta rutaSeleccionado = (Ruta) listview1.getSelectionModel().getSelectedItem();
        if (rutaSeleccionado != null) {
            System.out.println("Ruta seleccionado");
            seleccionarRuta(rutaSeleccionado);
        }
    }

    //Abrir la ventana para editar el conductor o añadirlo
    public void EditarConduc(MouseEvent mouseEvent) {
        adminStage = (Stage) ImagenBtn.getScene().getWindow();
        adminStage.close();
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource("EditConductor.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        adminStage.setTitle("Logistic24");
        adminStage.setScene(scene);
        adminStage.setResizable(false);
        String imagen= "src/main/resources/img/logo.png";
        Image image= new Image(new File(imagen).toURI().toString());
        adminStage.getIcons().add(image);
        adminStage.show();



    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
