package entity;

import java.util.Random;

/**
 * @author alban
 * Abstract class for Entity
 * AbstractEntity provides methods for Entity
 * Also use for create random identify
 * This class is used by other Entity classes for the generic methods
 */
public abstract class AbstractEntity {
    /**
     * Unique id of entity
     */
    protected long id;

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

}
