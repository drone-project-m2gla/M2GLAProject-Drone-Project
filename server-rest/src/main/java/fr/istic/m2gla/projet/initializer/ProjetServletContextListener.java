package fr.istic.m2gla.projet.initializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.core.Response;

import dao.GeoIconDAO;
import dao.UserDAO;
import entity.GeoIcon;
import entity.Position;
import entity.User;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import service.position.GetDronePositionThread;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

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
