import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Queue;
import java.util.Scanner;

public class FirstThread extends Thread {
    Queue<Test> Tests;
    File[] files;
    public String directoryPath;
    public FirstThread(Queue<Test> Tests_, File[] files_, String directoryPath_)
    {
        directoryPath = directoryPath_;
        Tests = Tests_;
        files = files_;
    }
    public void run()
    {
        for(File file : files)
        {
            if (file.getName().charAt(file.getName().length() - 1) == 'n' && file.getName().charAt(file.getName().length() - 2) == 'i')
            {
                Test Temp = new Test();
                Temp.Name = file.getName();
                String filePath = directoryPath + "\\" + file.getName();
                try (Scanner in = new Scanner(new File(filePath)))
                {
                    if (in.hasNextInt())
                    {
                        String str = in.nextLine();
                        int n = Integer.parseInt(str);
                        if (n > 5)
                            Temp.State = Test.status.Corrupted;

                        for (int i = 0; i < n; i++)
                        {
                            if (in.hasNextLine())
                            {
                                String Line = in.nextLine();
                                String[] Numbers = Line.split("\\s+");
                                if(Numbers.length != 12)
                                    Temp.State = Test.status.Corrupted;

                                HashSet<Pair<Integer>> pair_set = new HashSet<>();
                                if(Temp.State != Test.status.Corrupted)
                                {
                                    for (int j = 0; j < Numbers.length - 1; j += 2)
                                    {
                                        int temp = Integer.parseInt(Numbers[j]);
                                        int temp2 = Integer.parseInt(Numbers[j + 1]);
                                        if(temp <= 0 || temp > 1000 || temp2 <= 0 || temp2 > 1000)
                                        {
                                            Temp.State = Test.status.Corrupted;
                                            break;
                                        }
                                        if (temp2 < temp)
                                            pair_set.add(new Pair<>(temp2, temp));
                                        else
                                            pair_set.add(new Pair<>(temp, temp2));
                                    }
                                    if(Temp.State != Test.status.Corrupted)
                                        Temp.TaskQueue.add(pair_set);
                                }
                            }
                            else
                                Temp.State = Test.status.Corrupted;
                        }
                    }
                    else
                        Temp.State = Test.status.Corrupted;
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                Tests.add(Temp);
            }
        }
    }
}