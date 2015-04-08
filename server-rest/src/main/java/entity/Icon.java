package entity;

import util.Constant;

/**
 * Created by jerem on 07/04/15.
 *
 * Retourne un icone "statique de la carte" avec son nom et le nom du fichier svg dans l'application android.
 */
public class Icon extends AbstractEntity  {

    private String filename; //nom du fichier sans l'extension
    private String entitled;


    public Icon() {
        super();
        this.datatype = Constant.DATATYPE_ICON;

    }

    public String getEntitled() {
        return entitled;
    }

    public void setEntitled(String entitled) {
        this.entitled = entitled;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Icon geoImage = (Icon) o;

        if (filename != null ? !filename.equals(geoImage.filename) : geoImage.filename != null)
            return false;
        if (entitled != null ? !entitled.equals(geoImage.entitled) : geoImage.entitled != null) return false;

        return true;
    }
//Classe avec une liste d'objets (id , intitulé, chemin_du_fichier)

//Classe avec une liste de coordonnées d'objets (id_objet_statique, coordonnées_satellite, description)


}
