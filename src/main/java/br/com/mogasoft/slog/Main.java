/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mogasoft.slog;

import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Kegan Onomichi
 */
public class Main {
    
    public static void main(String[] args) {
        Logger log = LogManager.getLogger();
        log.info("TESTE INFO");
        log.trace("TESTE TRACE");
        log.debug("TESTE DEBUG");
        log.warn("TESTE WARN");
        log.error("TESTE ERROR", new Exception("Ocorreu um erro aqui!"));
        log.fatal("TESTE FATAL", new Exception("Ocorreu um erro aqui!"));
    }
    
}
