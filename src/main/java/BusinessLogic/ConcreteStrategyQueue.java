package BusinessLogic;
import Model.Queue;
import java.util.List;
public class ConcreteStrategyQueue implements Strategy {
    //Metoda pentru a adauga un client in coada in functie de coada cea mai scurta ca numar de clienti
    @Override
    public Queue addClient(List<Queue> queues) {
        int shortestQueueIndex = 0;
        int shortestQueueLength = queues.get(0).getClients().length;
        for (int i = 1; i < queues.size(); i++) {
            int queueLength = queues.get(i).getClients().length;
            if (queueLength < shortestQueueLength) {
                shortestQueueLength = queueLength;
                shortestQueueIndex = i;
            }
        }
        Queue shortQ=queues.get(shortestQueueIndex);
        return shortQ;
    }
}

