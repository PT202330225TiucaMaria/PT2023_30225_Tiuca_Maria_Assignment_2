package BusinessLogic;
import Model.Client;
import Model.Queue;
import java.util.List;
public class ConcreteStrategyTime implements Strategy {
    //Metoda pentru a adauga un client in coada in functie de coada cea mai scurta ca timp
    @Override
    public Queue addClient(List<Queue> queues) {
        int shortestQueueIndex=0;
        int shortestQueueTime = Integer.MAX_VALUE;
        for (int i=0;i<queues.size();i++) {
            Queue q=queues.get(i);
            int waitingTime=0;
            for(int j=0;j<q.getClients().length-1;j++){
                if(j==q.getClients().length-1)
                    waitingTime+=q.getClients()[j].getServiceTime();
                else {
                    Client c=q.getClients()[j];
                    waitingTime+=c.getServiceTime();
                }
            }
            if(waitingTime<shortestQueueTime){
                shortestQueueIndex=i;
                shortestQueueTime=waitingTime;
            }
        }
        Queue shortestTime=queues.get(shortestQueueIndex);
        return shortestTime;
    }

}

