import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.MalformedInputException;

public class MainFrame extends JFrame implements ActionListener {
    private static JButton govnoButton;
    private static JLabel govnoLabel;
    MainFrame()
    {
        this.setLayout(null);
        this.setTitle("RGR_2");
        this.setSize(500, 400);
        this.setLocation(500, 500);
        govnoButton = new JButton();
        govnoButton.addActionListener(this);
        govnoButton.setBounds(20,20, 450, 30);
        govnoButton.setText("pizda");
        this.add(govnoButton);
        govnoLabel = new JLabel("pizda");
        govnoLabel.setBounds(50, 50, 20, 20);
        this.add(govnoLabel);
        this.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == govnoButton)
            System.out.println("MATVEY SPASIBO");
    }
}
