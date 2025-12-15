package com.mathew.gimnasio.controladores;

import com.mathew.gimnasio.dao.UsuarioDAO;
import com.mathew.gimnasio.modelos.Usuario;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/usuarios") // URL base: /api/usuarios
public class UsuarioController {

    private UsuarioDAO dao = new UsuarioDAO();

    // 1. GET: Obtener todos (READ)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarUsuarios() {
        List<Usuario> lista = dao.listar();
        return Response.ok(lista).build();
    }

    // 2. GET: Obtener uno por ID (READ) -> Ejemplo URL: /api/usuarios/5
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerUsuario(@PathParam("id") int id) {
        Usuario u = dao.obtenerPorId(id);
        if (u != null) {
            return Response.ok(u).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
        }
    }

    // 3. POST: Crear nuevo (CREATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON) // Recibe JSON
    @Produces(MediaType.APPLICATION_JSON) // Devuelve JSON
    public Response crearUsuario(Usuario nuevoUsuario) {
        boolean exito = dao.crear(nuevoUsuario);
        if (exito) {
            return Response.status(Response.Status.CREATED).entity("Usuario creado con éxito").build();
        } else {
            return Response.status(500).entity("Error al crear usuario").build();
        }
    }

    // 4. PUT: Actualizar (UPDATE)
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizarUsuario(Usuario usuarioEditado) {
        boolean exito = dao.actualizar(usuarioEditado);
        if (exito) {
            return Response.ok("Usuario actualizado").build();
        } else {
            return Response.status(500).entity("Error al actualizar").build();
        }
    }

    // 5. DELETE: Borrar (DELETE) -> Ejemplo URL: /api/usuarios/5
    @DELETE
    @Path("/{id}")
    public Response eliminarUsuario(@PathParam("id") int id) {
        boolean exito = dao.eliminar(id);
        if (exito) {
            return Response.ok("Usuario eliminado").build();
        } else {
            return Response.status(500).entity("Error al eliminar").build();
        }
    }
}