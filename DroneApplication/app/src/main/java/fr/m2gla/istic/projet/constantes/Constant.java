package fr.m2gla.istic.projet.constantes;

/**
 * Created by mds on 10/04/15.
 */
public class Constant {

    // Nom des fichiers svg
    public static final String SVG_COLONNE_INCENDIE_ACTIVE = "colonne_incendie_active";
    public static final String SVG_GROUPE_INCENDIE_ACTIF = "groupe_incendie_actif";
    public static final String SVG_MOYEN_INTERVENTION_AERIEN = "moyen_intervention_aerien_prevu";
    public static final String SVG_SECOUR_A_PERSONNE_PREVU = "secours_a_personnes_prevu";
    public static final String SVG_VEHICULE_A_INCENDIE_SEUL = "vehicule_incendie_seul_prevu";

    // Nom des moyens correspondants aux fichiers svg
    public static final String VALUE_COLONNE_INCENDIE_ACTIVE = "Colonne incendie";
    public static final String VALUE_GROUPE_INCENDIE_ACTIF = "Groupe incendie";
    public static final String VALUE_MOYEN_INTERVENTION_AERIEN = "Moyen aérien";
    public static final String VALUE_SECOUR_A_PERSONNE_PREVU = "Secours à personne";
    public static final String VALUE_VEHICULE_A_INCENDIE_SEUL = "Véhicule incendie";


    public enum Symbols {
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
}
