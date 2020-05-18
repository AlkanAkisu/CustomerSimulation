public class SimulationApp {
    static CustomerQueue queue;
    static Cashier cashier;
    static boolean secondAdded;

    static int maxArrivalTime, simTime, currentTime, maxServiceTime, nextArrival;

    public static void main(String[] args) {

        new SimulationFrame();


    }
}