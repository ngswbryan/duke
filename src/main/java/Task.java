/** 
* Parent task class for all events, deadlines and todos
*/ 

public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "\u2713" : "\u2718"); //return tick or X symbols 
    }


    public void markAsDone() {
        this.isDone = true;
    }

    public abstract String getStatus();

    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }
}