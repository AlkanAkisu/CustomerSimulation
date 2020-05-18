
public class Customer {

    public int id;
    public static int nextId = 0;
    public int arrivalTime, waitTime, serviceTime;

    public Customer(int arrivalTime) {
        id = ++nextId;
        this.arrivalTime = arrivalTime;

    }

    public void IncreaseWait() {
        waitTime++;
    }

    public boolean isArrived(int currentTime) {
        return currentTime >= arrivalTime;
    }

    public String toString() {
        return " ID: " + id + " ArrivalTime: " + arrivalTime + " Serve Time: " + serviceTime;
    }

}
