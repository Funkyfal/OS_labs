import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Vector;

public class Main_frame extends JFrame implements ActionListener {
    JButton choose_files;

    public static Tasks tasks;
    JButton openButton;
    JButton start_button;
    JButton pause_button;
    JTextField filePathField;
    JTextArea textArea = new JTextArea();
    /////////////////////////////////////
    String DirectoryP;
    ParallelepipedSolver  solver;
    public static Queue<Tasks> allTasks;
    public static Queue<Tasks> secondAllTasks;
    RectangleReader fThread;

    Main_frame() {
        this.setSize(600, 400);

        choose_files = new JButton("...");
        choose_files.setBackground(Color.GRAY);
        choose_files.setFocusPainted(false);
        choose_files.addActionListener(this);

        start_button = new JButton("Start");
        start_button.setBackground(Color.GRAY);
        start_button.setFocusPainted(false);
        start_button.addActionListener(this);

        pause_button = new JButton("Pause");
        pause_button.setBackground(Color.GRAY);
        pause_button.setFocusPainted(false);
        pause_button.addActionListener(this);

        Dimension buttonSize = choose_files.getPreferredSize();
        buttonSize.width *= 2.5;
        choose_files.setPreferredSize(buttonSize);

        filePathField = new JTextField(30);

        openButton = new JButton("Open");
        openButton.setBackground(Color.GRAY);
        openButton.setFocusPainted(false);
        openButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); // Use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(start_button);
        buttonPanel.add(pause_button);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(filePathField, BorderLayout.CENTER);
        mainPanel.add(choose_files, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(mainPanel);

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        allTasks = new ArrayDeque<>();
        secondAllTasks = new ArrayDeque<>();

        for (int i = 0; i < 100; i++) {
            textArea.append("I LOVE YOU ^_^ " + i + "\n");
        }
/////////////////////////////////////////////////////////////////////////////////
        setLocationRelativeTo(null);
        setVisible(true);

        DirectoryP = "";

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == choose_files) {
            Dialog();
        } else if (e.getSource() == openButton) {
            DirectoryP = filePathField.getText();
            System.out.println(DirectoryP);
        } else if (e.getSource() == start_button) {
            Start_Thread();
        } else if (e.getSource() == pause_button) {
            Pause_Thread();
        }
    }

    private void Dialog() {
        JFileChooser chose = new JFileChooser();
        chose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = chose.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = chose.getSelectedFile();
            filePathField.setText(selectedFolder.getPath());
        }
    }

    private void openFile() {
        String filePath = filePathField.getText();
        File file = new File(filePath);

        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "File does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Start_Thread() {
        File tempDir = new File(DirectoryP);

        /*tasks = new Tasks(tempDir.getName(), TaskStatus.COMPLETED);*/
        if (tempDir.isDirectory()) {
            File[] checkFiles = tempDir.listFiles();
            if (checkFiles != null) {
                fThread = new RectangleReader(DirectoryP, allTasks);
                solver = new ParallelepipedSolver(allTasks, secondAllTasks, fThread);
                fThread.start();
                try {
                    fThread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                solver.start();
            }
            //todo: Сделать else и запилить экспешн
        }
        //todo: Сделать else и запилить экспешн
    }

    private void Pause_Thread() {
        solver.working = false;
    }
}
