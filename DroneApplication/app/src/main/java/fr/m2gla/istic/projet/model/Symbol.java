package fr.m2gla.istic.projet.model;

/**
 * Created by fernando on 4/14/15.
 */
public class Symbol {
    public enum SymbolType {
        colonne_incendie_active,
        groupe_incendie_actif,
        moyen_intervention_aerien_actif,
        moyen_intervention_aerien_prevu,
        pc_colonne_deux_fonctions_actif,
        pc_site,
        point_ravitaillement,
        poste_commandement_prevu,
        prise_eau_non_perenne,
        prise_eau_perenne,
        secours_a_personnes_actif,
        secours_a_personnes_prevu,
        vehicule_incendie_seul_actif,
        vehicule_incendie_seul_prevu,
        danger,
        etoile,
        point_sensible
    }

    private SymbolType symbolType;
    private String description;
    private String firstText;
    private String secondText;
    private String color;
    private boolean validated;
    private boolean topographic;

    public Symbol(SymbolType symbolType, String firstText, String secondText, String color, String description) {
        this.symbolType = symbolType;
        this.description = description;
        this.firstText = firstText;
        this.secondText = secondText;
        this.color = color;
        this.validated = false;
    }

    public Symbol(SymbolType symbolType, String firstText, String secondText, String color, String description, boolean topographic) {
        this(symbolType, firstText, secondText, color, description);
        this.topographic = topographic;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public String getDescription() {
        return description;
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
