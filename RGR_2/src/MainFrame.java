import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;

public class MainFrame extends JFrame implements ActionListener {
    // ВСЕ ЧТО КАСАЕТСЯ ГРАФИКИ
    private static JTextField changeDirectoryPath;
    private static JButton runThread;
    private static JButton checkTask;
    private static JButton appendResults;
    private static JButton dialogButton;
    private static JCheckBox CorCheckBox;
    private static JCheckBox ComCheckBox;
    private static JCheckBox WasCheckBox;
    private static JCheckBox WrongCheckBox;
    private static JCheckBox ValCheckBox;
    private JTextArea textArea;
    private JLabel errorLabel;
    private JLabel secondErrorLabel;
    private JLabel ThreadStop;

    // ВСЕ ЧТО КАСАЕТСЯ АЛГОРИТМА
    static Queue<Test> Tests;
    static Queue<Test> Results;
    static Queue<Test> ValidatedResults;
    File[] files;
    FirstThread firstThread;
    SolveThread secondThread;
    CoolThread thirdThread;

    MainFrame() {
        this.setLayout(null);
        this.setTitle("RGR_2");
        this.setSize(500, 400);
        this.setLocation(500, 500);

        changeDirectoryPath = new JTextField();
        changeDirectoryPath.addActionListener(this);
        changeDirectoryPath.setBounds(20, 20, 350, 30);
        changeDirectoryPath.setText("");
        changeDirectoryPath.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateString();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateString();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateString();
            }
        });
        this.add(changeDirectoryPath);

        runThread = new JButton("START");
        runThread.addActionListener(this);
        runThread.setBounds(20, 70, 100, 30);
        add(runThread);

        dialogButton = new JButton("...");
        dialogButton.addActionListener(this);
        dialogButton.setBounds(370, 20, 100, 30);
        add(dialogButton);

        checkTask = new JButton("STOP");
        checkTask.addActionListener(this);
        checkTask.setBounds(20, 100, 100, 30);
        checkTask.setEnabled(false);
        add(checkTask);

        appendResults = new JButton("APPEND");
        appendResults.addActionListener(this);
        appendResults.setBounds(20, 130, 100, 30);
        add(appendResults);

        CorCheckBox = new JCheckBox("Corrupted");
        CorCheckBox.setBounds(20, 170, 100, 20);
        add(CorCheckBox);
        ComCheckBox = new JCheckBox("Completed");
        ComCheckBox.setBounds(20, 190, 100, 20);
        add(ComCheckBox);
        WasCheckBox = new JCheckBox("Not checked");
        WasCheckBox.setBounds(20, 210, 100, 20);
        add(WasCheckBox);
        WrongCheckBox = new JCheckBox("Wrong");
        WrongCheckBox.setBounds(20, 230, 100, 20);
        add(WrongCheckBox);
        ValCheckBox = new JCheckBox("Validating");
        ValCheckBox.setBounds(20, 250, 100, 20);
        add(ValCheckBox);

        errorLabel = new JLabel("Choose right folder location");
        errorLabel.setBounds(20, 50, 350, 14);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        add(errorLabel);

        secondErrorLabel = new JLabel("There is no files with .in extension");
        secondErrorLabel.setBounds(20, 50, 350, 14);
        secondErrorLabel.setForeground(Color.RED);
        secondErrorLabel.setVisible(false);
        add(secondErrorLabel);

        ThreadStop = new JLabel("");
        ThreadStop.setBounds(20, 330, 350, 14);
        ThreadStop.setForeground(Color.BLACK);
        ThreadStop.setVisible(false);
        add(ThreadStop);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(120, 70, 350, 250);
        textArea.setEditable(false);

        scrollPane.addMouseWheelListener(e -> {
            JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getValue() - e.getWheelRotation() * verticalScrollBar.getUnitIncrement());
        });

        add(scrollPane);

        this.setResizable(false);

        Tests = new ArrayDeque<>();
        Results = new ArrayDeque<>();
        ValidatedResults = new ArrayDeque<>();
    }

    private void updateString() {
        File directory = new File(changeDirectoryPath.getText());
        secondErrorLabel.setVisible(false);
        if (!directory.isDirectory())
        {
            runThread.setEnabled(false);
            checkTask.setEnabled(false);
            errorLabel.setVisible(true);
        }
        else
        {
            runThread.setEnabled(true);
            checkTask.setEnabled(true);
            errorLabel.setVisible(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == runThread) {
            if(changeDirectoryPath.getText().isEmpty())
                errorLabel.setVisible(true);
            File directory = new File(changeDirectoryPath.getText());
            if (directory.isDirectory()) {
                files = directory.listFiles();
                boolean b = false;
                for(File file : files)
                    if (file.getName().charAt(file.getName().length() - 1) == 'n' && file.getName().charAt(file.getName().length() - 2) == 'i') {
                        b = true;
                        break;
                    }
                if (!b)
                    secondErrorLabel.setVisible(true);
                else {
                    firstThread = new FirstThread(Tests, files, changeDirectoryPath.getText());
                    secondThread = new SolveThread(firstThread, Tests, Results);
                    firstThread.start();
                    secondThread.start();
                    thirdThread = new CoolThread(firstThread, secondThread, Results, ValidatedResults);
                    thirdThread.start();

                    checkTask.setEnabled(true);
                    runThread.setEnabled(false);
                }
            }
        }

        if (e.getSource() == checkTask) {
            if(secondThread.running)
                ThreadStop.setText("You have stopped the second Thread");
            else
                ThreadStop.setText("Second Thread has already finished its work");
            ThreadStop.setVisible(true);
            secondThread.running = !secondThread.running;
            checkTask.setEnabled(false);
        }

        if(e.getSource() == appendResults)
        {
            textArea.setText("");
            synchronized (Tests) {
                Queue<Test> copyOfTests = new ArrayDeque<>(Tests);
                    for (Test temp : copyOfTests) {
                        if(CorCheckBox.isSelected() && temp.State == Test.status.Corrupted)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                        if(ComCheckBox.isSelected() && temp.State == Test.status.Completed)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                        if(WasCheckBox.isSelected() && temp.State == Test.status.wasNotChecked)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                        if(WrongCheckBox.isSelected() && temp.State == Test.status.Wrong)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                        if(ValCheckBox.isSelected() && temp.State == Test.status.Validating)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                    }
            }
            synchronized (Results) {
                Queue<Test> copyOfResults = new ArrayDeque<>(Results);
                    for(Test temp : copyOfResults) {
                        if(CorCheckBox.isSelected() && temp.State == Test.status.Corrupted)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                        if(ComCheckBox.isSelected() && temp.State == Test.status.Completed)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                        if(WasCheckBox.isSelected() && temp.State == Test.status.wasNotChecked)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                        if(WrongCheckBox.isSelected() && temp.State == Test.status.Wrong)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                        if(ValCheckBox.isSelected() && temp.State == Test.status.Validating)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                    }
            }
            synchronized (ValidatedResults) {
                Queue<Test> copyOfValidatedResults = new ArrayDeque<>(ValidatedResults);
                    for(Test temp : copyOfValidatedResults) {
                        if(CorCheckBox.isSelected() && temp.State == Test.status.Corrupted)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                        if(ComCheckBox.isSelected() && temp.State == Test.status.Completed)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                        if(WasCheckBox.isSelected() && temp.State == Test.status.wasNotChecked)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                        if(WrongCheckBox.isSelected() && temp.State == Test.status.Wrong)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                        if(ValCheckBox.isSelected() && temp.State == Test.status.Validating)
                            textArea.append(temp.Name + " | " + temp.State + "\n");
                    }
            }
        }
        if(e.getSource() == dialogButton)
            Dialog();
    }
    private void Dialog() {
        JFileChooser chose = new JFileChooser();
        chose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = chose.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = chose.getSelectedFile();
            changeDirectoryPath.setText(selectedFolder.getPath());
        }
    }
}
