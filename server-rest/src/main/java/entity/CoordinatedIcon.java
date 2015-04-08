package entity;

import util.Constant;

/**
 * Created by jerem on 08/04/15.
 *
 *  Retourne une coordonnée satellite pour la carte avec son signalement (icone "statique)
 */
public class CoordinatedIcon extends AbstractEntity {

    private Icon icon;

    private Position position;


    public CoordinatedIcon() {
        super();
        this.datatype = Constant.DATATYPE_COORDINATED_ICON;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoordinatedIcon coordinatedIcon = (CoordinatedIcon) o;

        if (icon != null ? !icon.equals(coordinatedIcon.icon) : coordinatedIcon.icon != null)
            return false;
        if (position != null ? !position.equals(coordinatedIcon.position) : coordinatedIcon.position != null)
            return false;

        return true;
    }
}