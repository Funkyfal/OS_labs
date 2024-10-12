import javax.swing.*;
import java.util.HashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;
import java.util.Queue;
import javax.swing.*;

public class Main {
    public static Queue<HashSet<Pair<Integer>>> Tasks;

    public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                new MainFrame().setVisible(true);
            });
        Tasks = new LinkedList<>();

        String directoryPath = "F:\\Anton\\java labs\\RGR\\src";
        File directory = new File(directoryPath);
        boolean check = false;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files == null)
                System.out.println("todo : ВЫДАТЬ ЭКСЕПШН НА ВСЕ ЕБАЛО");
            else
                check = true;
        } else
            System.out.println("todo : ВЫДАТЬ ЭКСЕПШН НА ВСЕ ЕБАЛО");
        File[] files = directory.listFiles();
        FirstThread thread = new FirstThread(Tasks, files, directoryPath);
        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while(!Tasks.isEmpty())
        {
            HashSet<Pair<Integer>> temp = Tasks.remove();
            System.out.println(temp);
        }
        //самый главный todo:Разобраться с ебучим потоком
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

    /*public static void GetInf(String filePath, Queue<HashSet<Pair<Integer>>> Tasks)
    {
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
}*/