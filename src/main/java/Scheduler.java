import java.util.ArrayList;
import java.util.List;
public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxClientiPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers,int maxClientiPerServer){
        this.maxNoServers=maxNoServers;
        this.maxClientiPerServer=maxClientiPerServer;
        this.servers=new ArrayList<Server>();
        //for maxnoservers
        for(int i=0;i<maxNoServers;i++) {
            //create server object
            Server server=new Server();
            servers.add(server);
            //create thread with the object
            Thread thread=new Thread(server);
            thread.start();
        }
    }
    public void changeStrategy(SelectionPolicy policy){
        if(policy==SelectionPolicy.SHORTEST_QUEUE){
            strategy=new ConcreteStrategyQueue();
        }
        if(policy==SelectionPolicy.SHORTEST_TIME){
            strategy=new ConcreteStrategyTime();
        }
    }
    public void dispatchClient(Client t){
        //call the strategy addClient method
        strategy.addClient(servers,t);
    }
    public List<Server> getServers(){
        return servers;
    }
}