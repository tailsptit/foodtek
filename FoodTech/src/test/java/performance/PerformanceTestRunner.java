package test.java.performance;

import main.java.sol.Company;
import main.java.writer.DataWriter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class PerformanceTestRunner {
    static final private int K = 100;
    private static List<String> permissions = new ArrayList<>();
    private Random randomNumberOfPermissions = new Random(K);
    private Random randomPermissions = new Random(K);
    static Company ft;
    private BufferedWriter writer;

    @BeforeClass
    public static void init() {
        ft = new Company();
        ft.setOperationMode("PRODUCT"); // "PRODUCT" or "DEBUG"
        System.out.println("init");
        for (int i = 0; i < K; i++) {
            permissions.add("P" + i);
        }
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("tearDown");
    }

//    @Before
//    public void setup() {
//        System.out.println("Setup for each testcase");
//    }

    @Test
    public void testUserOfUser10() throws Exception {
        int numberOfUsers = 10;
        testUserOfUser(numberOfUsers, "./data/generate_data_" + numberOfUsers + ".txt");
    }

    @Test
    public void testUserOfUser100() throws Exception {
        int numberOfUsers = 100;
        testUserOfUser(numberOfUsers, "./data/generate_data_" + numberOfUsers + ".txt");
    }

    @Test
    public void testUserOfUser1000() throws Exception {
        int numberOfUsers = 1000;
        testUserOfUser(numberOfUsers, "./data/generate_data_" + numberOfUsers + ".txt");
    }

    @Test
    public void testUserOfUser2000() throws Exception {
        int numberOfUsers = 2000;
        testUserOfUser(numberOfUsers, "./data/generate_data_" + numberOfUsers + ".txt");
    }

    @Test
    public void testUserOfUser5000() throws Exception {
        int numberOfUsers = 5000;
        testUserOfUser(numberOfUsers, "./data/generate_data_" + numberOfUsers + ".txt");
    }
    @Test
    public void testUserOfUser10000() throws Exception {
        int numberOfUsers = 10000;
        testUserOfUser(numberOfUsers, "./data/generate_data_" + numberOfUsers + ".txt");
    }

    @Test
    public void testUserOfUser50000() throws Exception {
        int numberOfUsers = 50000;
        testUserOfUser(numberOfUsers, "./data/generate_data_" + numberOfUsers + ".txt");
    }

    @Test
    public void testUserOfUser100000() throws Exception {
        int numberOfUsers = 100000;
        String filename = "./data/generate_data_" + numberOfUsers + ".txt";
        testUserOfUser(numberOfUsers, filename);
    }

    public void makeData(String filename, int numberOfUsers) throws Exception {
        Random randomOfChild = new Random();
        writer = new BufferedWriter(new FileWriter(filename));
        writer.write("" + numberOfUsers + "\n");
        for (int userId = 0; userId <= numberOfUsers; userId++) {
            int numberOfPermissionsForUser = randomNumberOfPermissions.nextInt(K);
            for (int i = 0; i < numberOfPermissionsForUser; i++) {
                writer.write(permissions.get(randomPermissions.nextInt(K)) + " ");
            }
            writer.write(permissions.get(randomPermissions.nextInt(K)) + "\n");
        }
        Vector<Integer> users = new Vector<>();
        users.add(0);
        for (int id = 1; id <= numberOfUsers; id++) {
            int r = randomOfChild.nextInt(users.size());
            int father = users.get(r);
            if (father == 0) {
                writer.write("CEO\n");
            } else {
                writer.write("" + father + "\n");
            }
            users.add(id);
        }
        writer.close();
    }

    public void testUserOfUser(int numberOfUsers, String filename) throws Exception {
        makeData(filename, numberOfUsers);
        ft.setReader(new BufferedReader(new BufferedReader(new FileReader(new File(filename)))));
        ft.setWriter(new DataWriter(new BufferedWriter(new FileWriter(new File(filename.substring(0, filename.length() - 4) + "_out" + ".txt")))));
        long start = System.currentTimeMillis();
        ft.execute();
        long end = System.currentTimeMillis();
        System.out.println("Test file: " + filename + "\n\tNumber of users: " + numberOfUsers + "\n\tExecution time: " + (end - start) + " (milliseconds)\n");
    }
}
