package fr.m2gla.istic.projet.model;

/**
 * Created by fernando on 4/14/15.
 */
public class Symbol {
    public enum SymbolType {
        colonne_incendie_active,
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

    public Symbol(SymbolType symbolType, String firstText, String secondText, String color) {
        this.symbolType = symbolType;
        this.firstText = firstText;
        this.secondText = secondText;
        this.color = color;
        this.validated = false;
    }

    public Symbol(String id, SymbolType symbolType, String firstText, String secondText, String color) {
        this(symbolType, firstText, secondText, color);
        this.id = id;
    }

    public Symbol(SymbolType symbolType, String firstText, String secondText, String color, boolean topographic) {
        this(symbolType, firstText, secondText, color);
        this.topographic = topographic;
    }

    public String getId() {
        return id;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public String getFirstText() {
        return firstText;
    }

    public String getSecondText() {
        return secondText;
    }

    public String getColor() {
        return color;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public boolean isTopographic() {
        return topographic;
    }
}
