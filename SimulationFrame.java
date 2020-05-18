import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class SimulationFrame {
	JFrame scndFrame;
	JFrame frame;
	JPanel panel;
	JOptionPane messagePane;
	JTextField maxSimuTimeTxt, maxArrTimeTxt, maxServTimeTxt;
	JButton startButton;
	JLabel maxSimuTime, maxServTime, maxArrTime, timerSpeedLbl;
	JLabel simuTime, simuTimeVal, queueLen, queueLenVal, totSerTime, totSerTimeVal;
	JComboBox<String> timerSpeed;
	static int maxSimu, maxArr, maxServ, timeSpd, seconds;
	JPanel scndPnl;

	static CustomerQueue queue;
	static Cashier cashier;
	static boolean secondAdded;

	Customer cashingCustomer = null, newCustomer, firstCustomer;

	boolean customerRemoved = true, cashierBusy = false;

	static int nextArrival;

	public SimulationFrame() {
		firstWindow();

	}

	private void firstWindow() {
		final String cBoxList[] = {"5",  "200", "400", "500", "1000" };

		// Setting up Components
		frame = new JFrame();
		panel = new JPanel();
		messagePane = new JOptionPane();
		maxSimuTime = new JLabel("Max Simulation Time: ");
		maxArrTime = new JLabel("Max Arrival Time: ");
		maxServTime = new JLabel("Max Service Time: ");
		timerSpeedLbl = new JLabel("Timer Speed (Milliseconds)");
		maxSimuTimeTxt = new JTextField(20);
		maxArrTimeTxt = new JTextField(20);
		maxServTimeTxt = new JTextField(20);
		startButton = new JButton();
		startButton.setText("Start Simulation");
		timerSpeed = new JComboBox<>(cBoxList);

		// Setting the Panel
		panel.setBorder(BorderFactory.createEmptyBorder(70, 100, 70, 100));
		panel.setLayout(new GridLayout(5, 2));

		// Adding Components to The Panel
		panel.add(maxSimuTime);
		panel.add(maxSimuTimeTxt);
		panel.add(maxArrTime);
		panel.add(maxArrTimeTxt);
		panel.add(maxServTime);
		panel.add(maxServTimeTxt);
		panel.add(timerSpeedLbl);
		panel.add(timerSpeed);
		panel.add(startButton);

		// Adding Panel to Frame
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("COMP:132 HW2");
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600, 400);

		// Putting button on action
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {

				try {
					maxSimu = Integer.parseInt(maxSimuTimeTxt.getText());
					maxArr = Integer.parseInt(maxArrTimeTxt.getText());
					maxServ = Integer.parseInt(maxServTimeTxt.getText());
					timeSpd = Integer.parseInt(cBoxList[timerSpeed.getSelectedIndex()]);
					frame.dispose();
					secondWindow();
				} catch (final NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "You didn't enter valid information");
				}

			}

		});
	}

	private void secondWindow() {

		scndFrame = new JFrame();
		scndPnl = new JPanel();

		simuTime = new JLabel("Current Simulation Time:");
		simuTimeVal = new JLabel();
		queueLen = new JLabel("Current Queue Lenght: ");
		queueLenVal = new JLabel();
		totSerTime = new JLabel("Total Service Time: ");
		totSerTimeVal = new JLabel();

		// Creating frame and panel for second window;
		scndPnl.setBorder(BorderFactory.createEmptyBorder(70, 100, 70, 100));
		scndPnl.setLayout(new GridLayout(3, 2));

		scndPnl.add(simuTime);
		scndPnl.add(simuTimeVal);
		scndPnl.add(queueLen);
		scndPnl.add(queueLenVal);
		scndPnl.add(totSerTime);
		scndPnl.add(totSerTimeVal);

		scndFrame.add(scndPnl, BorderLayout.CENTER);
		scndFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		scndFrame.pack();
		scndFrame.setSize(600, 400);
		scndFrame.setVisible(true);



		timerStarter(simuTimeVal, queueLenVal, totSerTimeVal);

	}

	private void timerStarter(JLabel label, JLabel queueVal , JLabel totSerTimeVal) {

		cashier = new Cashier();
		queue = new CustomerQueue();
		firstCustomer = CreateCustomer();
		queue.enqueue(firstCustomer);

		nextArrival = firstCustomer.arrivalTime;

		customerRemoved = true;
		cashierBusy = false;

		ActionListener counter = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				if (seconds >= maxSimu) {
					//add last ones
					for (Customer customer : CustomerQueue.customerQueue)
						queue.totalWaitTime += customer.waitTime;

					System.out.println("Average Wait Time: " + queue.AverageWaitTime() + "Total Wait Time: " + queue.totalWaitTime);

					String stats = "Number of Customers: " + Cashier.totalCustomer + "\nAverage Wait Time: "
							+ queue.AverageWaitTime() + "\nAverage Serve Time: " + cashier.GetAverageServiceTime()
							+ "\nMaximum Wait Time: " + queue.maxWaitTime + "\nMaximum Queue Length: " + queue.maxCustomerLine;
					scndFrame.dispose();
					JOptionPane.showMessageDialog(null, stats);
					System.exit(0);
				} else {
					label.setText("" + seconds);
					seconds++;
					int size = CustomerQueue.customerQueue.size();
					queueVal.setText("" + size);
					totSerTimeVal.setText("" + Cashier.totalServiceTime);


					// ----------------------MAIN LOOP----------------------
					System.out.println("Current Second: " + seconds);
					System.out.println("-------------------------");
					if (seconds >= nextArrival) {
						newCustomer = CreateCustomer();
						queue.enqueue(newCustomer);
						nextArrival = newCustomer.arrivalTime;
					}

					if (!cashierBusy && !CustomerQueue.customerQueue.isEmpty()) {

						firstCustomer = CustomerQueue.customerQueue.getFirst();
						if (firstCustomer.isArrived(seconds)) {
							cashierBusy = true;
							cashingCustomer = firstCustomer;
						}
					}

					if (cashierBusy && !CustomerQueue.customerQueue.isEmpty()) {

						cashier.Serve(cashingCustomer);

						if (cashier.serveFinished)
							cashierBusy = false;
						else
							cashierBusy = true;
						customerRemoved = false;

					}

					if (!cashierBusy && !customerRemoved) {
						queue.dequeue();
						customerRemoved = true;
					}

					queue.IncreaseQueueWait();
					// ----------------------MAIN LOOP FINISH----------------------
				}

			}
		};


		Timer timer = new Timer(timeSpd, counter);
		timer.setInitialDelay(0);
		timer.start();
		seconds = 0;

	}

	public static int GetArrival() {
		Random rnd = new Random();
		return rnd.nextInt(maxArr) + 1;
	}

	public static int GetServiceTime() {
		Random random = new Random();
		return random.nextInt(maxServ) + 1 ;

	}

	public static Customer CreateCustomer() {
		Customer customer = new Customer(GetArrival() + seconds);

		customer.serviceTime = GetServiceTime();
		Cashier.totalServiceTime += customer.serviceTime;
		Cashier.totalCustomer += 1;

		System.out.println("Customer Added: " + customer + " Queue size: " + CustomerQueue.customerQueue.size());
		// uncomment when it works
		// secondCustomer.SetWaitTime();

		return customer;
	}

}