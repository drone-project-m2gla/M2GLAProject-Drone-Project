package dao;

import org.bson.Document;
import util.Constant;
import util.Tools;
import entity.Mean;

/**
 * @author arno
 * This class implements these abstract methods for Mean transformation
 */
public class MeanDAO extends AbstractDAO<Mean>{

    /**
     * Contructor MeanDAO
     */
    public MeanDAO()
    {
        this.datatype = Constant.DATATYPE_MEAN;
    }

    @Override
    protected Mean documentToEntity(Document document) {
        return Tools.documentToMean(document);
    }

    @Override
    protected Document entityToDocument(Mean entity) {
        return Tools.meanToDocument(entity);
    }
}
