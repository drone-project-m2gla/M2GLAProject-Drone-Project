package entity;

import util.Constant;

/**
 * The interventions units.
 * Created by alban on 10/03/15.
 */
public class Unity extends AbstractEntity{
    private Position unitPosition;
    private String name;

    /**
     * Basic contruct, assign type
     */
    public Unity()
    {
        super();
        this.datatype = Constant.DATATYPE_UNITY;
    }
    /**
     * Unity position getter     *
     * @return Position
     */
    public Position getUnitPosition() {
        return unitPosition;
    }

    /**
     * Unity position setter     *
     * @param unitPosition Position
     */
    public void setUnitPosition(Position unitPosition) {
        this.unitPosition = unitPosition;
    }

    @Override
    public String toString() {
        return "Unity{"  +
                "datatype='" + datatype + "\'" +
                ", unitPosition=" + unitPosition +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unity unity = (Unity) o;

        if (name != null ? !name.equals(unity.name) : unity.name != null) return false;
        if (unitPosition != null ? !unitPosition.equals(unity.unitPosition) : unity.unitPosition != null) return false;
        if (datatype != null ? !datatype.equals(unity.datatype) : unity.datatype != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = unitPosition != null ? unitPosition.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String newName)
    {
        this.name = newName;
    }
}
