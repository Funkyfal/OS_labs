import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Queue;
import java.util.Scanner;

public class FirstThread extends Thread {
    public Queue<HashSet<Pair<Integer>>> Tasks;
    public File[] files;
    public String directoryPath;
    public FirstThread(Queue<HashSet<Pair<Integer>>> Tasks_, File[] files_, String directoryPath_)
    {
        directoryPath = directoryPath_;
        Tasks = Tasks_;
        files = files_;
    }
    public void run()
    {
        for(File file : files) {
            if (file.getName().charAt(file.getName().length() - 1) == 'n' && file.getName().charAt(file.getName().length() - 2) == 'i') {
                String filePath = directoryPath + "\\" + file.getName();
                try (Scanner in = new Scanner(new File(filePath))) {
                    if (in.hasNextInt()) {
                        String str = in.nextLine();
                        int n = Integer.parseInt(str);

                        for (int i = 0; i < n; i++) {
                            if (in.hasNextLine()) {
                                String Line = in.nextLine();
                                String[] Numbers = Line.split("\\s+");
                                for (String s : Numbers)
                                    for (int j = 0; j < s.length(); j++)
                                        if (!Character.isDigit(s.charAt(i)))
                                            System.out.println("todo : Закинуть в коррапт");
                                HashSet<Pair<Integer>> pair_set = new HashSet<>();
                                for (int j = 0; j < Numbers.length - 1; j += 2) {
                                    int temp = Integer.parseInt(Numbers[j]);
                                    int temp2 = Integer.parseInt(Numbers[j + 1]);
                                    if (temp2 < temp)
                                        pair_set.add(new Pair<>(temp2, temp));
                                    else
                                        pair_set.add(new Pair<>(temp, temp2));
                                }
                                Tasks.add(pair_set);
                            } else
                                System.out.println("todo : Закинуть в коррапт");
                        }
                    } else
                        System.out.println("todo : Закинуть в коррапт");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}