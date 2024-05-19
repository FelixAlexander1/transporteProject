package com.example.transporte.conexion;

import com.example.transporte.model.*;
import javafx.scene.control.Alert;

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

    // Método para registrar un nuevo usuario en la base de datos
    // Primero verifica si el nombre de usuario o el email ya existen
    public void registrarUsuario(String nombre, String contraseña, String email, String direccion, String telefono) {
        try (Connection conn = Conexion.initConnection()) {
            // Verificar si el nombre de usuario o el email ya existen
            String consulta = "SELECT COUNT(*) FROM Usuarios WHERE Nombre = ? OR Email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(consulta)) {
                stmt.setString(1, nombre);
                stmt.setString(2, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        System.out.println("El nombre de usuario o el email ya están en uso");
                        return;
                    }
                }
            }

            // Insertar nuevo usuario en la tabla Usuarios
            String insertarUsuario = "INSERT INTO Usuarios (Nombre, Contraseña, Email, Tipo) VALUES (?, ?, ?, 'Cliente')";
            try (PreparedStatement stmt = conn.prepareStatement(insertarUsuario, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nombre);
                stmt.setString(2, contraseña);
                stmt.setString(3, email);
                stmt.executeUpdate();
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                int usuarioID = -1;
                if (generatedKeys.next()) {
                    usuarioID = generatedKeys.getInt(1);
                }

                // Insertar los datos del cliente en la tabla Clientes
                String insertarCliente = "INSERT INTO Clientes (UsuarioID, Direccion, Telefono) VALUES (?, ?, ?)";
                try (PreparedStatement stmtCliente = conn.prepareStatement(insertarCliente)) {
                    stmtCliente.setInt(1, usuarioID);
                    stmtCliente.setString(2, direccion);
                    stmtCliente.setString(3, telefono);
                    stmtCliente.executeUpdate();
                }
            }

            System.out.println("Registro exitoso");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al registrar usuario");
        }
    }

    // Método para obtener el tipo de usuario basado en el email y la contraseña proporcionados
    // Ejecuta una consulta para buscar el usuario con las credenciales dadas
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

    // Método para obtener las rutas asignadas a un conductor específico
    // Ejecuta una consulta para buscar rutas basadas en el nombre del conductor
    public List<Ruta> obtenerRutasPorConductor(String nombreConductor) {
        String sql = "SELECT r.RutaID, r.FechaInicio, r.FechaFin, p.ID AS PedidoID, p.Nombre, p.Origen, p.Destino, p.FechaPedido, p.Estado, c.ID AS ConductorID, c.LicenciaConducir, c.VehiculoID, c.Disponible " +
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
                    LocalDateTime fechaInicio = null;
                    Timestamp fechaInicioTimestamp = resultSet.getTimestamp("FechaInicio");
                    if (fechaInicioTimestamp != null) {
                        fechaInicio = fechaInicioTimestamp.toLocalDateTime();
                    }
                    LocalDateTime fechaFin = null;
                    Timestamp fechaFinTimestamp = resultSet.getTimestamp("FechaFin");
                    if (fechaFinTimestamp != null) {
                        fechaFin = fechaFinTimestamp.toLocalDateTime();
                    }
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
                    Conductor conductor = new Conductor(conductorID, nombreConductor, "", "", null, 0, null, licenciaConducir, null, disponible);

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

    // Método para obtener todos los clientes de la base de datos
    // Ejecuta una consulta para obtener los registros de la tabla Clientes y Usuarios
    public ArrayList<Cliente> obtenerClientes() {
        //String sql = "SELECT c.ID, c.Nombre AS NombreCliente, c.Direccion, c.Telefono, u.Nombre AS NombreUsuario, u.Email, u.Tipo FROM Clientes c INNER JOIN Usuarios u ON c.UsuarioID = u.ID";

        String sql = "SELECT c.ID, u.Nombre AS NombreCliente, u.Email, c.Direccion, c.Telefono, u.Tipo " +
                "FROM Clientes c " +
                "INNER JOIN Usuarios u ON c.UsuarioID = u.ID WHERE u.Tipo = 'Cliente'";
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

    // Método para obtener el nombre de un usuario basado en su email y contraseña
    // Ejecuta una consulta para buscar el usuario con las credenciales dadas
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

    // Método para obtener los pedidos asociados a un cliente específico
    // Ejecuta una consulta para buscar pedidos basados en el nombre del cliente
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

    // Método para cambiar el estado de un pedido específico
    // Actualiza el estado del pedido en la base de datos
    public void actualizarEstadoPedido(int pedidoID, EstadoPedido nuevoEstado) {
        String sql = "UPDATE Pedidos SET Estado = ? WHERE ID = ?";

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, nuevoEstado.name());
            statement.setInt(2, pedidoID);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No se encontró ningún pedido con el ID especificado.");
            } else {
                if (nuevoEstado == EstadoPedido.EnCurso) {
                    // Si el nuevo estado es "EnCurso", actualiza la fecha de inicio de la ruta
                    actualizarFechaInicioRuta(pedidoID);
                } else if (nuevoEstado == EstadoPedido.Cancelado) {
                    // Si el nuevo estado es "Cancelado", elimina la ruta asociada al pedido
                    eliminarRutaPorPedido(pedidoID);
                }
                System.out.println("Estado del pedido actualizado exitosamente.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el estado del pedido.", e);
        }
    }


    // Método para eliminar una ruta asociada a un pedido específico
    // Elimina el registro de la tabla Rutas donde el PedidoID coincide con el pedidoID proporcionado
    private void eliminarRutaPorPedido(int pedidoID) {
        String sql = "DELETE FROM Rutas WHERE PedidoID = ?";

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, pedidoID);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No se encontró ninguna ruta asociada al pedido.");
            } else {
                System.out.println("Ruta eliminada exitosamente.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la ruta asociada al pedido.", e);
        }
    }

    // Método para actualizar la fecha de inicio de una ruta asociada a un pedido específico
    // Actualiza el campo FechaInicio en la tabla Rutas con la hora actual
    private void actualizarFechaInicioRuta(int pedidoID) {
        String sql = "UPDATE Rutas SET FechaInicio = ? WHERE PedidoID = ?";

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            LocalDateTime fechaInicio = LocalDateTime.now(); // Obtener la hora actual
            statement.setTimestamp(1, Timestamp.valueOf(fechaInicio));
            statement.setInt(2, pedidoID);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No se encontró ninguna ruta asociada al pedido.");
            } else {
                System.out.println("Fecha de inicio de la ruta actualizada exitosamente.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la fecha de inicio de la ruta.", e);
        }
    }

    // Método para actualizar la fecha de fin de una ruta específica
    // Actualiza el campo FechaFin en la tabla Rutas con la nuevaFechaFin proporcionada
    public void actualizarFechaFinRuta(int rutaID, LocalDateTime nuevaFechaFin) {
        String sql = "UPDATE Rutas SET FechaFin = ? WHERE RutaID = ?";

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

    // Método para eliminar todas las rutas asociadas a un conductor específico
    // Elimina los registros de la tabla Rutas donde el ConductorID coincide con el conductor especificado
    public void eliminarRuta(String nombreConductor) {
        String sql = "DELETE FROM Rutas WHERE ConductorID = (SELECT ID FROM Conductores WHERE UsuarioID = (SELECT ID FROM Usuarios WHERE Nombre = ?))";

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, nombreConductor);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No se encontró ninguna ruta asociada al conductor especificado.");
            } else {
                System.out.println("Ruta eliminada exitosamente.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la ruta.", e);
        }
    }

    // Método para obtener los nombres de los productos (pedidos) asociados a un conductor específico
    // Ejecuta una consulta para buscar pedidos basados en el nombre del conductor
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

    // Método para eliminar un usuario de la base de datos
    // Elimina el registro de Usuario, Cliente y Conductor asociado al nombre de usuario proporcionado
    public void eliminarUsuario(String nombreUsuario) {
        try (Connection conn = Conexion.initConnection()) {
            // Obtener el ID del usuario a eliminar
            String obtenerIDUsuario = "SELECT ID FROM Usuarios WHERE Nombre = ?";
            int usuarioID = -1;
            try (PreparedStatement stmt = conn.prepareStatement(obtenerIDUsuario)) {
                stmt.setString(1, nombreUsuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        usuarioID = rs.getInt("ID");
                    } else {
                        System.out.println("El usuario especificado no existe.");
                        return;
                    }
                }
            }

            // Eliminar el registro del cliente asociado al usuario
            String eliminarCliente = "DELETE FROM Clientes WHERE UsuarioID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(eliminarCliente)) {
                stmt.setInt(1, usuarioID);
                stmt.executeUpdate();
            }

            // Eliminar el registro del conductor asociado al usuario
            String eliminarConductor = "DELETE FROM Conductores WHERE UsuarioID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(eliminarConductor)) {
                stmt.setInt(1, usuarioID);
                stmt.executeUpdate();
            }

            // Eliminar el registro del usuario
            String eliminarUsuario = "DELETE FROM Usuarios WHERE Nombre = ?";
            try (PreparedStatement stmt = conn.prepareStatement(eliminarUsuario)) {
                stmt.setString(1, nombreUsuario);
                stmt.executeUpdate();
            }

            System.out.println("Usuario eliminado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar el usuario.");
        }
    }

    // Método para obtener pedidos por su ID de la base de datos
    // Realiza una consulta SQL para seleccionar pedidos con el ID proporcionado
    public List<Pedido> obtenerPedidosPorId(int pedidoID) {
        String sql = "SELECT ID, Nombre, Origen, Destino, FechaPedido, Estado " +
                "FROM Pedidos " +
                "WHERE ID = ?";

        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, pedidoID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String nombre = resultSet.getString("Nombre");
                    String origen = resultSet.getString("Origen");
                    String destino = resultSet.getString("Destino");
                    LocalDateTime fechaPedido = resultSet.getTimestamp("FechaPedido").toLocalDateTime();
                    String estadoStr = resultSet.getString("Estado");
                    EstadoPedido estado = EstadoPedido.valueOf(estadoStr);

                    Pedido pedido = new Pedido(nombre, id, null, origen, destino, fechaPedido, estado);
                    pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    // Método para actualizar la dirección y el teléfono de un cliente en la base de datos
    // Realiza una consulta SQL para obtener el ID del usuario basado en el nombre de usuario proporcionado
    public boolean actualizarDireccionYTelefono(String usuarioName, String nuevaDireccion, String nuevoTelefono) {
        // Consulta para obtener el UsuarioID basado en el nombre de usuario
        String sqlSelectUserID = "SELECT ID FROM Usuarios WHERE Nombre = ?";

        // Consulta para actualizar la dirección y el teléfono del cliente
        String sqlUpdate = "UPDATE Clientes SET Direccion = ?, Telefono = ? WHERE UsuarioID = ?";

        try (
                // Preparar la consulta para obtener el UsuarioID
                PreparedStatement pstmtSelectUserID = conexion.prepareStatement(sqlSelectUserID);
                // Preparar la consulta para actualizar la dirección y el teléfono del cliente
                PreparedStatement pstmtUpdate = conexion.prepareStatement(sqlUpdate)
        ) {
            // Establecer el parámetro del nombre de usuario en la consulta de obtención del UsuarioID
            pstmtSelectUserID.setString(1, usuarioName);
            // Ejecutar la consulta para obtener el UsuarioID
            ResultSet rs = pstmtSelectUserID.executeQuery();

            // Verificar si se encontró un UsuarioID correspondiente al nombre de usuario proporcionado
            if (rs.next()) {
                int usuarioID = rs.getInt("ID"); // Obtener el UsuarioID

                // Establecer los parámetros de la consulta de actualización de dirección y teléfono
                pstmtUpdate.setString(1, nuevaDireccion);
                pstmtUpdate.setString(2, nuevoTelefono);
                pstmtUpdate.setInt(3, usuarioID); // Utilizar el UsuarioID obtenido

                // Ejecutar la consulta de actualización
                int affectedRows = pstmtUpdate.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Dirección y teléfono actualizados correctamente.");
                    return true;
                } else {
                    System.out.println("No se encontró el usuario.");
                    return false;
                }
            } else {
                System.out.println("No se encontró el usuario.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para actualizar la contraseña de un usuario en la base de datos
    // Realiza una consulta SQL para obtener la contraseña actual del usuario basado en el nombre de usuario proporcionado
    public boolean actualizarContrasena(String nombreUsuario, String contrasenaAntigua, String nuevaContrasena) {
        String sqlSelect = "SELECT Contraseña FROM Usuarios WHERE Nombre = ?";
        String sqlUpdate = "UPDATE Usuarios SET Contraseña = ? WHERE Nombre = ?";

        try (
                PreparedStatement pstmtSelect = conexion.prepareStatement(sqlSelect);
                PreparedStatement pstmtUpdate = conexion.prepareStatement(sqlUpdate)
        ) {
            // Verificar la contraseña antigua
            pstmtSelect.setString(1, nombreUsuario);
            ResultSet rs = pstmtSelect.executeQuery();

            if (rs.next()) {
                String storedContrasena = rs.getString("Contraseña");
                if (contrasenaAntigua.equals(storedContrasena)) {
                    // Contraseña antigua coincide, proceder a actualizar la contraseña
                    pstmtUpdate.setString(1, nuevaContrasena);
                    pstmtUpdate.setString(2, nombreUsuario);
                    int affectedRows = pstmtUpdate.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Contraseña actualizada correctamente.");
                        return true;
                    } else {
                        System.out.println("No se pudo actualizar la contraseña.");
                        return false;
                    }
                } else {
                    System.out.println("La contraseña antigua no coincide.");
                    return false;
                }
            } else {
                System.out.println("No se encontró el usuario.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para actualizar la información de un conductor en la base de datos
    // Verifica si el vehículo ya está asignado a otro conductor antes de realizar la actualización
    public boolean actualizarConductor(int conductorId, String nuevaLicencia, int nuevoVehiculoId, boolean disponible) {
        // Verificar si el vehículo ya está asignado a otro conductor
        if (esVehiculoAsignado(nuevoVehiculoId, conductorId)) {
            mostrarAlerta("El vehículo ya está asignado a otro conductor.");
            return false;
        }

        String sql = "UPDATE Conductores SET LicenciaConducir = ?, VehiculoID = ?, Disponible = ? WHERE ID = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nuevaLicencia);
            pstmt.setInt(2, nuevoVehiculoId);
            pstmt.setBoolean(3, disponible);
            pstmt.setInt(4, conductorId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método privado para verificar si un vehículo está asignado a otro conductor
    // Realiza una consulta para contar la cantidad de conductores que tienen asignado el vehículo especificado
    private boolean esVehiculoAsignado(int vehiculoId, int conductorId) {
        String sql = "SELECT COUNT(*) AS cantidad FROM Conductores WHERE VehiculoID = ? AND ID != ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, vehiculoId);
            pstmt.setInt(2, conductorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int cantidad = rs.getInt("cantidad");
                    return cantidad > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para obtener el ID de un conductor por su nombre
    // Realiza una consulta para obtener el ID del conductor basado en su nombre
    public int obtenerIdConductorPorNombre(String nombreConductor) {
        String sql = "SELECT c.ID FROM Conductores c JOIN Usuarios u ON c.UsuarioID = u.ID WHERE u.Nombre = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nombreConductor);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retorna -1 si no se encuentra el conductor
    }

    // Método para registrar un nuevo usuario conductor
    // Verifica si el nombre de usuario o el email ya están en uso
    public boolean registrarUsuarioConductor(String nombre, String contraseña, String email, String licencia, int vehiculoID) {
        try (Connection conn = Conexion.initConnection()) {
            // Verificar si el nombre de usuario o el email ya existen
            String consulta = "SELECT COUNT(*) FROM Usuarios WHERE Nombre = ? OR Email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(consulta)) {
                stmt.setString(1, nombre);
                stmt.setString(2, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        mostrarAlerta("El nombre de usuario o el email ya están en uso");
                        return false;
                    }
                }
            }

            // Verificar si el vehiculoID existe en la tabla Vehiculos
            String verificarVehiculo = "SELECT COUNT(*) FROM Vehiculos WHERE ID = ?";
            try (PreparedStatement stmtVerificar = conn.prepareStatement(verificarVehiculo)) {
                stmtVerificar.setInt(1, vehiculoID);
                try (ResultSet rsVerificar = stmtVerificar.executeQuery()) {
                    rsVerificar.next();
                    if (rsVerificar.getInt(1) == 0) {
                        mostrarAlerta("El vehículo especificado no existe");
                        return false;
                    }
                }
            }

            // Insertar nuevo usuario en la tabla Usuarios
            String insertarUsuario = "INSERT INTO Usuarios (Nombre, Contraseña, Email, Tipo) VALUES (?, ?, ?, 'Conductor')";
            try (PreparedStatement stmt = conn.prepareStatement(insertarUsuario, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nombre);
                stmt.setString(2, contraseña);
                stmt.setString(3, email);
                stmt.executeUpdate();
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                int usuarioID = -1;
                if (generatedKeys.next()) {
                    usuarioID = generatedKeys.getInt(1);
                }

                // Insertar los datos del conductor en la tabla Conductores
                String insertarConductor = "INSERT INTO Conductores (UsuarioID, LicenciaConducir, VehiculoID) VALUES (?, ?, ?)";
                try (PreparedStatement stmtConductor = conn.prepareStatement(insertarConductor)) {
                    stmtConductor.setInt(1, usuarioID);
                    stmtConductor.setString(2, licencia);
                    stmtConductor.setInt(3, vehiculoID);
                    stmtConductor.executeUpdate();
                }
            }

            mostrarAlerta("Registro exitoso");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al registrar usuario");
            return false;
        }
    }

    // Método para obtener la lista de todos los conductores con información detallada
    // Realiza una consulta para obtener información de usuarios y vehículos asociados a los conductores
    public ArrayList<Conductor> obtenerConductores() {

        String sql = "SELECT u.ID AS UsuarioID, u.Nombre, u.Email, u.Contraseña, u.Tipo, c.LicenciaConducir, c.VehiculoID, c.Disponible, " +
                "v.Marca, v.Modelo, v.Año, v.Capacidad, v.Matricula " +
                "FROM Usuarios u " +
                "INNER JOIN Conductores c ON u.ID = c.UsuarioID " +
                "INNER JOIN Vehiculos v ON c.VehiculoID = v.ID";

        ArrayList<Conductor> conductores = new ArrayList<>();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int usuarioID = resultSet.getInt("UsuarioID");
                    String nombre = resultSet.getString("Nombre");
                    String email = resultSet.getString("Email");
                    String licenciaConducir = resultSet.getString("LicenciaConducir");
                    int vehiculoID = resultSet.getInt("VehiculoID");
                    boolean disponible = resultSet.getBoolean("Disponible");

                    // Obtener datos adicionales del usuario
                    String contraseña = resultSet.getString("Contraseña");
                    TipoUsuario tipo = TipoUsuario.valueOf(resultSet.getString("Tipo"));

                    // Obtener datos del vehículo
                    String marca = resultSet.getString("Marca");
                    String modelo = resultSet.getString("Modelo");
                    int año = resultSet.getInt("Año");
                    int capacidad = resultSet.getInt("Capacidad");
                    String matricula = resultSet.getString("Matricula");

                    // Construir el objeto Usuario
                    Usuario usuario = new Usuario(usuarioID, nombre, email, contraseña, tipo);

                    // Construir el objeto Vehiculo
                    Vehiculo vehiculo = new Vehiculo(vehiculoID, marca, modelo, año, capacidad, matricula);

                    // Construir el objeto Conductor
                    Conductor conductor = new Conductor(usuarioID, nombre, email, contraseña, tipo, 0, usuario, licenciaConducir, vehiculo, disponible);
                    conductores.add(conductor);
                }
            }
            return conductores;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para ingresar un nuevo pedido con ruta
    // Inserta un nuevo pedido en la tabla Pedidos con los datos proporcionados
    public boolean ingresarPedidoConRuta(int pedidoID, String origen, String destino, String nombre) {
        try {
            // Insertar el pedido
            String insertarPedido = "INSERT INTO Pedidos (ID, Origen, Destino, Nombre) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmtPedido = conexion.prepareStatement(insertarPedido)) {
                stmtPedido.setInt(1, pedidoID);
                stmtPedido.setString(2, origen);
                stmtPedido.setString(3, destino);
                stmtPedido.setString(4, nombre);
                int filasPedido = stmtPedido.executeUpdate();

                // Verificar si el pedido se insertó correctamente
                if (filasPedido == 0) {
                    System.out.println("No se pudo insertar el pedido");
                    return false;
                }

                // Obtener conductores disponibles
                List<Integer> conductoresDisponibles = obtenerConductoresDisponibles();

                // Verificar si hay conductores disponibles
                if (!conductoresDisponibles.isEmpty()) {
                    // Crear la ruta con el primer conductor disponible
                    crearRuta(pedidoID, conductoresDisponibles.get(0));
                    System.out.println("Pedido asignado al conductor ID: " + conductoresDisponibles.get(0));
                } else {
                    System.out.println("No hay conductores disponibles en este momento");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al insertar el pedido", e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    // Método para comprobar la disponibilidad de los conductores y actualizarla según la cantidad de pedidos entregados
    // Obtiene todos los conductores y verifica la cantidad de pedidos entregados por cada uno
    public void comprobarDisponibilidadConductores() {
        try {
            List<Integer> conductores = obtenerTodosLosConductores();

            for (Integer conductorID : conductores) {
                int cantidadPedidos = obtenerCantidadProductosEntregados(conductorID);
                if (cantidadPedidos < 5) {
                    // Actualizar disponibilidad del conductor a true
                    actualizarDisponibilidadConductor(conductorID, true);
                    System.out.println("El conductor con ID " + conductorID + " tiene menos de 5 pedidos, su disponibilidad se ha establecido como verdadera.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al comprobar la disponibilidad de los conductores.");
        }
    }

    // Método para obtener todos los conductores
    // Realiza una consulta para obtener los IDs de todos los conductores en la tabla Conductores
    public List<Integer> obtenerTodosLosConductores() throws SQLException {
        List<Integer> conductores = new ArrayList<>();
        String consulta = "SELECT ID FROM Conductores";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(consulta)) {
            while (rs.next()) {
                conductores.add(rs.getInt("ID"));
            }
        }
        return conductores;
    }

    // Método para actualizar la disponibilidad de un conductor
    // Actualiza el campo 'Disponible' del conductor especificado en la tabla Conductores
    public void actualizarDisponibilidadConductor(int conductorID, boolean disponible) throws SQLException {
        String consulta = "UPDATE Conductores SET Disponible = ? WHERE ID = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
            stmt.setBoolean(1, disponible);
            stmt.setInt(2, conductorID);
            stmt.executeUpdate();
        }
    }

    // Método para asignar un pedido a un conductor disponible
    // Realiza los siguientes pasos:
    // 1. Obtiene una lista de conductores disponibles.
    // 2. Si hay conductores disponibles, selecciona el primero de la lista.
    // 3. Verifica si el conductor seleccionado puede aceptar más pedidos.
    // 4. Asigna el pedido al conductor seleccionado y cambia su estado de disponibilidad a false.
    public void asignarPedidoAConductorDisponible(int pedidoID) {
        try {
            // Paso 1: Obtener una lista de conductores disponibles
            List<Integer> conductoresDisponibles = obtenerConductoresDisponibles();

            if (!conductoresDisponibles.isEmpty()) {
                // Paso 2: Seleccionar un conductor (por ejemplo, el primero de la lista)
                int conductorID = conductoresDisponibles.get(0);

                // Verificar la cantidad de productos entregados por el conductor
                int cantidadProductosEntregados = obtenerCantidadProductosEntregados(conductorID);

                // Verificar si el conductor puede aceptar más pedidos
                if (cantidadProductosEntregados < 5) {
                    // Paso 3: Asignar el pedido al conductor seleccionado
                    String asignarPedido = "INSERT INTO ConductoresPedidos (ConductorID, PedidoID) VALUES (?, ?)";
                    try (PreparedStatement stmt = conexion.prepareStatement(asignarPedido)) {
                        stmt.setInt(1, conductorID);
                        stmt.setInt(2, pedidoID);
                        int filasInsertadas = stmt.executeUpdate();

                        if (filasInsertadas > 0) {
                            System.out.println("Pedido asignado al conductor ID: " + conductorID);

                            // Cambiar el estado de disponibilidad del conductor a false
                            cambiarEstadoDisponibilidadConductor(conductorID, false);
                        } else {
                            System.out.println("No se pudo asignar el pedido al conductor");
                        }
                    }
                } else {
                    // El conductor ya ha entregado 5 productos, por lo tanto, no está disponible
                    System.out.println("El conductor ID " + conductorID + " ya ha entregado 5 productos.");
                }
            } else {
                System.out.println("No hay conductores disponibles en este momento");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al asignar pedido al conductor");
        }
    }

    // Método para cambiar el estado de disponibilidad de un conductor
    // Actualiza el estado de disponibilidad del conductor en la base de datos
    public void cambiarEstadoDisponibilidadConductor(int conductorID, boolean disponible) {
        try {
            String actualizarDisponibilidad = "UPDATE Conductores SET Disponible = ? WHERE ID = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(actualizarDisponibilidad)) {
                stmt.setBoolean(1, disponible);
                stmt.setInt(2, conductorID);
                int filasActualizadas = stmt.executeUpdate();

                if (filasActualizadas > 0) {
                    System.out.println("Estado de disponibilidad del conductor actualizado.");
                } else {
                    System.out.println("No se pudo actualizar el estado de disponibilidad del conductor.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cambiar el estado de disponibilidad del conductor");
        }
    }

    // Método para obtener la lista de conductores disponibles
    // Realiza una consulta en la base de datos para obtener los IDs de los conductores disponibles
    public List<Integer> obtenerConductoresDisponibles() {
        List<Integer> conductoresDisponibles = new ArrayList<>();
        try {
            String consulta = "SELECT ID FROM Conductores WHERE Disponible = true";
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(consulta)) {
                while (rs.next()) {
                    int conductorID = rs.getInt("ID");
                    conductoresDisponibles.add(conductorID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener conductores disponibles");
        }
        return conductoresDisponibles;
    }

    // Método para obtener la cantidad de productos entregados por un conductor
    // Realiza una consulta en la base de datos para obtener la cantidad de productos entregados por el conductor especificado
    public int obtenerCantidadProductosEntregados(int conductorID) throws SQLException {
        String consulta = "SELECT COUNT(*) AS Cantidad FROM ConductoresPedidos WHERE ConductorID = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
            stmt.setInt(1, conductorID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Cantidad");
                }
            }
        }
        return 0;
    }

    // Método privado para crear una ruta asociada a un pedido y un conductor en la base de datos
    private boolean crearRuta(int pedidoID, int conductorID) throws SQLException {
        String insertarRuta = "INSERT INTO Rutas (PedidoID, ConductorID) VALUES (?, ?)";
        try (PreparedStatement stmtRuta = conexion.prepareStatement(insertarRuta)) {
            stmtRuta.setInt(1, pedidoID);
            stmtRuta.setInt(2, conductorID);
            int filasRuta = stmtRuta.executeUpdate();

            // Verificar si la ruta se insertó correctamente
            return filasRuta > 0;
        }
    }

    // Método para que las alertas se muestren en pantalla
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Registro de Usuario");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
