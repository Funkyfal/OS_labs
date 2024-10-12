import models.Rectangle;
import org.w3c.dom.css.Rect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ParallelepipedSolver extends Thread {
    Queue<Tasks> rects;
    Queue<Tasks> second_rects;
    RectangleReader reader;
    boolean working;
    public ParallelepipedSolver(Queue<Tasks> rects, Queue<Tasks> second_rects,
                                RectangleReader reader) {
        this.rects = rects;
        this.second_rects = second_rects;
        this.reader = reader;
        working = false;
    }

    public static Boolean Solve(Vector<Integer> testCase)
    {
        //if >< 12
        List<Rectangle> rects = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        for (int i = 0; i < 12; i += 2) {
            Rectangle rectangle = new Rectangle(testCase.elementAt(i), testCase.elementAt(i + 1));
            if (i == 0){
                rects.add(rectangle);
                counts.add(1);
                continue;
            }
            for (int j = 0; j < rects.size(); j++)
            {
                if (rectangle.equals(rects.get(j))) {
                    counts.set(j, counts.get(j) + 1);
                    break;
                }
                if (j == rects.size() - 1) {
                    rects.add(rectangle);
                    counts.add(1);
                    break;
                }
            }
        }

        //6
        if (rects.size() == 1 && counts.get(0) == 6 && rects.get(0).getWidth() == rects.get(0).getHeight())
            return true;

        //4|2
        if ((rects.size() == 2 && counts.get(0) * counts.get(1) == 8) &&
                (
                        counts.get(0) == 2 &&
                                rects.get(0).getHeight() == rects.get(0).getWidth() &&
                                (rects.get(1).getHeight() == rects.get(0).getWidth() ||
                                        rects.get(1).getWidth() == rects.get(0).getWidth()) ||

                                counts.get(1) == 2 &&
                                        rects.get(1).getHeight() == rects.get(1).getWidth() &&
                                        (rects.get(0).getHeight() == rects.get(1).getWidth() ||
                                                rects.get(0).getWidth() == rects.get(1).getWidth())
                ))
        {
            return true;
        }

        if (rects.size() == 3 && counts.get(0) == 2 && counts.get(1) == 2 && counts.get(2) == 2)
        {
            Set<Integer> set = new HashSet<>();
            for (int i = 0; i < 3; i++) {
                if (rects.get(i).getWidth() == rects.get(i).getHeight())
                    return false;
                set.add(rects.get(i).getHeight());
                set.add(rects.get(i).getWidth());
            }
            return set.size() == 3;
        }

        return false;
    }



    @Override
    public void run() {
        working = true;
        while(working) {
            if(!rects.isEmpty())
            {
                Tasks FileTask = rects.remove();

                Path filePath = Paths.get(FileTask.file_name);
                String fileNameWithoutExtension = filePath.getFileName().toString().replace(".in", "");
                Path newFilePath = filePath.resolveSibling(fileNameWithoutExtension + ".out");
                File newFile = new File(newFilePath.toString());

                Queue<Vector<Integer>> tempQueue = FileTask.allTasks;

                Writer writer;
                try {
                    writer = new FileWriter(newFile);
                    for(Vector<Integer> singleTest : tempQueue) {
                        if (Solve(singleTest))
                            writer.append("YES\n");
                        else
                            writer.append("NO\n");
                        writer.flush();
                    }
                    FileTask.setStatus(TaskStatus.IN_PROGRESS);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                second_rects.add(FileTask);
            }
            if(!reader.isAlive() && rects.isEmpty())
                working = false;
        }
        System.out.println("Я ВЫШЕЛ" + " " + second_rects.size());
    }
}
//TODO : НАЕБЕНИТЬ ДОХУЯ ЭКСЕПШЕНОВ