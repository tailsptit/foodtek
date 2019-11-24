package test.java.unit;//package test.java.unit;

import main.java.sol.Company;
import main.java.sol.User;
import main.java.writer.DataWriter;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CompanyTestRunner {
    private static Company company;

    @BeforeClass
    public static void init() {
        System.out.println("\ninit");
        company = new Company();
        company.setOperationMode("PRODUCT"); // "PRODUCT" or "DEBUG"
        company.setWriter(new DataWriter(new BufferedWriter(new OutputStreamWriter(System.out))));
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("\ntearDown");
    }

    @Before
    public void setup() {
        System.out.println("\nSetup for each testcase");
    }

    /**
     * Test: Add a permission to user
     * Assume
     * - CEO has permissions {P0} and manages user 1 and user 2
     * - User 1 has permissions {P1} and manages user 3
     * - User 2 has permissions {P2}
     * - User 3 has permissions {P3}
     * Action
     * - Add permission X to user 3
     * Expected
     * CEO has permissions {P0, P1, P2, P3, X}. In which, P0 is a direct-permission; P1, P2, P3, X are indirect-permissions
     * User 1 has permissions {P1, P3, X}. In which, P1 is a direct permission, P3, X are indirect-permissions
     * User 2 has permissions {P2}. In which, P2 is a direct permission
     * User 3 has permissions {P3, X}. In which, P2 is a direct-permission, X is an indirect-permission
     */
    @Test
    public void addIndirectPermissionsToManagers() {
        User[] users = new User[4];
        users[0] = setUser(-1, new String[]{"P0"}, Set.of(1, 2));
        users[1] = setUser(0, new String[]{"P1"}, Set.of(3));
        users[2] = setUser(0, new String[]{"P2"}, Set.of());
        users[3] = setUser(1, new String[]{"P3"}, Set.of());

        company.setUsers(users);
        company.addFullPermissionsForEachUserUsingDFS(0);
        company.getUsers()[3].addDirectPermissions(Set.of("X"));
        company.addIndirectPermissionsToManagers(company.getUsers()[3].getManager(), Set.of("X"));

        assertEquals(Set.of("P0", "P1", "P2", "P3", "X"), company.getUsers()[0].getPermissions().keySet());
        assertTrue(company.getUsers()[0].getPermissions().get("P0").isDirect());
        assertFalse(company.getUsers()[0].getPermissions().get("P1").isDirect());
        assertFalse(company.getUsers()[0].getPermissions().get("P2").isDirect());
        assertFalse(company.getUsers()[0].getPermissions().get("P3").isDirect());
        assertFalse(company.getUsers()[0].getPermissions().get("X").isDirect());

        assertEquals(Set.of("P1", "P3", "X"), company.getUsers()[1].getPermissions().keySet());
        assertTrue(company.getUsers()[1].getPermissions().get("P1").isDirect());
        assertFalse(company.getUsers()[1].getPermissions().get("P3").isDirect());
        assertFalse(company.getUsers()[1].getPermissions().get("X").isDirect());

        assertEquals(Set.of("P2"), company.getUsers()[2].getPermissions().keySet());
        assertTrue(company.getUsers()[2].getPermissions().get("P2").isDirect());

        assertEquals(Set.of("P3", "X"), company.getUsers()[3].getPermissions().keySet());
        assertTrue(company.getUsers()[3].getPermissions().get("P3").isDirect());
        assertTrue(company.getUsers()[3].getPermissions().get("X").isDirect());
    }

    @Test
    public void removeIndirectPermissionFromManagers() {
        User[] users = new User[4];
        users[0] = setUser(-1, new String[]{"CEO", "P0"}, Set.of(1, 2));
        users[1] = setUser(0, new String[]{"P1"}, Set.of(3));
        users[2] = setUser(0, new String[]{"P2"}, Set.of());
        users[3] = setUser(1, new String[]{"P3"}, Set.of());

//
//        users[1] = new User(new String[]{"P1"});
//        users[1].setManager(0);
//        users[2] = new User(new String[]{"P2"});
//        users[2].setManager(0);
//        users[3] = new User(new String[]{"P3"});
//        users[3].setManager(1);
        company.setUsers(users);
        company.addIndirectPermissionsToManagers(3, Set.of("P0"));
        assertEquals(Set.of("P0", "P3"), company.getUsers()[3].getPermissions().keySet());
        assertTrue(users[3].getPermissions().get("P3").isDirect());
        assertEquals(Set.of("P2"), company.getUsers()[2].getPermissions().keySet());
        assertEquals(Set.of("P0", "P1"), company.getUsers()[1].getPermissions().keySet());
        assertEquals(Set.of("P0", "CEO"), company.getUsers()[0].getPermissions().keySet());
        assertFalse(users[3].getPermissions().get("P0").isDirect());
        assertFalse(users[1].getPermissions().get("P0").isDirect());
        assertTrue(users[0].getPermissions().get("P0").isDirect());
        assertEquals(2, company.getUsers()[0].getPermissions().get("P0").getCount());
    }

    @Test
    public void addFullPermissionsForEachUserUsingDFS() {
    }

    @Test
    public void execute() {
    }

    private User setUser(int manager, String[] permissions, Set<Integer> staffs){
        User user = new User(permissions);
        user.setManager(manager);
        for (int staff: staffs) {
            user.addDirectStaff(staff);
        }
        return user;
    }
}