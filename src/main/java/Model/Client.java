package Model;
public class Client {
    private final int idClient, arrivalTime;
    private int serviceTime;
    public Client(int idClient, int arrivalTime, int serviceTime) {
        this.idClient = idClient;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getServiceTime() {
        return serviceTime;
    }
    public void setServiceTime() { //decrementare timpului de servire a clientului din fruntea cozii
        if (serviceTime > 0) serviceTime--;
    }
    public String toString() {
        return "(id: " + idClient + ", arrivalTime: " + arrivalTime + ", serviceTime: " + serviceTime + ")";
    }
}