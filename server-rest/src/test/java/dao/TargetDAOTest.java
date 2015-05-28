package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.Configuration;
import entity.Position;
import entity.Target;

public class TargetDAOTest {
    private static TargetDAO dao = new TargetDAO();

    private static final Logger LOGGER = Logger.getLogger(TargetDAOTest.class);

    @BeforeClass
    public static void beforeAllTests() {
        Configuration.loadConfigurations();
        dao.connect();
        dao.ensureIndex();
    }

    @AfterClass
    public static void afterAllTests() {
        dao.disconnect();
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() {


    }

    @Test
    public void testInsert()
    {
        List<Position> position = new ArrayList<Position>();
        position.add(new Position(7.0, 6.0, 4.0));

        Target target = new Target();
        target.setClose(true);
        target.setPositions(position);
        target.setInterventionId(0);
        Target targetInBase = dao.create(target);
        assertEquals(target, targetInBase);
    }

    @Test
    public void testUpdate() {
        List<Position> position = new ArrayList<Position>();
        position.add(new Position(7.0, 6.0, 4.0));

        Target target = new Target();
        target.setClose(true);
        target.setPositions(position);
        target.setInterventionId(0);
        Target targetInBase = dao.create(target);
        assertEquals(target, targetInBase);

        target.setClose(false);
        Target updated = dao.update(targetInBase);
        assertEquals(updated, targetInBase);
        assertTrue(targetInBase.isClose());
    }

    @Test
    public void testDelete() {
        List<Position> position = new ArrayList<Position>();
        position.add(new Position(7.0, 6.0, 4.0));

        Target target = new Target();
        target.setClose(true);
        target.setPositions(position);
        target.setInterventionId(0);
        Target targetInBase = dao.create(target);
        assertEquals(target, targetInBase);

        dao.delete(targetInBase);
        assertNull(dao.getById(targetInBase.getId()));
    }

}
