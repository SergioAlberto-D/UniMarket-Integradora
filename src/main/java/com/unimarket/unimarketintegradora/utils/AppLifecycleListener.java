package com.unimarket.unimarketintegradora.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppLifecycleListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Esto se ejecuta al iniciar la app (no necesitamos hacer nada aquí por ahora)
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Esto se ejecuta justo un segundo antes de que Tomcat recargue o apague la app
        System.out.println("Tomcat está recargando/apagando la app. Cerrando conexiones...");
        SQLConnector.cerrarPool();
    }
}