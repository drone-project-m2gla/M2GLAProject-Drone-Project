package fr.istic.m2gla.projet.initializer;

import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import service.position.GetDronePositionThread;


/**
 * ProjetServletContextListener launch application and log4j
 * @author baptiste
 */
public class ProjetServletContextListener implements ServletContextListener {
	private static final String LOG_DEV = "./webapps/ROOT/log/log4j.properties";
	private static final String LOG_PROD = "./webapps/sitserver/log/log4j.properties";
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		PropertyConfigurator.configure(LOG_PROD);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {}
}
