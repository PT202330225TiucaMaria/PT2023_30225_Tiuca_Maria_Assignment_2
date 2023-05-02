package BusinessLogic;
import GUI.PrintQueues;
import Model.Client;
import Model.Queue;
import java.util.*;
import java.util.List;
public class SimulationManager implements Runnable {
    private int nrClients, nrQueues, simulationInterval;
    private int minArrivalTime, maxArrivalTime;
    private int minServiceTime, maxServiceTime;
    public SelectionPolicy selectionPolicy;
    //entity responsible with queue management and client distribution
    public Scheduler scheduler;
    //pool of Clients (Client shopping in the store)
    public List<Client> listaClienti;
    //frame for displaying simulation
    public PrintQueues frame;
    //stopSimulation stops the queue's functionation if waitingClients is null OR SimulationTime is wrong
    private int stopSimulation = 0, peekHour;
    private double averageWaitingTime, averageServiceTime;
    public SimulationManager(int nrClients, int nrQueues, int simulationInterval, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime, SelectionPolicy strategy, PrintQueues frame) {
        this.nrClients = nrClients; this.nrQueues = nrQueues;
        this.simulationInterval = simulationInterval;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
        this.selectionPolicy = strategy;
        this.frame = frame;
        //initialize the scheduler
        scheduler = new Scheduler(nrQueues);
        //initialize selection strategy => createStrategy
        scheduler.changeStrategy(selectionPolicy);
        for (int i = 0; i < nrClients; i++)
            generateNRandomClients(i);
        averageWaitingTime = getAverageWaitingTime();
        averageServiceTime = getAverageServiceTime();
    }
    public void generateNRandomClients(int id) {
        if (listaClienti == null)
            listaClienti = new ArrayList<>();
        Random r=new Random();
        int rArrival=(r.nextInt(maxArrivalTime- minArrivalTime)+minArrivalTime)%maxArrivalTime+1;
        r=new Random();
        int rService=(r.nextInt(maxServiceTime- minServiceTime)+minServiceTime)%maxServiceTime+1;
        Client client = new Client(id, rArrival, rService);
        listaClienti.add(client);
        Collections.sort(listaClienti, new Comparator<Client>() { //ordonam lista de clienti in ordine crescatoare arrivalTime
            public int compare(Client c1, Client c2) {
                return c1.getArrivalTime() - c2.getArrivalTime();
            }
        });
    }
    @Override
    public void run() {
        frame.getTextArea1().append("Clienti generati:\n");
        for(Client c:listaClienti){
            frame.getTextArea1().append(c.toString()+"\n");
        }
        int currentTime = 0, maxWaitingTime = 0;
        while (currentTime < simulationInterval) {
            frame.getTextArea1().append("\nCurrent time: " + currentTime+"\n");
            while (!listaClienti.isEmpty() && listaClienti.get(0).getArrivalTime() == currentTime) {
                Client currentClient = listaClienti.remove(0);
                scheduler.dispatchClient(currentClient);
            }
            //gasim peekHour-ul
            List<Queue> queues = scheduler.getQueues();
            for (int i = 0; i < queues.size(); i++) {
                Queue queue = queues.get(i);
                if (maxWaitingTime <= queue.getWaitingPeriod()) {
                    maxWaitingTime = queue.getWaitingPeriod();
                    peekHour = currentTime;
                }

               frame.getTextArea1().append("Queue" + (i + 1)+":");
               for(Client c:queue.getClients()){
                  frame.getTextArea1().append(c.toString());
               }
               frame.getTextArea1().append("\n");
            }

            for (int i = 0; i < queues.size(); i++) {
                Queue queue = queues.get(i);
                Client[] lista = queue.getClients();
                if (queue.getWaitingPeriod() > 0)
                    queue.decrementWaitingPeriod(); //decrementare timp de asteptare per coada
                if(lista.length >=1)
                    lista[0].setServiceTime(); //setare timp de servire pentru primul client din coada
            }
            currentTime++;
            try {
                //waiting an interval from 1 second
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Thread sleep was interrupted: " + e.getMessage());
            }
        }
     scheduler.stopThreads();
     frame.getTextArea1().append("\nAverage waiting time: " + averageWaitingTime+"\nAverage service time: " + averageServiceTime+"\nPeek hour: " + peekHour);
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