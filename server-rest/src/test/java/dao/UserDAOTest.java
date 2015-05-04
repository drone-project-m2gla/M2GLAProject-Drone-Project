package dao;

import entity.Position;
import entity.User;
import org.apache.log4j.Logger;
import org.junit.*;
import service.impl.RetrieveAddressImpl;
import util.Configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by alban on 04/05/15.
 */
public class UserDAOTest {

    private static UserDAO dao = new UserDAO();

    private static final Logger LOGGER = Logger.getLogger(UserDAOTest.class);

    @BeforeClass
    public static void beforeAllTests() {
        Configuration.loadConfigurations();
        dao.connect();
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
        User user = new User();
        user.setPassword("chips");
        user.setUsername("adrezen");
        User userInBase = dao.create(user);
        assertEquals(user, userInBase);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setPassword("chips");
        user.setUsername("adrezen");
        User userInBase = dao.create(user);
        assertEquals(user, userInBase);
        userInBase.setPassword("Toto");
        User updated = dao.update(userInBase);
        assertEquals(userInBase,updated);
        assertEquals("Toto", updated.getPassword());
    }

    @Test
    public void testDelete() {
        User user = new User();
        user.setPassword("chips");
        user.setUsername("adrezen");
        User userInBase = dao.create(user);
        assertEquals(user, userInBase);
        dao.delete(userInBase);
        assertNull(dao.getById(userInBase.getId()));
    }
}
