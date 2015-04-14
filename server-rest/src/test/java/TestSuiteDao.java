/**
 * Created by arno on 14/04/15.
 */

import dao.FlushBDDTest;
import dao.InterventionDAOTest;
import org.junit.runner.RunWith;
        import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
        FlushBDDTest.class,
        InterventionDAOTest.class
})
public class TestSuiteDao {
}