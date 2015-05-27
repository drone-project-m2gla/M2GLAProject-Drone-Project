package fr.istic.m2gla.projet.initializer;

import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import service.position.GetDronePositionThread;


/**
 * @author baptiste
 * @see ProjetServletContextListener launch log4j
 */
public class ProjetServletContextListener implements ServletContextListener {
	private static final String LOG_DEV = "./webapps/ROOT/log/log4j.properties";
	private static final String LOG_PROD = "./webapps/sitserver/log/log4j.properties";
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		PropertyConfigurator.configure(LOG_PROD);

        // Démarrage du Thread de position.
        GetDronePositionThread.createNewInstance(-1);
        new Thread(GetDronePositionThread.getInstance()).start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {}
}
