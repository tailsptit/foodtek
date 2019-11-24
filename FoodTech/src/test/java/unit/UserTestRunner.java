package test.java.unit;

import main.java.sol.Permission;
import main.java.sol.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class UserTestRunner {
    private User user = new User(new String[]{});

    @BeforeClass
    public static void init() {
        System.out.println("init");
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("tearDown");
    }

    @Before
    public void setup() {
        System.out.println("Setup for each testcase");
        user.setManager(-1);
        user.getDirectStaffs().clear();
        user.getPermissions().clear();
    }

    /**
     * Test: set manager for user
     */

    @Test
    public void setManager() {
        user.setManager(1);
        assertEquals(1, user.getManager());
        user.setManager(0);
        assertEquals(0, user.getManager());
    }

    /**
     * Test: get manager from user
     */
    @Test
    public void getManager() {
        user.setManager(2);
        assertEquals(2, user.getManager());
        user.setManager(0);
        assertEquals(0, user.getManager());
    }

    /**
     * Test: check user is manager or staff only
     */
    @Test
    public void isManager() {
        user.getDirectStaffs().clear();
        assertFalse(user.isManager());
        user.getDirectStaffs().add(1);
        assertTrue(user.isManager());
    }

    /**
     * Test: get all direct staffs that user manages directly to whom
     */
    @Test
    public void getDirectStaffs() {
        user.getDirectStaffs().clear();
        assertEquals(Set.of(), user.getDirectStaffs());
        user.getDirectStaffs().add(1);
        user.getDirectStaffs().add(2);
        assertEquals(Set.of(1, 2), user.getDirectStaffs());
    }

    /**
     * Test: add a direct staff to user
     */
    @Test
    public void addDirectStaff() {
        user.getDirectStaffs().clear();
        assertEquals(Set.of(), user.getDirectStaffs());
        user.addDirectStaff(1);
        user.addDirectStaff(2);
        assertEquals(Set.of(1, 2), user.getDirectStaffs());
    }

    /**
     * Test: get permissions from user
     */
    @Test
    public void getPermission() {
        user.getPermissions().clear();
        user.getPermissions().put("A", new Permission(true, 1));
        user.getPermissions().put("B", new Permission(true, 1));
        assertEquals(Set.of("A", "B"), user.getPermissions().keySet());
    }

    /**
     * Test: Add a direct-permission to user
     * Assume
     * - User has no permissions
     * Action
     * - Add permission P0 to user
     * Expected
     * User has permissions {P0}. In which, P0 is a direct-permission
     */
    @Test
    public void addDirectPermissions01() {
        user.getPermissions().clear();
        user.addDirectPermissions(Set.of("P0"));

        assertEquals(1, user.getPermissions().size());
        assertEquals(1, user.getPermissions().get("P0").getCount());
        assertTrue(user.getPermissions().get("P0").isDirect());
    }

    /**
     * Test: Add direct-permissions to user
     * Assume
     * - User has no permissions
     * Action
     * - Add permissions {P0, P1} to user
     * Expected
     * User has permissions {P0, P1}. In which, P0 and P1 are direct-permissions
     */
    @Test
    public void addDirectPermissions02() {
        user.getPermissions().clear();
        user.addDirectPermissions(Set.of("P0", "P1"));

        assertEquals(2, user.getPermissions().size());
        assertEquals(1, user.getPermissions().get("P0").getCount());
        assertTrue(user.getPermissions().get("P0").isDirect());
        assertEquals(1, user.getPermissions().get("P1").getCount());
        assertTrue(user.getPermissions().get("P1").isDirect());
    }

    /**
     * Test: Add direct-permissions to user
     * Assume
     * - User has permissions {P2, P3}
     * Action
     * - Add permissions {P0, P1} to user
     * Expected
     * User has permissions {P0, P1, P2, P3}. In which, P0, P1, P2 and P3 are direct-permissions
     */
    @Test
    public void addDirectPermissions03() {
        user.getPermissions().clear();
        user.getPermissions().put("P2", new Permission(true, 1));
        user.getPermissions().put("P3", new Permission(true, 1));

        user.addDirectPermissions(Set.of("P0", "P1"));
        assertEquals(4, user.getPermissions().size());
        assertEquals(1, user.getPermissions().get("P0").getCount());
        assertTrue(user.getPermissions().get("P0").isDirect());
        assertEquals(1, user.getPermissions().get("P1").getCount());
        assertTrue(user.getPermissions().get("P1").isDirect());
    }

    /**
     * Test: Add direct-permissions to user
     * Assume
     * - User has permissions {P1, P2}
     * Action
     * - Add permissions {P0, P1} to user
     * Expected
     * User has permissions {P0, P1, P2}. In which, P0, P1 and P2 are direct-permissions
     */
    @Test
    public void addDirectPermissions04() {
        user.getPermissions().clear();
        user.getPermissions().put("P1", new Permission(true, 1));
        user.getPermissions().put("P2", new Permission(true, 1));

        user.addDirectPermissions(Set.of("P0", "P1"));
        assertEquals(3, user.getPermissions().size());
        assertEquals(1, user.getPermissions().get("P0").getCount());
        assertTrue(user.getPermissions().get("P0").isDirect());
        assertEquals(1, user.getPermissions().get("P1").getCount());
        assertTrue(user.getPermissions().get("P1").isDirect());
    }

    /**
     * Test: Add direct-permissions to user
     * Assume
     * - User has permissions {P1, P2}. P1 is an indirect-permission
     * Action
     * - Add permissions {P0, P1} to user
     * Expected
     * User has permissions {P0, P1, P2}. In which, P0, P1 and P2 are direct-permissions
     */
    @Test
    public void addDirectPermissions05() {
        user.getPermissions().clear();
        user.getPermissions().put("P1", new Permission(false, 1));
        user.getPermissions().put("P2", new Permission(true, 1));

        user.addDirectPermissions(Set.of("P0", "P1"));
        assertEquals(3, user.getPermissions().size());
        assertEquals(1, user.getPermissions().get("P0").getCount());
        assertTrue(user.getPermissions().get("P0").isDirect());
        assertEquals(2, user.getPermissions().get("P1").getCount());
        assertTrue(user.getPermissions().get("P1").isDirect());
    }

    /**
     * Test: Add an indirect-permissions to user
     * Assume
     * - User has permissions {}
     * Action
     * - Add permissions {P0} to user
     * Expected
     * User has permissions {P0}. In which, P0 is an indirect-permission
     */
    @Test
    public void addIndirectPermission01() {
        user.getPermissions().clear();
        user.addIndirectPermission("P0", 1);
        assertEquals(1, user.getPermissions().size());
        assertFalse(user.getPermissions().get("P0").isDirect());
        assertEquals(1, user.getPermissions().get("P0").getCount());
    }

    /**
     * Test: Add an indirect-permissions to user
     * Assume
     * - User has permissions {P1}
     * Action
     * - Add permissions {P0} to user
     * Expected
     * User has permissions {P0}. In which, P0 is an indirect-permission
     */
    @Test
    public void addIndirectPermission02() {
        user.getPermissions().clear();
        user.getPermissions().put("P1", new Permission(true, 1));
        user.addIndirectPermission("P0", 1);
        assertEquals(2, user.getPermissions().size());
        assertFalse(user.getPermissions().get("P0").isDirect());
        assertEquals(1, user.getPermissions().get("P0").getCount());
    }

    /**
     * Test: Add an indirect-permissions to user
     * Assume
     * - User has permissions {P0}. In which, P0 is a direct-permission
     * Action
     * - Add permissions {P0} to user
     * Expected
     * User has permissions {P0}. In which, P0 is an indirect-permission
     */
    @Test
    public void addIndirectPermission03() {
        user.getPermissions().clear();
        user.getPermissions().put("P0", new Permission(true, 1));
        user.addIndirectPermission("P0", 1);
        assertEquals(1, user.getPermissions().size());
        assertTrue(user.getPermissions().get("P0").isDirect());
        assertEquals(2, user.getPermissions().get("P0").getCount());
    }

    /**
     * Test: Add an indirect-permissions to user
     * Assume
     * - User has permissions {P0}. In which, P0 is an indirect-permission
     * Action
     * - Add permissions {P0} to user
     * Expected
     * User has permissions {P0}. In which, P0 is an indirect-permission
     */
    @Test
    public void addIndirectPermission04() {
        user.getPermissions().clear();
        user.getPermissions().put("P0", new Permission(false, 1));
        user.addIndirectPermission("P0", 1);
        assertEquals(1, user.getPermissions().size());
        assertFalse(user.getPermissions().get("P0").isDirect());
        assertEquals(2, user.getPermissions().get("P0").getCount());
    }

    /**
     * Test: Add indirect-permissions to user
     * Assume
     * - User has permissions {}
     * Action
     * - Add permissions {P0, P1} to user
     * Expected
     * User has permissions {P0, P1}. In which, P0 and P1 are indirect-permissions
     */
    @Test
    public void addIndirectPermissions01() {
        user.getPermissions().clear();
        user.addIndirectPermissions(new HashMap<>(){{
            put("P0", new Permission(true, 1));
            put("P1", new Permission(true, 1));
        }});
        assertEquals(2, user.getPermissions().size());
        assertFalse(user.getPermissions().get("P0").isDirect());
        assertFalse(user.getPermissions().get("P1").isDirect());
        assertEquals(1, user.getPermissions().get("P0").getCount());
        assertEquals(1, user.getPermissions().get("P1").getCount());
    }

    /**
     * Test: Add indirect-permissions to user
     * Assume
     * - User has permissions {P2}
     * Action
     * - Add permissions {P0, P1} to user
     * Expected
     * User has permissions {P0, P1}. In which, P0 and P1 are indirect-permissions
     */
    @Test
    public void addIndirectPermissions02() {
        user.getPermissions().clear();
        user.getPermissions().put("P2", new Permission(true, 1));
        user.addIndirectPermissions(new HashMap<>(){{
            put("P0", new Permission(true, 1));
            put("P1", new Permission(true, 1));
        }});
        assertEquals(3, user.getPermissions().size());
        assertFalse(user.getPermissions().get("P0").isDirect());
        assertFalse(user.getPermissions().get("P1").isDirect());
        assertEquals(1, user.getPermissions().get("P0").getCount());
        assertEquals(1, user.getPermissions().get("P1").getCount());
    }

    /**
     * Test: Add indirect-permissions to user
     * Assume
     * - User has permissions {P0}. In which, P0 is direct-permission
     * Action
     * - Add permissions {P0, P1} to user
     * Expected
     * User has permissions {P0, P1}. In which, P0 and P1 are indirect-permissions
     */
    @Test
    public void addIndirectPermissions03() {
        user.getPermissions().clear();
        user.getPermissions().put("P0", new Permission(true, 1));
        user.addIndirectPermissions(new HashMap<>(){{
            put("P0", new Permission(true, 1));
            put("P1", new Permission(true, 1));
        }});
        assertEquals(2, user.getPermissions().size());
        assertTrue(user.getPermissions().get("P0").isDirect());
        assertFalse(user.getPermissions().get("P1").isDirect());
        assertEquals(2, user.getPermissions().get("P0").getCount());
        assertEquals(1, user.getPermissions().get("P1").getCount());
    }

    /**
     * Test: Add indirect-permissions to user
     * Assume
     * - User has permissions {P0}. In which, P0 is indirect-permission
     * Action
     * - Add permissions {P0, P1} to user
     * Expected
     * User has permissions {P0, P1}. In which, P0 and P1 are indirect-permissions
     */
    @Test
    public void addIndirectPermissions04() {
        user.getPermissions().clear();
        user.getPermissions().put("P0", new Permission(false, 1));
        user.addIndirectPermissions(new HashMap<>(){{
            put("P0", new Permission(true, 1));
            put("P1", new Permission(true, 1));
        }});
        assertEquals(2, user.getPermissions().size());
        assertFalse(user.getPermissions().get("P0").isDirect());
        assertFalse(user.getPermissions().get("P1").isDirect());
        assertEquals(2, user.getPermissions().get("P0").getCount());
        assertEquals(1, user.getPermissions().get("P1").getCount());
    }

    /**
     * Test: remove a direct-permission to user
     * Assume
     * - User has permissions {P0, P1}. In which, P0 and P1 are direct-permissions
     * Action
     * - Remove permission {P0} from user
     * Expected
     * User has permissions {P1}.
     */
    @Test
    public void removeDirectPermissions01() {
        user.getPermissions().clear();
        user.getPermissions().put("P0", new Permission(true, 1));
        user.getPermissions().put("P1", new Permission(true, 1));
        user.removeDirectPermissions(Set.of("P0"));
        assertEquals(Set.of("P1"), user.getPermissions().keySet());
    }

    /**
     * Test: remove a direct-permission to user
     * Assume
     * - User has permissions {P0, P1}. In which, P0 is indirect-permission
     * Action
     * - Remove permission {P0} from user
     * Expected
     * User has permissions {P1}.
     */
    @Test
    public void removeDirectPermission02() {
        user.getPermissions().clear();
        user.getPermissions().put("P0", new Permission(false, 1));
        user.getPermissions().put("P1", new Permission(true, 1));
        user.removeDirectPermissions(Set.of("P0"));
        assertEquals(Set.of("P0", "P1"), user.getPermissions().keySet());
    }

    /**
     * Test: remove an indirect-permission to user
     * Assume
     * - User has permissions {P0, P1}. In which, P0 is indirect-permission
     * Action
     * - Remove permission {P0} from user
     * Expected
     * User has permissions {P1}.
     */
    @Test
    public void removeIndirectPermissions01() {

    }

    @Test
    public void removeIndirectPermissions02() {

    }
}