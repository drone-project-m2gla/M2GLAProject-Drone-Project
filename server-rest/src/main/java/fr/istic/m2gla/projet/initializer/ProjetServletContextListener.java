package fr.istic.m2gla.projet.initializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;
import service.position.GetDronePositionThread;

public class ProjetServletContextListener implements ServletContextListener {
	private static final String LOG_DEV = "./webapps/ROOT/log/log4j.properties";
	private static final String LOG_PROD = "./webapps/sitserver/log/log4j.properties";
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		PropertyConfigurator.configure(LOG_PROD);

        // Démarrage du Thread de position.
        GetDronePositionThread.createNewInstance();
        new Thread(GetDronePositionThread.getInstance()).start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {}
}
