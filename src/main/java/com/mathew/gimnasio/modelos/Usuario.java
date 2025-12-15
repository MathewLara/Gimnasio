package com.mathew.gimnasio.modelos;

import java.io.Serializable;
import java.sql.Timestamp;

public class Usuario implements Serializable {
    private int idUsuario;
    private int idRol;
    private String usuario;
    private String contrasena;
    private Timestamp fechaCreacion;

    // CONSTRUCTOR VACÍO (¡Obligatorio!)
    public Usuario() {}

    // GETTERS Y SETTERS (¡Obligatorios!)
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
