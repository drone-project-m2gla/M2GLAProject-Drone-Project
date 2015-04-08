package entity;

import util.Constant;

/**
 * Created by jerem on 08/04/15.
 *
 *  Retourne une coordonnée satellite pour la carte avec son signalement (icone "statique)
 */
public class CoordinatedIcon extends AbstractEntity {

    private Icon anIcone;

    private Position aPosition;


    public CoordinatedIcon() {
        super();
        this.datatype = Constant.DATATYPE_COORDINATED_ICON;
    }

    public Position getaPosition() {
        return aPosition;
    }

    public void setaPosition(Position aPosition) {
        this.aPosition = aPosition;
    }

    public Icon getAnIcone() {
        return anIcone;
    }

    public void setAnIcone(Icon anIcone) {
        this.anIcone = anIcone;
    }
}
