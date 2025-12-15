package com.mathew.gimnasio.util;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityUtil {

    // Usar esto cuando CREAS un usuario nuevo
    public static String encriptar(String textoPlano) {
        return BCrypt.hashpw(textoPlano, BCrypt.gensalt());
    }

    // Usar esto cuando haces LOGIN
    public static boolean verificar(String textoPlano, String hashGuardado) {
        return BCrypt.checkpw(textoPlano, hashGuardado);
    }
}