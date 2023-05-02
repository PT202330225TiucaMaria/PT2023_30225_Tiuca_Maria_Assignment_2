package GUI;

import javax.swing.*;
import java.awt.*;

public class PrintQueues {
    private JPanel panel1;
    private JTextArea textArea1;

    public PrintQueues() {
        final JFrame frame = new JFrame();
        frame.setTitle("QUEUES");
        frame.setMinimumSize(new Dimension(900, 700));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel1);
        frame.pack();
        frame.setVisible(true);
    }
    public JTextArea getTextArea1() {
        return textArea1;
    }
}