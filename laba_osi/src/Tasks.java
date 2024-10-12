import java.util.Queue;
import java.util.Vector;

enum TaskStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}

public class Tasks {
    Queue<Vector<Integer>> allTasks;

    String file_name;

    private  TaskStatus status;

    Tasks(String file_name, TaskStatus status, Queue<Vector<Integer>> allTasks)
    {
        this.file_name=file_name;
        this.status=status;
        this.allTasks = allTasks;
    }

    public TaskStatus getStatus() {
        return status;
    }
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
