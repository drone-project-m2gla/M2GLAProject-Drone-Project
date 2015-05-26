package fr.m2gla.istic.projet.model;

/**
 * Created by mds on 07/04/15.
 * Class ${CLASS}
 * Definition d'un utilisateur
 */
public class User extends Entity {
    private String username;
    private String password;

    /**
     * Constructeur
     */
    public User() {
    }

    /**
     * Récupération du nom de l'utilisateur
     * @return : nom de l'utilisateur
     */
    public String getUsername() {
        return username;
    }

    /**
     * Renseignement du nom de l'utilisateur
     * @param username : nom de l'utilisateur
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Récupération du mot de passe de l'utilisateur
     * @return : mot de passe de l'utilisateur
     */
    public String getPassword() {
        return password;
    }

    /**
     * Renseignement du mot de passe de l'utilisateur
     * @param password : mot de passe de l'utilisateur
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
