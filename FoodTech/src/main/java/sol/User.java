package main.java.sol;

import java.util.*;

public class User {
    private int manager;                                // manager of this user, manager of CEO is -1
    private Set<Integer> directStaffs;                  // all of staffs that this user manages directly
    private Map<String, Permission> permissions;        // Contains all permissions of user, including direct-permissions and indirect-permissions
                                                        // direct-permission is permission that user managed directly
                                                        // indirect-permission is permission that user manages indirectly
    /**
     * Constructor for User class, assign permissions to user
     *
     * @param permissions List of initial permissions of user
     */
    public User(String[] permissions) {
        this.manager = -1;
        this.directStaffs = new HashSet<>();
        this.permissions = new HashMap<>();
        for (String p : permissions) {
            this.permissions.put(p, new Permission(true, 1));
        }
    }

    /**
     * Set manager for user
     *
     * @param manager will be managed by user
     */
    public void setManager(int manager) {
        this.manager = manager;
    }

    /**
     * Get manager of user
     *
     * @return manager of user
     */
    public int getManager() {
        return manager;
    }

    /**
     * This user is manager if having at least one staff that user manages directly
     *
     * @return true if user manage at least one staff, otherwise false
     */
    public boolean isManager() {
        return !directStaffs.isEmpty();
    }

    /**
     * Get all staffs that user manages directly
     *
     * @return all staffs that user manages directly
     */
    public Set<Integer> getDirectStaffs() {
        return directStaffs;
    }

    /**
     * Add a staff to user. After this operation, user will directly manage this staff
     *
     * @param staff Staff will be managed by user
     */
    public void addDirectStaff(int staff) {
        directStaffs.add(staff);
    }

    /**
     * Get all permissions of user in Map format
     *
     * @return all staffs will be managed by user
     */
    public Map<String, Permission> getPermissions() {
        return permissions;
    }

    /**
     * Add direct-permissions to user
     * @param permissions Set of permissions added to user
     *
     * @return all staffs will be managed by user
     */
    public void addDirectPermissions(Set<String> permissions) {
        for (String permission : permissions) {
            if (this.permissions.containsKey(permission)) {
                Permission p = this.permissions.get(permission);
                if (!p.isDirect()) {
                    p.setDirect(true);
                    p.incCount();
                }
            } else {
                this.permissions.put(permission, new Permission(true, 1));
            }
        }
    }

    /**
     * Add an indirect-permission to user
     *
     * @param permission          Indirect-permission will be added to user
     * @param numberOfAppearances Number of indirect-permissions will be added to user
     */
    public void addIndirectPermission(String permission, int numberOfAppearances) {
        if (permissions.containsKey(permission))
            permissions.get(permission).incCount(numberOfAppearances);
        else
            permissions.put(permission, new Permission(false, numberOfAppearances));
    }

    /**
     * Add indirect-permissions to user
     *
     * @param permissions Indirect-permissions will be added to user
     */
    public void addIndirectPermissions(Map<String, Permission> permissions) {
        for (String permission : permissions.keySet()) {
            addIndirectPermission(permission, permissions.get(permission).getCount());
        }
    }

    /**
     * Remove direct-permissions from user
     *
     * @param permissions Direct-permission will be removed from user
     * @return true if this permission i a direct-permission of user, other false
     */
    public void removeDirectPermissions(Set<String> permissions) {
        for (String permission : permissions) {
            if (this.permissions.containsKey(permission)) {
                Permission p = this.permissions.get(permission);
                if (p.isDirect()) {
                    p.setDirect(false);
                    p.decCount();
                    if (p.getCount() <= 0) {
                        this.permissions.remove(permission);
                    }
                }
            }
        }
    }

    /**
     * Remove an indirect-permission from user
     *
     * @param permission Indirect-permission will be removed from user
     * @return true if this permission i an indirect-permission of user, other false
     */
    private boolean removeIndirectPermission(String permission) {
        if (permissions.containsKey(permission)) {
            Permission p = permissions.get(permission);
            if (p.isDirect()) {
                if (p.getCount() > 1) {
                    p.decCount();
                    return true;
                }
            } else {
                p.decCount();
                if (p.getCount() <= 0) {
                    permissions.remove(permission);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeIndirectPermission(Set<String> permissions) {
        boolean cont = false;
        for (String permission : permissions) {
            cont = cont || removeIndirectPermission(permission);
        }
        return cont;
    }
}
