package fr.m2gla.istic.projet.constantes;

import fr.m2gla.istic.projet.activity.R;

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

    public static final String VALUE_VEHICULE_VSAV = "VSAV";
    public static final String VALUE_VEHICULE_VSR = "VSR";
    public static final String VALUE_VEHICULE_VLCG = "VLCG";
    public static final String VALUE_VEHICULE_EPA = "EPA";
    public static final String VALUE_VEHICULE_FPT = "FPT";


    // Text
    public static final int DRAWABLE_VEHICULE_VSAV = R.mipmap.ic_vehicule_vsav;
    public static final int DRAWABLE_VEHICULE_VSR = R.mipmap.ic_vehicule_vsr;
    public static final int DRAWABLE_VEHICULE_VLCG = R.mipmap.ic_vehicule_vlcg;
    public static final int DRAWABLE_VEHICULE_EPA = R.mipmap.ic_vehicule_epa;
    public static final int DRAWABLE_VEHICULE_FPT = R.mipmap.ic_vehicule_fpt;

    // Image
    public static final int DRAWABLE_IMG_VEHICULE_VSAV = R.mipmap.ic_vehicule_img_vsav;
    public static final int DRAWABLE_IMG_VEHICULE_VSR = R.mipmap.ic_vehicule_img_vsr;
    public static final int DRAWABLE_IMG_VEHICULE_VLCG = R.mipmap.ic_vehicule_img_vlcg;
    public static final int DRAWABLE_IMG_VEHICULE_EPA = R.mipmap.ic_vehicule_img_epa;
    public static final int DRAWABLE_IMG_VEHICULE_FPT = R.mipmap.ic_vehicule_img_fpt;


    // VSAV -- Véhicule de secours et d'assistance aux victimes
    // VSR -- Véhicule secours routier
    // VLCG -- Véhicule de liaison chef de groupe
    // EPA -- Echelle pivotante automatique
    // FTP -- Fourgon pompe-tonne
    public enum Vehicule {
        VSAV, VSR, VLCG, EPA, FPT
    }
}
