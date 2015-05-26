package fr.m2gla.istic.projet.strategy;

/**
 * Created by baptiste on 09/04/15.
 */
public interface Strategy {

    /**
     * Retourne le code du message GCM traité par la stratégie
     * @return code du message traité
     */
    public String getScopeName();

    /**
     * Retourne le type de l'objet par réflexion
     * @return type de l'objet transmis
     */
    public Class<?> getType();

    /**
     * Opération à appeler lors de la réception du message
     * @param object objet transmis
     */
    public void call(Object object);
}
