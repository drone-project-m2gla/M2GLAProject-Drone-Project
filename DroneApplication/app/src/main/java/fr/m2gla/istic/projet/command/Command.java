package fr.m2gla.istic.projet.command;

import org.apache.http.HttpResponse;

public interface Command {
    /**
     * Code a executer apres le callback http
     */
    public void execute(HttpResponse response);
}
