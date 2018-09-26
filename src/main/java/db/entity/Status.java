package db.entity;


/**
 * Status entity
 */
public class Status {
    private int id;
    private boolean state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", state='" + state + '\'' +
                '}';
    }
}
