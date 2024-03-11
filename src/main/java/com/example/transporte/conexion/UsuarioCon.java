package com.example.transporte.conexion;

import com.example.transporte.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioCon {
    public Connection conexion;

    public String name;

    public UsuarioCon() {
        conexion= Conexion.initConnection();
    }

    public TipoUsuario obtenerTipoUsuario(String email, String password) {
        String sql = "SELECT Tipo FROM Usuarios WHERE Email = ? AND Contraseña = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String tipoUsuarioStr = resultSet.getString("Tipo");
                    return TipoUsuario.valueOf(tipoUsuarioStr); // Devolver el tipo de usuario encontrado en la base de datos
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Devolver null si no se encuentra ningún usuario con las credenciales proporcionadas
    }

    public List<Ruta> obtenerRutasPorConductor(String nombreConductor) {
        String sql = "SELECT r.ID AS RutaID, r.FechaInicio, r.FechaFin, p.ID AS PedidoID, p.Nombre, p.Origen, p.Destino, p.FechaPedido, p.Estado, c.ID AS ConductorID, c.LicenciaConducir, c.VehiculoID, c.Disponible " +
                "FROM Rutas r " +
                "INNER JOIN Conductores c ON r.ConductorID = c.ID " +
                "INNER JOIN Usuarios u ON c.UsuarioID = u.ID " +
                "INNER JOIN Pedidos p ON r.PedidoID = p.ID " +
                "WHERE u.Nombre = ?";

        List<Ruta> rutas = new ArrayList<>();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, nombreConductor);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int rutaID = resultSet.getInt("RutaID");
                    LocalDateTime fechaInicio = resultSet.getTimestamp("FechaInicio").toLocalDateTime();
                    LocalDateTime fechaFin = resultSet.getTimestamp("FechaFin").toLocalDateTime();
                    int pedidoID = resultSet.getInt("PedidoID");
                    String nombrePedido = resultSet.getString("Nombre");
                    String origen = resultSet.getString("Origen");
                    String destino = resultSet.getString("Destino");
                    LocalDateTime fechaPedido = resultSet.getTimestamp("FechaPedido").toLocalDateTime();
                    String estadoStr = resultSet.getString("Estado");
                    EstadoPedido estado = EstadoPedido.valueOf(estadoStr);

                    // Datos del conductor
                    int conductorID = resultSet.getInt("ConductorID");
                    String licenciaConducir = resultSet.getString("LicenciaConducir");
                    boolean disponible = resultSet.getBoolean("Disponible");

                    // Crear el objeto Pedido
                    Pedido pedido = new Pedido(nombrePedido, pedidoID, null, origen, destino, fechaPedido, estado);

                    // Crear el objeto Conductor
                    Conductor conductor = new Conductor(conductorID,nombreConductor,"","", null,0,null, licenciaConducir, null, disponible);

                    // Crear el objeto Ruta
                    Ruta ruta = new Ruta(rutaID, pedido, conductor, fechaInicio, fechaFin);

                    // Agregar la ruta a la lista
                    rutas.add(ruta);
                }
            }
            return rutas;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Cliente> obtenerClientes() {
        //String sql = "SELECT c.ID, c.Nombre AS NombreCliente, c.Direccion, c.Telefono, u.Nombre AS NombreUsuario, u.Email, u.Tipo FROM Clientes c INNER JOIN Usuarios u ON c.UsuarioID = u.ID";

        String sql = "SELECT c.ID, u.Nombre AS NombreCliente, u.Email, c.Direccion, c.Telefono, u.Tipo " +
                "FROM Clientes c " +
                "INNER JOIN Usuarios u ON c.UsuarioID = u.ID";
        ArrayList<Cliente> clientes = new ArrayList<>();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String nombreCliente = resultSet.getString("NombreCliente");
                String email = resultSet.getString("Email");
                String direccion = resultSet.getString("Direccion");
                String telefono = resultSet.getString("Telefono");
                String tipo = resultSet.getString("Tipo");

                // Suponiendo que tienes un constructor en la clase Cliente que recibe estos parámetros
                Cliente cliente = new Cliente(id, nombreCliente, email, "", TipoUsuario.valueOf(tipo), 0, null, direccion, telefono);
                clientes.add(cliente);
            }
        }

        return clientes;
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String recibirNombre(String email, String passw) {
        // Consulta SQL con PreparedStatement para evitar la inyección de SQL
        String sql = "select Nombre from Usuarios where Email = ? and Contraseña = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, passw);

            try (ResultSet resultSet = statement.executeQuery()) {
                // Verificar si se encontraron resultados
                if (resultSet.next()) {
                    name = resultSet.getString("Nombre");
                    System.out.println(name);
                } else {
                    return "error";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public List<Pedido> obtenerPedidosConCliente(String nombre) {
        String sql = "SELECT p.ID AS PedidoID,p.Nombre, p.Origen, p.Destino, p.FechaPedido, p.Estado," +
                "       c.ID AS ClienteID, u.Nombre AS NombreCliente, c.Direccion, c.Telefono " +
                "FROM Pedidos p " +
                "INNER JOIN Clientes c ON p.ClienteID = c.ID " +
                "INNER JOIN Usuarios u ON c.UsuarioID = u.ID " +
                "WHERE u.Nombre = ?";

        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, nombre);
            try (ResultSet resultSet = statement. executeQuery()) {

                while (resultSet.next()) {
                    int pedidoID = resultSet.getInt("PedidoID");
                    String nombrePedido = resultSet.getString("Nombre");
                    String origen = resultSet.getString("Origen");
                    String destino = resultSet.getString("Destino");
                    LocalDateTime fechaPedido = resultSet.getTimestamp("FechaPedido").toLocalDateTime();
                    String estadoStr = resultSet.getString("Estado");
                    EstadoPedido estado = EstadoPedido.valueOf(estadoStr);
                    int clienteID = resultSet.getInt("ClienteID");
                    String nombreCliente = resultSet.getString("NombreCliente");
                    String direccion = resultSet.getString("Direccion");
                    String telefono = resultSet.getString("Telefono");

                    // Crear el objeto Cliente
                    Cliente cliente = new Cliente(clienteID, nombreCliente, "", "", null, 0, null, direccion, telefono);

                    // Crear el objeto Pedido con la información del cliente asociado
                    Pedido pedido = new Pedido(nombrePedido,pedidoID, cliente, origen, destino, fechaPedido, estado);

                    // Agregar el pedido a la lista
                    pedidos.add(pedido);
                }
        }

        return pedidos;
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pedido> obtenerNombresPedidosConCliente(String nombre) {
        String sql = "SELECT p.Nombre " +
                "FROM Pedidos p " +
                "INNER JOIN Clientes c ON p.ClienteID = c.ID " +
                "INNER JOIN Usuarios u ON c.UsuarioID = u.ID " +
                "WHERE u.Nombre = ?";

        List<Pedido> nombresPedidos = new ArrayList<>();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, nombre);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String nombrePedido = resultSet.getString("Nombre");
                    Pedido pedido = new Pedido(nombrePedido);
                    nombresPedidos.add(pedido);
                }
            }
            return nombresPedidos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void actualizarEstadoPedido(int pedidoID, EstadoPedido nuevoEstado) {
        String sql = "UPDATE Pedidos SET Estado = ? WHERE ID = ?";

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, nuevoEstado.name());
            statement.setInt(2, pedidoID);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No se encontró ningún pedido con el ID especificado.");
            } else {
                System.out.println("Estado del pedido actualizado exitosamente.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el estado del pedido.", e);
        }
    }

    public void actualizarFechaFinRuta(int rutaID, LocalDateTime nuevaFechaFin) {
        String sql = "UPDATE Rutas SET FechaFin = ? WHERE ID = ?";

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(nuevaFechaFin));
            statement.setInt(2, rutaID);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No se encontró ninguna ruta con el ID especificado.");
            } else {
                System.out.println("Fecha de fin de la ruta actualizada exitosamente.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la fecha de fin de la ruta.", e);
        }
    }
    public void eliminarRuta(int rutaID) {
        String sql = "DELETE FROM Rutas WHERE ID = ?";

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, rutaID);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No se encontró ninguna ruta con el ID especificado.");
            } else {
                System.out.println("Ruta eliminada exitosamente.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la ruta.", e);
        }
    }

    public List<Pedido> obtenerNombresProductosConConductor(String conductorNombre) {
        String sql = "SELECT p.ID, p.Nombre, p.Estado " +
                "FROM Pedidos p " +
                "INNER JOIN ConductoresPedidos cp ON p.ID = cp.PedidoID " +
                "INNER JOIN Conductores c ON cp.ConductorID = c.ID " +
                "INNER JOIN Usuarios u ON c.UsuarioID = u.ID " +
                "WHERE u.Nombre = ?";

        List<Pedido> nombresProductos = new ArrayList<>();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, conductorNombre);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int idProducto = resultSet.getInt("ID");
                    String nombreProducto = resultSet.getString("Nombre");
                    String estadoProducto = resultSet.getString("Estado");
                    Pedido pedido = new Pedido(nombreProducto, idProducto, EstadoPedido.valueOf(estadoProducto));
                    nombresProductos.add(pedido);
                }
            }
            return nombresProductos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
