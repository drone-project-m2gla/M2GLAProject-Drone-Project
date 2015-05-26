package fr.m2gla.istic.projet.command;

import org.apache.http.HttpResponse;

public interface Command {
    /**
     * Code a executer apres le callback http
     * @param response : objet traité
     */
    public void execute(Object response);
}
