package fr.m2gla.istic.projet.constantes;

import fr.m2gla.istic.projet.activity.R;

/**
 * Created by mds on 10/04/15.
 */
public class Constant {

    // Nom des fichiers svg
    public static final String SVG_COLONNE_INCENDIE = "colonne_incendie";
    public static final String SVG_GROUPE_INCENDIE = "groupe_incendie";
    public static final String SVG_SECOURS_A_PERSONNE = "secours_a_personnes";
    public static final String SVG_VEHICULE_A_INCENDIE_SEUL = "vehicule_incendie_seul";
    public static final String SVG_VEHICULE_POST_COMMAND = "poste_commandement_prevu";

    // Nom des moyens correspondants aux fichiers svg
    public static final String VALUE_COLONNE_INCENDIE = "Colonne incendie";
    public static final String VALUE_GROUPE_INCENDIE = "Groupe incendie";
    public static final String VALUE_MOYEN_INTERVENTION_AERIEN = "Moyen aérien";
    public static final String VALUE_SECOURS_A_PERSONNE = "Secours à personne";
    public static final String VALUE_VEHICULE_A_INCENDIE = "Véhicule incendie";

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

    // image type svg variable
    public static final String TYPE_IMG = "type";

    public static String getImage(String title) {
        String imgID = SVG_VEHICULE_A_INCENDIE_SEUL;

        switch (title.toLowerCase()) {
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
        }
        return imgID;
    }

    // VSAV -- Véhicule de secours et d'assistance aux victimes
    // VSR -- Véhicule secours routier
    // VLCG -- Véhicule de liaison chef de groupe
    // EPA -- Echelle pivotante automatique
    // FTP -- Fourgon pompe-tonne
    public enum Vehicule {
        VSAV, VSR, VLCG, EPA, FPT
    }
}
