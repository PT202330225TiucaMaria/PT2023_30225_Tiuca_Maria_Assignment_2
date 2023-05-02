package Model;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
public class Queue implements Runnable {
    private final BlockingQueue<Client> clients;
    private final AtomicInteger waitingPeriod; //utilizam AtomicInteger pentru a evita interferenta thread-urilor
    private boolean isRunning; //variabila care controleaza oprirea fortata a tuturor coziilor
    public Queue() {
        //initialize queue and waitingPeriod
        clients = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger();
        isRunning = true;
    }
    public void addClient(Client client) { //adaugam clientul si actualizam timpul total de asteptare la coada
        //add Client to queue
        clients.add(client);
        //increment the waitingPeriod
        waitingPeriod.addAndGet(client.getServiceTime());
    }
    public void run() {
        while (isRunning) {
            try {
                //take next Client from queue
                Client client = clients.peek();
                if (client != null) {
                    //stop the thread for a time equal with the Client's processing time
                    Thread.sleep(client.getServiceTime() * 1000L);
                    clients.remove(client);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    public Client[] getClients() {
        return clients.toArray(new Client[0]);
    }
    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }
    //decrement the waitingPeriod
    public void decrementWaitingPeriod() {
        waitingPeriod.addAndGet(-1);
    }
    public void stopQueue() {
        isRunning = false;
    }
}