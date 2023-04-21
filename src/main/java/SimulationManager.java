import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimulationManager implements Runnable{
    private int nrClienti;
    private int nrCozi;
    private int timpSimulare;
    private int timpMinArrival;
    private int timpMaxArrival;
    private int timpMinService;
    private int timpMaxService;

    public SelectionPolicy selectionPolity=SelectionPolicy.SHORTEST_TIME;
    private Scheduler scheduler;
    private SimulationFrame frame;
    private List<Client> generatedClienti;

    public SimulationManager(){
        Scanner input=new Scanner(System.in);
        System.out.print("Introdu numarul de clienti: ");
        this.nrClienti=input.nextInt();
        System.out.print("Introdu numarul de cozi: ");
        this.nrCozi=input.nextInt();
        System.out.print("Introdu timpul de simulare: ");
        this.timpSimulare=input.nextInt();
        System.out.print("Introdu timpul minim arrival: ");
        this.timpMinArrival=input.nextInt();
        System.out.print("Introdu timpul maxim arrival: ");
        this.timpMaxArrival=input.nextInt();
        System.out.print("Introdu timpul minim servire: ");
        this.timpMinService=input.nextInt();
        System.out.print("Introdu timpul maxim service: ");
        this.timpMaxService=input.nextInt();

//        intialize the scheduler
//                create and start numberOfServers threads
//                initialize selection strategy=>createStrategy
//                initialize frame to display simulation
//                generate numberOfClients clients using generateNRandomTasks() and store them to generatedTasks
    }
    private void generateNRandomTasks(){
//        generate N random Clienti
//        random processing time
//        minProcessingTime<processingTime<maxProcessingTime
//        random arrivalTime
        ArrayList<Client> clienti=new ArrayList<Client>(nrClienti);
        for(int i=0;i<nrClienti;i++){
            clienti.set(i, new Client(i, timpMinArrival, timpMaxArrival, timpMinService, timpMaxService));
        }
        //        sort list with respect to arrivalTime
    }
    public void run(){
        int currentTime=0;
        while(currentTime<timpSimulare){
            //iterate generatedTasks list and pick tasks that have the arrivalTime equal with the currentTime
            //send Clienti to queue by calling the dispatchTask method from Scheduler
            //delete Client from list
            // update UI frame
            currentTime++;
            //wait an interval of 1 second
        }
    }
    public static void main(String[] args){
      SimulationManager gen=new SimulationManager();
      Thread t=new Thread(gen);
      t.start();
        }
}