package com.mathew.gimnasio.dao;

import com.mathew.gimnasio.configuracion.ConexionDB;
import com.mathew.gimnasio.modelos.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.mathew.gimnasio.util.SecurityUtil;

public class UsuarioDAO {

    // 1. CREATE (Insertar un nuevo usuario)
    public boolean crear(Usuario u) {
        String sql = "INSERT INTO usuarios (id_rol, usuario, contrasena) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, u.getIdRol());
            ps.setString(2, u.getUsuario());

            // CAMBIO AQUÍ: Encriptamos antes de guardar
            String hash = SecurityUtil.encriptar(u.getContrasena());
            ps.setString(3, hash);

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. READ (Listar todos)
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setUsuario(rs.getString("usuario"));
                u.setContrasena("******"); // Por seguridad, no devolvemos la contraseña real
                u.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 3. READ (Buscar uno solo por ID)
    public Usuario obtenerPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        Usuario u = null;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setUsuario(rs.getString("usuario"));
                // ... setea los demás campos ...
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    // 4. UPDATE (Actualizar)
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET usuario = ?, id_rol = ? WHERE id_usuario = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getUsuario());
            ps.setInt(2, u.getIdRol());
            ps.setInt(3, u.getIdUsuario());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // Devuelve true si se actualizó algo
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. DELETE (Borrar)
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Usuario login(String user, String pass) {
        System.out.println("--- INTENTO DE LOGIN ---");
        System.out.println("1. Buscando usuario: " + user);

        String sql = "SELECT * FROM usuarios WHERE usuario = ?";
        Usuario u = null;

        try (java.sql.Connection conn = com.mathew.gimnasio.configuracion.ConexionDB.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("2. Usuario encontrado en BD.");
                String hashGuardado = rs.getString("contrasena");
                System.out.println("3. Hash en BD: " + hashGuardado);

                // INTENTO DE VERIFICACIÓN
                try {
                    boolean coincide = com.mathew.gimnasio.util.SecurityUtil.verificar(pass, hashGuardado);
                    System.out.println("4. Resultado de verificación: " + coincide);

                    if (coincide) {
                        u = new Usuario();
                        u.setIdUsuario(rs.getInt("id_usuario"));
                        u.setIdRol(rs.getInt("id_rol"));
                        u.setUsuario(rs.getString("usuario"));
                        u.setContrasena(null);
                    }
                } catch (Throwable errorLibreria) {
                    System.err.println("!!! ERROR GRAVE: NO SE ENCUENTRA JBCRYPT !!!");
                    errorLibreria.printStackTrace();
                }
            } else {
                System.out.println("2. Usuario NO encontrado en BD.");
            }
        } catch (Exception e) {
            System.out.println("Error de Base de Datos:");
            e.printStackTrace();
        }
        return u;
    }
    public boolean guardarCodigo2FA(int idUsuario, String codigo) {
        // El código será válido por 5 minutos
        String sql = "INSERT INTO codigos_verificacion (id_usuario, codigo, fecha_expiracion) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP + INTERVAL '5 minutes')";

        try (java.sql.Connection conn = com.mathew.gimnasio.configuracion.ConexionDB.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, codigo);

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public String obtenerEmail(int idUsuario) {
        String email = null;
        // Buscamos en ambas tablas
        String sql = "SELECT email FROM clientes WHERE id_usuario = ? " +
                "UNION " +
                "SELECT email FROM entrenadores WHERE id_usuario = ?";

        try (java.sql.Connection conn = com.mathew.gimnasio.configuracion.ConexionDB.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);

            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }
}
