package fr.istic.m2gla.projet.initializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

public class ProjetServletContextListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		PropertyConfigurator.configure("./log/log4j.properties");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {}
}
