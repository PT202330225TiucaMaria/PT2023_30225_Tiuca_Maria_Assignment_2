import java.util.*;

public class Client {
    private int ID; //nr intre 1 si N-nr clienti
    private int timeArrival;
    private int timeService;

    public Client(int ID,int timpMinArrival,int timpMaxArrival,int timpMinService,int timpMaxService){
        this.ID=ID;
        Random rArrival=new Random();
        this.timeArrival=(rArrival.nextInt(timpMaxArrival- timpMinArrival + 1)+timpMinArrival)%timpMaxArrival;
        Random rService=new Random();
        this.timeService=(rService.nextInt(timpMaxService- timpMinService + 1)+timpMinService)%timpMaxService;
    //System.out.println("timp arrival: "+this.timeArrival+" timp service: "+this.timeService);
    }
    public int getID() {
        return ID;
    }
    public int getTimeArrival() {
        return timeArrival;
    }
    public int getTimeService() {
        return timeService;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public void setTimeArrival(int timeArrival) {
        this.timeArrival = timeArrival;
    }
    public void setTimeService(int timeService) {
        this.timeService = timeService;
    }
}
