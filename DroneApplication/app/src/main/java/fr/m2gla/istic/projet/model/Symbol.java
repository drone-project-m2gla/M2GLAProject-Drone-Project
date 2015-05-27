package fr.m2gla.istic.projet.model;

import fr.m2gla.istic.projet.context.GeneralConstants;

/**
 * Created by fernando on 4/14/15.
 * Marqueur sur la carte pour représenter un élément topographique ou un moyen
 */
public class Symbol {
    public enum SymbolType {
        colonne_incendie,
        groupe_incendie,
        moyen_intervention_aerien,
        pc_colonne_deux_fonctions_actif,
        pc_site,
        point_ravitaillement,
        poste_commandement_prevu,
        prise_eau_non_perenne,
        prise_eau_perenne,
        secours_a_personnes,
        vehicule_incendie_seul,
        danger,
        etoile,
        point_sensible
    }

    private String id;
    private SymbolType symbolType;
    private String firstText;
    private String secondText;
    private String color;
    private boolean validated;
    private boolean topographic;

    /**
     * Constructeur
     * @param symbolType : Type du symbole
     * @param firstText : Texte primaire ou texte de définition du type
     * @param secondText : Texte secondaire ou texte de spécification
     * @param color : Couleur associée
     */
    public Symbol(SymbolType symbolType, String firstText, String secondText, String color) {
        this.symbolType = symbolType;
        this.firstText = firstText;
        this.secondText = secondText;
        this.color = color;
        this.validated = false;
    }

    /**
     * Constructeur
     * @param id : Identifiant du moyen ou de l'élément
     * @param symbolType : Type du symbole
     * @param firstText : Texte primaire ou texte de définition du type
     * @param secondText : Texte secondaire ou texte de spécification
     * @param color : Couleur associée
     */
    public Symbol(String id, SymbolType symbolType, String firstText, String secondText, String color) {
        this(symbolType, firstText, secondText, color);
        this.id = id;
    }

    /**
     * Constructeur
     * @param symbolType : Type du symbole
     * @param firstText : Texte primaire ou texte de définition du type
     * @param secondText : Texte secondaire ou texte de spécification
     * @param color : Couleur associée
     * @param topographic : true pour indiquer un élément topographique, false pour un moyen
     */
    public Symbol(SymbolType symbolType, String firstText, String secondText, String color, boolean topographic) {
        this(symbolType, firstText, secondText, color);
        this.topographic = topographic;
    }

    /**
     * Récupération de l'identifiant du moyen / de l'élément topographique
     * @return : identifiant du moyen / élément topographique
     */
    public String getId() {
        return id;
    }

    /**
     * Récupération du type du symbole
     * @return : type du symbole
     */
    public SymbolType getSymbolType() {
        return symbolType;
    }

    /**
     * Récupération du texte  primaire
     * @return : texte primaire
     */
    public String getFirstText() {
        return firstText;
    }

    /**
     * Récupération du texte secondaire
     * @return : texte secondaire
     */
    public String getSecondText() {
        return secondText;
    }

    /**
     * Récupération de la couleur associée au symbole
     * @return : couleur associée au symbole
     */
    public String getColor() {
        return color;
    }

    /**
     * Vérification de la validation du symbole
     * @return : true si validé, false sinon
     */
    public boolean isValidated() {
        return validated;
    }

    /**
     * Positionnement de la validation
     * @param validated : true si validé, false sinon
     */
    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    /**
     * Vérification de la catégorie du symbole
     * @return : true si le symbole est celui d'un élément topographique ou celui d'un moyen
     */
    public boolean isTopographic() {
        return topographic;
    }

    /**
     * Récupération de l'image associé à un titre de type de véhicule
     * @param title : nom de type de véhicule
     * @return : Image sous forme de chaine de caractères
     */
    public static String getImage(String title) {
        String imgID = GeneralConstants.SVG_VEHICULE_A_INCENDIE_SEUL;

        /*switch (title.toLowerCase()) {
            case "vsav":
                imgID = SVG_SECOURS_A_PERSONNE;
                break;
            case "vsr":
                imgID = SVG_COLONNE_INCENDIE;
                break;
            case "vlcg":
                imgID = SVG_VEHICULE_POST_COMMAND;
                break;
            default:
                break;
        }*/
        return imgID;
    }

    /**
     * Récupération de la couleur associée à un type de vehicule
     * @param vehicle : type e véhicule
     * @return : Couleur sous forme de chaine de caractères
     */
    public static String getMeanColor(Vehicle vehicle) {
        String color = "ff0000";
        switch (vehicle) {
            case FPT:
                color = "ff0000";
                break;
            case VSAV:
                color = "00ff00";
                break;
            case CCGC:
                color = "0000ff";
                break;
            case VLCG:
                color = "551a8b";
                break;
            default:
                break;
        }
        return color;
    }

    /**
     * Récupération du trigram lié à une ville
     * @return : RNS !
     */
    /*public static String getCityTrigram(){
        return "RNS";
    }*/
}
