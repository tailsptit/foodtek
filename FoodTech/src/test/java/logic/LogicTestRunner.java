package test.java.logic;

import main.java.sol.Company;
import main.java.sol.User;
import main.java.writer.DataWriter;
import org.junit.*;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class LogicTestRunner {
    private static Company company;

    @BeforeClass
    public static void init() {
        company = new Company();
        company.setWriter(new DataWriter(new BufferedWriter(new OutputStreamWriter(System.out))));
        company.setOperationMode("PRODUCT");
        System.out.println("init");
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("tearDown");
    }

    @Before
    public void setup() {
        System.out.println("Setup for each testcase");
    }

    /**
     * Test: Company has 10 users (CEO + 9 staffs)
     * Assume
     * - CEO has permissions {P1, P2} and manages user 8 and user 9
     * - User 1 has permissions {P3, P5, P10} and does not manages any user
     * - User 2 has permissions {P2} and manages user 3 and user 4
     * - User 3 has permissions {P10} and does not manages any user
     * - User 4 has permissions {P2, P4} and does not manages any user
     * - User 5 has permissions {P5, P6}  manages user 6 and user 7
     * - User 6 has permissions {P3} and does not manages any user
     * - User 7 has permissions {P4, P8, P9} and does not manages any user
     * - User 8 has permissions {P3} and manages user 5
     * - User 9 has permissions {P10} and manages user 1 and user 2
     * Action
     * - ADD 3 P7
     * - REMOVE 2 P2
     * - REMOVE 1 P3 P10
     * Expected
     * CEO has permissions {P0, P1, P2, P3, X}. In which, P0 is a direct-permission; P1, P2, P3, X are indirect-permissions
     * User 1 has permissions {P1, P3, X}. In which, P1 is a direct permission, P3, X are indirect-permissions
     * User 2 has permissions {P2}. In which, P2 is a direct permission
     * User 3 has permissions {P3, X}. In which, P2 is a direct-permission, X is an indirect-permission
     */
    @Test
    public void testCompany01(){
        User[] users = new User[10];
        users[0] = setUser(-1, new String[]{"P1", "P2"}, Set.of(8, 9));
        users[1] = setUser(9, new String[]{"P3", "P5", "P10"}, Set.of());
        users[2] = setUser(9, new String[]{"P2"}, Set.of(4, 3));
        users[3] = setUser(2, new String[]{"P10"}, Set.of());
        users[4] = setUser(2, new String[]{"P2", "P4"}, Set.of());
        users[5] = setUser(8, new String[]{"P5", "P6"}, Set.of(6, 7));
        users[6] = setUser(5, new String[]{"P3"}, Set.of());
        users[7] = setUser(5, new String[]{"P4", "P8", "P9"}, Set.of());
        users[8] = setUser(0, new String[]{"P3"}, Set.of(5));
        users[9] = setUser(0, new String[]{"P10"}, Set.of(1, 2));
        company.setUsers(users);
        company.exportDebug("Print initial data for testCompany01");
        company.addFullPermissionsForEachUserUsingDFS(0);
        company.exportDebug("addFullPermissionsForEachUserUsingDFS for testCompany01");
        assertEquals(Set.of("P1", "P2", "P10", "P3", "P5", "P4", "P6", "P8", "P9"), users[0].getPermissions().keySet());
        assertEquals(Set.of("P3", "P5", "P10"), users[1].getPermissions().keySet());
        assertEquals(Set.of("P2", "P4", "P10"), users[2].getPermissions().keySet());
        assertEquals(Set.of("P10"), users[3].getPermissions().keySet());
        assertEquals(Set.of("P2", "P4"), users[4].getPermissions().keySet());
        assertEquals(Set.of("P5", "P6", "P3", "P4", "P8", "P9"), users[5].getPermissions().keySet());
        assertEquals(Set.of("P3"), users[6].getPermissions().keySet());
        assertEquals(Set.of("P4", "P8", "P9"), users[7].getPermissions().keySet());
        assertEquals(Set.of("P5", "P6", "P3", "P4", "P8", "P9"), users[8].getPermissions().keySet());
        assertEquals(Set.of("P10", "P3", "P5", "P2", "P4"), users[9].getPermissions().keySet());
        users[7].addDirectPermissions(Set.of("P7"));
        company.addIndirectPermissionsToManagers(users[7].getManager(), Set.of("P7"));
        assertEquals(Set.of("P7", "P4", "P8", "P9"), users[7].getPermissions().keySet());
        company.exportDebug("Query");
        assertEquals(Set.of("P5", "P6", "P3", "P4", "P8", "P9", "P7"), users[5].getPermissions().keySet());
        assertEquals(Set.of("P5", "P6", "P3", "P4", "P8", "P9", "P7"), users[8].getPermissions().keySet());
        assertEquals(Set.of("P1", "P2", "P10", "P3", "P5", "P4", "P6", "P8", "P9", "P7"), users[0].getPermissions().keySet());
        users[2].removeDirectPermissions(Set.of("P2"));
        company.addIndirectPermissionsToManagers(users[2].getManager(), Set.of("P2"));
        users[1].removeDirectPermissions(Set.of("P3", "P10"));
        company.addIndirectPermissionsToManagers(users[1].getManager(), Set.of("P3", "P10"));
        assertEquals(Set.of("P2", "P4", "P10"), users[2].getPermissions().keySet());
        assertEquals(Set.of("P5"), users[1].getPermissions().keySet());
    }

    public User setUser(int manager, String[] permissions, Set<Integer> staffs){
        User user = new User(permissions);
        user.setManager(manager);
        for (int staff: staffs) {
            user.addDirectStaff(staff);
        }
        return user;
    }
}

