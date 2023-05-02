package BusinessLogic;
import Model.Client;
import Model.Queue;
import java.util.ArrayList;
import java.util.List;
public class Scheduler {
    private List<Queue> queues;
    private Strategy strategy;
    public Scheduler(int nrQueues) {
        queues = new ArrayList<>();
        //for maxNoQueues
        for (int i = 0; i < nrQueues; i++) {
            //create queue object
            Queue queue = new Queue();
            //create thread with the object
            Thread thread = new Thread(queue);
            thread.start();
            queues.add(queue);
        }
    }
    public void changeStrategy(SelectionPolicy policy) {
       //apply strategy patter to instantiate the strategy with the concrete strategy corresponding to policy
        if (policy == SelectionPolicy.SHORTEST_QUEUE)
            strategy = new ConcreteStrategyQueue();
        if (policy == SelectionPolicy.SHORTEST_TIME)
            strategy = new ConcreteStrategyTime();
    }
    public void dispatchClient(Client client) {
        //call the strategy addClient method
        Queue selectedQueue = strategy.addClient(queues);
        selectedQueue.addClient(client);
    }
    public List<Queue> getQueues() {
        return queues;
    }
    //method to stop the queues threads
    public void stopThreads() {
        for (Queue queue : queues)
            queue.stopQueue();
    }
}