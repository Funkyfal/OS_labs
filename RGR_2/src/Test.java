import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Test {
    public Test() {
        Name = "";
        State = status.wasNotChecked;
        TaskQueue = new LinkedList<>();
    }

    public enum status{Corrupted, Completed, wasNotChecked, Wrong, Validating};
    public String Name;
    public status State;
    Queue<HashSet<Pair<Integer>>> TaskQueue;

    public Test(String Name, status State)
    {
        this.Name = Name;
        this.State = State;
        TaskQueue = new LinkedList<>();
    }
}
