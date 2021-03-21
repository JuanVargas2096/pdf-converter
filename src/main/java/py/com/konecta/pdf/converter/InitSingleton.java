/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.konecta.pdf.converter;

import java.util.Base64;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Juan Vargas <juan.vargas@konecta.com.py at Konecta S.A.>
 */
@Startup
@Singleton
public class InitSingleton {

    private static final Logger LOGGER = LogManager.getLogger(InitSingleton.class);

    @PostConstruct
    public void init() {
        LOGGER.info("[INICIANDO SINGLETON]");
        try {

            String elemento = Constantes.USER.concat(":").concat(Constantes.FRASE_SECRETA).concat(Constantes.PWD);
            String base64 = Base64.getEncoder().encodeToString(elemento.getBytes());
            Constantes.HEADER = "Basic ".concat(base64);
            LOGGER.info("[ALL THE CONSTANTS WERE GENERATED]");
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

}
