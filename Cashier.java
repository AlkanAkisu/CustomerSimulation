

public class Cashier {

  public static int totalServiceTime = 0, totalCustomer = 0;
  public Customer currentCustomer;
  public boolean serveFinished = false;
  int currentTimeServed = 0;

  public Cashier() {

  }
  // TODO assign wait time to customer in main loop
  // call just once for per customer or it give random value every time

  public double GetAverageServiceTime() {
    return totalServiceTime / (double) totalCustomer;
  }

  public void Serve(Customer customer) {
    currentTimeServed++;

    if (customer.serviceTime > currentTimeServed) {
      System.out.println("Serving..." + " Customer no:" + customer.id +
      " Remaining Serve: " + (customer.serviceTime - currentTimeServed - 1));
      serveFinished = false;
    } else {
      // serve finished
      System.out.println("Serve Finished." + " Customer no:" + customer.id );
      serveFinished = true;
      currentTimeServed = 0;
    }
  }

}

