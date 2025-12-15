package com.mathew.gimnasio.controladores;

import com.mathew.gimnasio.dao.UsuarioDAO;
import com.mathew.gimnasio.modelos.Credenciales;
import com.mathew.gimnasio.modelos.Usuario;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.mathew.gimnasio.servicios.EmailService; // OJO con el nombre del paquete
import java.util.Random;

@Path("/auth") // La URL será: .../api/auth
public class AuthController {

    private UsuarioDAO dao = new UsuarioDAO();

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response iniciarSesion(Credenciales credenciales) {

        // 1. Validar usuario y contraseña (encriptada)
        Usuario usuarioEncontrado = dao.login(credenciales.getUsuario(), credenciales.getContrasena());

        if (usuarioEncontrado != null) {
            // 2. Buscar el email del usuario
            String emailDestino = dao.obtenerEmail(usuarioEncontrado.getIdUsuario());

            if (emailDestino == null) {
                return Response.status(500).entity("{\"mensaje\": \"Usuario sin email registrado.\"}").build();
            }

            // 3. Generar código aleatorio de 6 dígitos
            String codigo = String.format("%06d", new Random().nextInt(999999));

            // 4. Guardar código en BD
            dao.guardarCodigo2FA(usuarioEncontrado.getIdUsuario(), codigo);

            // 5. Enviar correo
            EmailService emailService = new EmailService();
            emailService.enviarCodigo(emailDestino, codigo);

            // 6. Responder al Postman
            return Response.ok("{\"mensaje\": \"Login correcto. Se ha enviado un código a " + emailDestino + "\"}").build();

        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"mensaje\": \"Credenciales incorrectas\"}")
                    .build();
        }
    }
}