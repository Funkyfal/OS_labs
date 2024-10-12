import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.Scanner;

public class CoolThread extends Thread {
    FirstThread fThread;
    SolveThread sThread;
    Queue<Test> Results;
    Queue<Test> ValidatedResults;

    public CoolThread(FirstThread fThread, SolveThread sThread, Queue<Test> Results, Queue<Test> ValidatedResults)
    {
        this.fThread = fThread;
        this.sThread = sThread;
        this.Results = Results;
        this.ValidatedResults = ValidatedResults;
    }
    public void run()
    {
        while(fThread.isAlive() || sThread.isAlive() || !Results.isEmpty())
        {
            if(!Results.isEmpty())
            {
                Test notValidated = Results.remove();
                try (Scanner in = new Scanner(new File(fThread.directoryPath + "\\" + notValidated.Name.substring(0, notValidated.Name.length() - 2) + "out"))) {
                    try (Scanner in2 = new Scanner(new File(fThread.directoryPath + "\\"
                            + notValidated.Name.substring(0, notValidated.Name.length() - 2) + "myout"))) {
                        while(in.hasNextLine() && in2.hasNextLine())
                        {
                            String check1 = in.nextLine();
                            String check2 = in2.nextLine();
                            if(!check1.equals(check2) || (in.hasNextLine() && !in2.hasNextLine()) || (!in.hasNextLine() && in2.hasNextLine()))
                                notValidated.State = Test.status.Wrong;
                        }
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                if(notValidated.State != Test.status.Wrong && notValidated.State != Test.status.Corrupted)
                    notValidated.State = Test.status.Completed;
                ValidatedResults.add(notValidated);
            }
        }

    }
}
