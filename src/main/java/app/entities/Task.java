package app.entities;

public class Task {

    private int id;
    private String name;
    private boolean done = false;


    public Task(int id, String title, boolean done) {
        this.id = id;
        this.name = title;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isDone() {
        return done;
    }
}
