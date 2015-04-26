package dao;

import org.bson.Document;
import util.Constant;
import util.Tools;
import entity.Mean;

/**
 * Created by arno on 08/04/15.
 */
public class MeanDAO extends AbstractDAO<Mean>{

    /**
     * Contructeur MeanDAO
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
