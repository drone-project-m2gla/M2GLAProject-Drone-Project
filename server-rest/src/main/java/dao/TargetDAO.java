package dao;

import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;

import util.Constant;
import util.Tools;
import entity.Target;

public class TargetDAO extends AbstractDAO<Target> {
	/**
	 * Contructor TargetDAO
	 */
	public TargetDAO() {
		this.datatype = Constant.DATATYPE_TARGET;
	}

	@Override
	protected Target documentToEntity(Document document) {
		if (document == null) {
			return null;
		}
		
		Target target = new Target();
		target.setId(document.getLong("_id"));
		target.setInterventionId(document.getInteger("interventionId"));
		target.setClose(document.getBoolean("isClose"));
		target.setPositions(Tools.documentListToPositionList(((List) document.get("positions"))));
		return target;
	}

	@Override
	protected Document entityToDocument(Target entity) {
		if (entity == null) {
			return null;
		}
		
		Document document = new Document();
		document.put("_id", entity.getId());
		document.put("interventionId", entity.getInterventionId());
		document.put("isClose", entity.isClose());
		document.put("positions", Tools.positionListToBasicDBList(entity.getPositions()));
		return document;
	}

	public Target getTargetByInterventionId(int interventionId) {
		FindIterable findIterable = collection.find(new BasicDBObject("interventionId", interventionId));
		return documentToEntity((Document) findIterable.first());
	}
}
