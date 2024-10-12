import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;

public class SolveThread extends Thread {
    FirstThread thread;
    Queue<Test> Tests;
    Queue<Test> Results;
    boolean running;

    public SolveThread(FirstThread thread, Queue<Test> Tests, Queue<Test> Results) {
        this.Tests = Tests;
        this.Results = Results;
        this.thread = thread;
        running = true;
    }

    public void run() {
        while (running) {
            if (!Tests.isEmpty()) {
                Test temp = Tests.remove();
                String FilePath = temp.Name.substring(0, temp.Name.length() - 2);
                Writer writer;
                try {
                    writer = new FileWriter(thread.directoryPath + "\\" + FilePath + "out", false);
                    for (HashSet<Pair<Integer>> tempSet : temp.TaskQueue) {
                        if (isBox(tempSet))
                            writer.write("YES\n");
                        else
                            writer.write("NO\n");
                        writer.flush();
                        temp.State = Test.status.Validating;
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Results.add(temp);
            }
            if (!thread.isAlive() && Tests.isEmpty())
                running = false;
        }
    }

    public static boolean isBox(HashSet<Pair<Integer>> pair_set) {
        if (pair_set.size() == 1) {
            Iterator<Pair<Integer>> i = pair_set.iterator();
            Pair<Integer> temp1 = i.next();
            return temp1.first == temp1.second;
        } else if (pair_set.size() == 2) {
            Iterator<Pair<Integer>> i = pair_set.iterator();
            Pair<Integer> temp1 = i.next(), temp2 = i.next();
            return temp1.hasCommon(temp2);
        } else if (pair_set.size() == 3) {
            Iterator<Pair<Integer>> i = pair_set.iterator();
            Pair<Integer> temp1 = i.next(), temp2 = i.next(), temp3 = i.next();
            return temp1.hasCommon(temp2) && temp1.hasCommon(temp3) && temp2.hasCommon(temp3);
        }
        return false;
    }
}
