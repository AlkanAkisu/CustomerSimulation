import java.util.LinkedList;

/**
 * CustomerQueue
 */
public class CustomerQueue {

  public static LinkedList<Customer> customerQueue = new LinkedList<Customer>();
  public int maxWaitTime = 0, maxCustomerLine = 0, totalWaitTime = 0;

  public void enqueue(Customer customer) {

    customerQueue.add(customer);

  }

  public void dequeue() {

    MaximumWaitTime();
    CurrentCustomerNumber();
    if (!customerQueue.isEmpty()) {
      System.out.println(
          "Customer removed id: " + customerQueue.getFirst().id + " Queue size: " + (customerQueue.size() - 1));
      totalWaitTime += customerQueue.getFirst().waitTime;
      customerQueue.remove();

    }
    // System.out.println("removed");
  }

  public void IncreaseQueueWait() {
    for (Customer customer : customerQueue) {
      if (customer == customerQueue.getFirst())
        continue;
      customer.IncreaseWait();

    }
  }

  // call this func before deleting node
  public void CurrentCustomerNumber() {
    int currentCustomerSize = customerQueue.size();

    if (currentCustomerSize > maxCustomerLine)
      maxCustomerLine = currentCustomerSize;

  }

  // call this func before deleting node
  public void MaximumWaitTime() {

    for (Customer customer : customerQueue) {
      int currentWaitTime = customer.waitTime;
      if (maxWaitTime == 0)
        maxWaitTime = currentWaitTime;
      if (currentWaitTime > maxWaitTime)
        maxWaitTime = currentWaitTime;

    }

  }

  public double AverageWaitTime() {
    return totalWaitTime/(double)Cashier.totalCustomer;
  }
}