import models.Rectangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RectangleReader extends Thread {
    String directoryName;
    Queue<Tasks> rects;


    public RectangleReader(String directoryName, Queue<Tasks> rects) {
        this.directoryName = directoryName;
        this.rects = rects;
    }

    @Override
    public void run() {
            File Directory = new File(directoryName);
            if (Directory.isDirectory()) {
                File[] allFiles = Directory.listFiles();
                if (allFiles != null) {
                    for (File temp : allFiles) {
                        if (temp.getName().charAt(temp.getName().length() - 1) == 'n'
                                && temp.getName().charAt(temp.getName().length() - 2) == 'i'
                                && temp.getName().charAt(temp.getName().length() - 3) == '.') {
                            Scanner scanner = null;
                            try {
                                scanner = new Scanner(new File(directoryName + "\\" + temp.getName()));
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            int k = scanner.nextInt();
                            scanner.nextLine();

                            Queue<Vector<Integer>> tempD = new ArrayDeque<>();
                            for (int i = 0; i < k; i++) {
                                Vector<Integer> rectangle = new Vector<>();

                                String[] line = scanner.nextLine().split(" ");
                                for (String numStr : line) {
                                    if (!numStr.isEmpty()) {
                                        rectangle.add(Integer.parseInt(numStr));
                                    }
                                }
                                tempD.add(rectangle);
                            }
                            rects.add(new Tasks(directoryName + "\\" + temp.getName(), TaskStatus.NOT_STARTED, tempD));
                            scanner.close();
                        }
                    }
                } else System.out.println("СДЕЛАТЬ ЭКСЕПШН");
            }
        }

}
