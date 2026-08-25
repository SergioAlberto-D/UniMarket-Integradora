package com.unimarket.unimarketintegradora.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppLifecycleListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        System.out.println("Tomcat está recargando/apagando la app. Cerrando conexiones...");
        SQLConnector.cerrarPool();
    }
}