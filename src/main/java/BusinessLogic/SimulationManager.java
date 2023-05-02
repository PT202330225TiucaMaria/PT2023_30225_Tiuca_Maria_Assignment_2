package BusinessLogic;
import Model.Client;
import Model.Queue;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import static java.lang.Math.random;
public class SimulationManager implements Runnable {
    private int nrClients, nrQueues, simulationInterval;
    private int minArrivalTime, maxArrivalTime;
    private int minServiceTime, maxServiceTime;
    public SelectionPolicy selectionPolicy;
    public Scheduler scheduler; //entitate responsabila pentru managementul coziilor si distributia clientilor
    public List<Client> listaClienti; //lista de clientii care asteapta(urmeaza) sa fie pusi intr-o coada
    public JFrame frame;
    public FileManager file;
    private int stopSimulation = 0, peekHour; //variabila stopSimulation opreste functionarea cozii daca waitingClients este null SAU s-a efectuat timpul de simulare
    private double averageWaitingTime, averageServiceTime;
    public SimulationManager(int nrClients, int nrQueues, int simulationInterval, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime, SelectionPolicy strategy, JFrame frame) {
        this.nrClients = nrClients; this.nrQueues = nrQueues;
        this.simulationInterval = simulationInterval;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
        this.selectionPolicy = strategy;
        this.frame = frame;
        scheduler = new Scheduler(nrQueues);
        scheduler.changeStrategy(selectionPolicy);

        for (int i = 0; i < nrClients; i++)
            generateNRandomClients(i); //generam clienti cu timp de sosire si servire random

        file=new FileManager(listaClienti);

        averageWaitingTime = getAverageWaitingTime();
        averageServiceTime = getAverageServiceTime();
    }
    public void generateNRandomClients(int id) {
        if (listaClienti == null)
            listaClienti = new ArrayList<Client>(); //initializam lista de clienti
        Random r=new Random();
        int rArrival=(r.nextInt(maxArrivalTime- minArrivalTime)+minArrivalTime)%maxArrivalTime+1;
        r=new Random();
        int rService=(r.nextInt(maxServiceTime- minServiceTime)+minServiceTime)%maxServiceTime+1;
        Client client = new Client(id, rArrival, rService);
        listaClienti.add(client); //am creat clientul cu arrivalTime & serviceTime random, iar acum il adaugam in lista de clienti
        Collections.sort(listaClienti, new Comparator<Client>() { //ordonam lista de clienti in ordine crescatoare arrivalTime
            public int compare(Client c1, Client c2) {
                return c1.getArrivalTime() - c2.getArrivalTime();
            }
        });
    }
    @Override
    public void run() {
        int currentTime = 0, maxWaitingTime = 0;
        while (currentTime < simulationInterval) {
            file.print("\nCurrent time: " + currentTime);
            while (!listaClienti.isEmpty() && listaClienti.get(0).getArrivalTime() == currentTime) {
                Client currentClient = listaClienti.remove(0);
                scheduler.dispatchClient(currentClient); //pentru fiecare client care are arrivalTime=currentTime il punem intr-o coada
            }
            //gasim peekHour-ul
            List<Queue> queues = scheduler.getQueues();
            for (int i = 0; i < queues.size(); i++) {
                Queue queue = queues.get(i);
                if (maxWaitingTime <= queue.getWaitingPeriod()) {
                    maxWaitingTime = queue.getWaitingPeriod();
                    peekHour = currentTime;
                }

                file.print("Queue" + (i + 1) + Arrays.toString(queue.getClients()));
            }

            refresh(nrQueues, currentTime, queues, listaClienti);
            if (listaClienti.isEmpty() && stopSimulation == 0) break; //daca lista de clienti care asteapta s-a terminat SAU timpul de simulare s-a terminat, se opreste simularea
            for (int i = 0; i < queues.size(); i++) {
                Queue queue = queues.get(i);
                Client[] lista = queue.getClients();
                if (queue.getWaitingPeriod() > 0) queue.decrementWaitingPeriod();
                if(lista.length >=1) lista[0].setServiceTime(); //actualizam timpul de servire pentru clientul din fruntea listei
            }
            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Thread sleep was interrupted: " + e.getMessage());
            }
        }
        scheduler.stopThreads();

     file.print("\nAverage waiting time: " + averageWaitingTime+"\nAverage service time: " + averageServiceTime+"\nPeek hour: " + peekHour);
     file.printWriterClose();

     JOptionPane.showMessageDialog(frame,"Simulation finished!\n"+"Average waiting time: "+averageWaitingTime+"\n"+"Average service time: "+averageServiceTime+"\n"+"Peak hour: "+peekHour+"\n");
    }
    public void refresh(int nrQueues, int currentTime, List<Queue> queues, List<Client> listaClienti) {
        frame.getContentPane().removeAll();
        frame.repaint();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel timpCurentPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel timpCurent = new JLabel("Current time: " + currentTime);
        timpCurentPanel.add(timpCurent);
        panel.add(timpCurentPanel);
        JPanel waitingClientsPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel listaClientiLabel = new JLabel("Waiting clients: ");
        waitingClientsPanel.add(listaClientiLabel);
        panel.add(waitingClientsPanel);

        for (int i = 0; i < listaClienti.size(); i++) {
            JLabel clientLabel = new JLabel("( " + listaClienti.get(i).getId() + ", " + listaClienti.get(i).getArrivalTime() + ", " + listaClienti.get(i).getServiceTime() + ")");
            waitingClientsPanel.add(clientLabel);
        }

        stopSimulation = 0;
        JPanel cozi = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panel.add(cozi);
        for (int i = 0; i < nrQueues; i++) {
            Client[] queueClients = queues.get(i).getClients();

            JPanel coada = new JPanel(new FlowLayout(FlowLayout.LEADING));
            JLabel queueLabel = new JLabel("Queue " + (i + 1) + ": ");
            coada.add(queueLabel);
            panel.add(coada);
            if (queueClients.length != 0)
                for (int j = 0; j < queueClients.length; j++) {
                    if (queueClients[j].getServiceTime() != 0) {
                        JLabel queueClientLabel = new JLabel("( " + queueClients[j].getId() + ", " + queueClients[j].getArrivalTime() + ", " + queueClients[j].getServiceTime() + ")");
                        coada.add(queueClientLabel);
                    }
                }
            if (queues.get(i).getClients().length != 0) stopSimulation = 1;
        }

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.WEST);
        frame.add(new JPanel(), BorderLayout.CENTER);
        frame.setVisible(true);
    }
    public double getAverageWaitingTime() {
        int timpi = 0;
        for (Client client : listaClienti)
            timpi += client.getArrivalTime();
        double averageWaitingTime=(double) timpi / nrClients;
        averageWaitingTime=(double)Math.round(averageWaitingTime*1000d)/1000d;
        return averageWaitingTime;
    }
    public double getAverageServiceTime() {
        int timpi = 0;
        for (Client client : listaClienti)
            timpi += client.getServiceTime();
        double averageServiceTime=(double) timpi / nrClients;
        averageServiceTime=(double)Math.round(averageServiceTime*1000d)/1000d;
        return averageServiceTime;
    }
}