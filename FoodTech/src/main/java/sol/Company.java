package main.java.sol;

import main.java.writer.DataWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Company {
    private String operationMode;     // running in "PRODUCT" or "DEBUG" mode
    private BufferedReader reader;  // Store buffer reader that using to read input data. Buffer reader may be terminal or file
    private DataWriter writer;      // Store buffer writer that using to write output data. Buffer writer may be terminal or file
    private User[] users;           // Store all users of company

    /**
     * Set buffer reader that using to read input data. Buffer reader may be terminal or file
     *
     * @param reader Buffer reader
     */
    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    /**
     * Set buffer reader that using to read input data. Buffer reader may be terminal or file
     *
     * @param reader Buffer reader
     */
    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Set buffer writer that using to write output data. Buffer writer may be terminal or file
     *
     * @param reader Buffer writer
     */
    public void setWriter(DataWriter writer) {
        this.writer = writer;
    }

    /**
     * Set users for company
     *
     * @param users List of users in company
     */
    public void setUsers(User[] users) {
        this.users = users;
    }

    /**
     * Get all of users of company
     *
     * @return all users in company
     */
    public User[] getUsers() {
        return users;
    }

    /**
     * Add an indirect-permissions to user and all user's managers
     *
     * @param userId     User will be added
     * @param permissions Indirect-permissions will be added to user
     */
    public void addIndirectPermissionsToManagers(int userId, Set<String> permissions) {
        if (userId != -1) {
            String permission;
            Iterator<String> iterator = permissions.iterator();
            while (iterator.hasNext()){
                permission = iterator.next();
                if (users[userId].getPermissions().containsKey(permission)) {
                    users[userId].getPermissions().get(permission).incCount();
                } else {
                    users[userId].getPermissions().put(permission, new Permission(false, 1));
                }
            }
            addIndirectPermissionsToManagers(users[userId].getManager(), permissions);
        }
    }

    /**
     * Remove indirect-permissions from user and all user's managers
     *
     * @param userId     User will be added
     * @param permissions Indirect-permissions will be removed to user
     */

    public void removeIndirectPermissionFromManagers(int userId, Set<String> permissions) {
        if ((userId != -1) && (users[userId].removeIndirectPermission(permissions))) {
            removeIndirectPermissionFromManagers(users[userId].getManager(), permissions);
        }
    }

    /**
     * Add all direct-permissions and all indirect permissions for all users using the Depth First Search algorithm (DFS).
     * After operation, each user has all direct permissions of self user and all permissions of user that he manages
     * By using DFS, beginning from CEO (highest-level), each staff (from lowest-level to highest-level) will be added more indirect-permissions that he manages
     *
     * @param userId User will be added more indirect-permissions that he manages
     */
    public void addFullPermissionsForEachUserUsingDFS(int userId) {
        for (Integer staffId : users[userId].getDirectStaffs()) {
            addFullPermissionsForEachUserUsingDFS(staffId);
            users[userId].addIndirectPermissions(users[staffId].getPermissions());
            exportDebug(staffId, "Process add full permissions for user " + staffId);
        }
//        exportDebug(userId, "Process add full permissions for user " + userId);
    }

    /**
     * 1. Read number of users, permissions from buffer and assign for each user
     * 2. Read and assign manager, staffs for each user
     * 3. Add full permissions (direct-permissions and indirect-permissions) for each user, using Depth First Search algorithm, beginning from CEO
     * 4. Write output result to writer buffer for Question 1
     * 5. Process and write output to writer buffer for Question 4
     *
     * @throws IOException Exception when read, write, close buffers
     */

    public void execute() throws IOException {
        // 1. Read number of users, permissions from buffer and assign for each user
        int numberOfUsers = Integer.valueOf(reader.readLine());
        users = new User[numberOfUsers + 1];
        for (int id = 0; id <= numberOfUsers; id++) {
            users[id] = new User(reader.readLine().trim().replaceAll("\\s+", ",").split(","));
        }
        exportDebug("1. Read number of users, permissions from buffer and assign for each user");

        // 2. Read and assign manager, staffs for each user
        for (int userId = 1; userId <= numberOfUsers; userId++) {
            String val = reader.readLine().trim().replaceAll("\\s+", ",");
            if (val.equals("CEO")) {
                users[userId].setManager(0);
                users[0].addDirectStaff(userId);
            } else {
                int manager = Integer.valueOf(val);
                users[userId].setManager(manager);
                users[manager].addDirectStaff(userId);
            }
        }
        exportDebug("2. Read and assign manager, staffs for each user");

        // 3. Add full permissions (direct-permissions and indirect-permissions) for each user, using Depth First Search algorithm, beginning from CEO
        addFullPermissionsForEachUserUsingDFS(0);
        exportDebug("3. Add full permissions (direct-permissions and indirect-permissions) for each user, using Depth First Search algorithm, beginning from CEO");

        // 4. Write output result to writer buffer for Question 1
        for (int i = 0; i < numberOfUsers; i++) {
            writer.write(users[i]);
            writer.write("\n");
        }
        writer.write(users[numberOfUsers]);

        // 5. Process and write output to writer buffer for Question 4
        String command;
        String[] params;
        Set<String> permissions;
        while ((command = reader.readLine()) != null) {
            int userId;
            params = command.trim().replaceAll("\\s+", ",").split(",");
            switch (params[0]) {
                case "ADD":
                    userId = Integer.valueOf(params[1]);
                    permissions = Arrays.stream(params, 2, params.length).collect(Collectors.toCollection(HashSet::new));
                    users[userId].addDirectPermissions(permissions);
                    addIndirectPermissionsToManagers(users[userId].getManager(), permissions);
                    exportDebug("Permission of users after execute command ADD " + params[1] + " " + params[2]);
                    break;
                case "QUERY":
                    writer.write("\n");
                    if (params[1].equals("CEO")) {
                        writer.write(users[0]);
                    } else
                        writer.write(users[Integer.valueOf(params[1])]);
                    exportDebug(params[1].equals("CEO") ? 0 : Integer.valueOf(params[1]), "Permission of users after execute command QUERY " + params[1]);
                    break;
                case "REMOVE":
                    userId = Integer.valueOf(params[1]);
                    permissions = Arrays.stream(params, 2, params.length).collect(Collectors.toCollection(HashSet::new));
                    users[userId].removeDirectPermissions(permissions);
                    removeIndirectPermissionFromManagers(users[userId].getManager(), permissions);
                    exportDebug("Permission of users after execute command REMOVE  " + params[1] + " " + params[2]);
                    break;
                default:
                    break;
            }
        }
        // Close buffer reader and buffer writer before terminating program
        reader.close();
        writer.close();
    }

    public void exportDebug(String string) {
        if (operationMode.equals("DEBUG")) {
            System.out.println("\n" + string);
            for (int userId = 0; userId < users.length; userId++) {
                System.out.println("user " + userId);
                System.out.println("\t- manager: " + users[userId].getManager());
                System.out.print("\t- staffs: [ ");
                for (Integer staff : users[userId].getDirectStaffs()) {
                    System.out.print(staff + " ");
                }
                System.out.println("]");
                System.out.print("\t- permissions: [ ");
                for (Map.Entry<String, Permission> permission : users[userId].getPermissions().entrySet()) {
                    System.out.print("{" + permission.getKey() + "," + permission.getValue().isDirect() + "," + permission.getValue().getCount() + "} ");
                }
                System.out.println("]");
            }
        }
    }

    public void exportDebug(int userId, String string) {
        if (operationMode.equals("DEBUG")) {
            System.out.println("\n" + string);
            System.out.println("user " + userId);
            System.out.println("\t- manager: " + users[userId].getManager());
            System.out.print("\t- staffs: [ ");
            for (Integer staff : users[userId].getDirectStaffs()) {
                System.out.print(staff + " ");
            }
            System.out.println("]");
            System.out.print("\t- permissions: [ ");
            for (Map.Entry<String, Permission> permission : users[userId].getPermissions().entrySet()) {
                System.out.print("{" + permission.getKey() + "," + permission.getValue().isDirect() + "," + permission.getValue().getCount() + "} ");
            }
            System.out.println("]");
        }
    }
}
