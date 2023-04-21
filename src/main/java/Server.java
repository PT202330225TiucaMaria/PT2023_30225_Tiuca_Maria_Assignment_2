import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
public class Server implements Runnable{
    private BlockingQueue<Client> clienti;
    private AtomicInteger waitingPeriod;
    public Server(){
        //initialize queue and waitingPeriod
        clienti = new LinkedBlockingQueue<Client>();
        waitingPeriod=new AtomicInteger();
    }
    public void addClient(Client newClient){
        //adaugam clienti in coada
        clienti.add(newClient);
        //incrementam perioada de asteptare
        waitingPeriod.addAndGet(newClient.getTimeService());
    }
    public void run(){
        while(true){
            try {
                //luam urm client din coada
                Client currentClient=clienti.take();
                //oprim thread ul pt un timp=timpul de procesare al clientului
                Thread.sleep(currentClient.getTimeService());
                //decrementam perioada de asteptare
                waitingPeriod.addAndGet(-currentClient.getTimeService());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public Client[] getClient(){
        return clienti.toArray(new Client[clienti.size()]);
    }
}