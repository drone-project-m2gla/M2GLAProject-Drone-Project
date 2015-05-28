package fr.istic.m2gla.projet.initializer;

import org.apache.log4j.PropertyConfigurator;

import util.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ProjetServletContextListener launch application and log4j
 * @author baptiste
 */
public class ProjetServletContextListener implements ServletContextListener {	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		PropertyConfigurator.configure(Configuration.getPATH_LOG());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {}
}
