package main.java.sol;

public class Permission {
    private boolean direct; // true if this is a direct-permission. Otherwise, false
    private int count;      // number of appearances of this permission. That mean, number of direct-permissions + number of indirect-permissions

    public Permission(boolean direct, int count) {
        this.direct = direct;
        this.count = count;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public boolean isDirect() {
        return direct;
    }

    public int getCount() {
        return count;
    }

    public void incCount() {
        count = count + 1;
    }

    public void incCount(int delta) {
        count = count + delta;
    }

    public void decCount() {
        count = count - 1;
    }
}
