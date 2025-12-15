package com.mathew.gimnasio.modelos;

public class Credenciales {
    private String usuario;
    private String contrasena;

    public Credenciales() {} // Constructor vacío obligatorio

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}