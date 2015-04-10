package entity;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Random;

/**
 * Created by alban on 10/03/15.
 */
public abstract class AbstractEntity {
    /**
     * Unique id of entity
     */
    protected long id;

    /**
     * Type of entity
     */
    @JsonIgnore
    protected String datatype;

    /**
     * Basic contruct, assign a random id
     */
    public AbstractEntity()
    {
        Random random = new Random();
        id = random.nextInt();
    }

    public long getId() {
        return id;
    }

    public void setId(long id)
    {
        this.id=id;
    }

    public String getDataType() {
        return datatype;
    }
}
