package GUI;
import BusinessLogic.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static BusinessLogic.SelectionPolicy.SHORTEST_QUEUE;
import static BusinessLogic.SelectionPolicy.SHORTEST_TIME;

public class EnterData {
    private JLabel NrClient;
    private JPanel NrClients;
    private JLabel NrQueue;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JLabel MinimArrivalTime;
    private JLabel MaximArrivalTime;
    private JLabel MinimServiceTime;
    private JLabel MaximServiceTime;
    private JCheckBox shortestTimeCheckBox;
    private JCheckBox shortestQueueCheckBox;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton STARTButton;
    SimulationManager manager;
    private PrintQueues gui2;
    public EnterData(){
        final JFrame frame=new JFrame();
  ////////////////////////////////////
        frame.setTitle("EnterData");
        frame.setMinimumSize(new Dimension(900,700));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(NrClients);
        frame.pack();
///////////////////////////////////////////
        textField1.setVisible(true);
        textField2.setVisible(true);
        textField3.setVisible(true);
        textField4.setVisible(true);
        textField5.setVisible(true);
        textField6.setVisible(true);
        textField7.setVisible(true);
        textField1.setEditable(true);
        textField2.setEditable(true);
        textField3.setEditable(true);
        textField4.setEditable(true);
        textField5.setEditable(true);
        textField6.setEditable(true);
        textField7.setEditable(true);
        STARTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nrClients = Integer.parseInt(textField1.getText());
                int nrQueues = Integer.parseInt(textField2.getText());
                int simulationTime = Integer.parseInt(textField3.getText());
                int minArrivalTime = Integer.parseInt(textField4.getText());
                int maxArrivalTime = Integer.parseInt(textField5.getText());
                int minServiceTime = Integer.parseInt(textField6.getText());
                int maxServiceTime = Integer.parseInt(textField7.getText());
                SelectionPolicy strategy = null;
                if (shortestQueueCheckBox.isSelected()) {
                    strategy = SHORTEST_QUEUE;
                } else if (shortestTimeCheckBox.isSelected()) {
                    strategy = SHORTEST_TIME;
                }
                gui2=new PrintQueues();
                manager = new SimulationManager(nrClients, nrQueues, simulationTime, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime, strategy, gui2);
                Thread t = new Thread(manager);
                t.start();
                System.out.println("click buton start");
            }
        });
        frame.setVisible(true);
    }
}
