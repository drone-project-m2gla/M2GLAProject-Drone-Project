package entity;

import util.Constant;

/**
 * Created by jerem on 07/04/15.
 */
public class Icon extends AbstractEntity  {

    private String address_file;
    private String entitled;


    public Icon() {
        super();
        this.datatype = Constant.DATATYPE_ICON;

    }

    public String getAddress_file() {
        return address_file;
    }

    public void setAddress_file(String address_file) {
        this.address_file = address_file;
    }

    public String getEntitled() {
        return entitled;
    }

    public void setEntitled(String entitled) {
        this.entitled = entitled;
    }


//Classe avec une liste d'objets (id , intitulé, chemin_du_fichier)

//Classe avec une liste de coordonnées d'objets (id_objet_statique, coordonnées_satellite, description)


}
