import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Porter extends Thread {
    private static List<Order> ordersWaiting = Collections.synchronizedList(new ArrayList<Order>());
    private Lock driverLock = new ReentrantLock();
    private Condition availabilityCondition = driverLock.newCondition();
    private boolean availability;

    public Porter() {
        availability = true;
        this.start();
    }

    public boolean getAvailability() {
        return availability;
    }

    public static synchronized void addOrder(Order order) {
        ordersWaiting.add(order);
    }

    public void getPorter() {
        try {
            driverLock.lock();
            availability = false;
            availabilityCondition.signal();
        }
        finally {
            driverLock.unlock();
        }
    }

    public void run() {
        while (true) {
            while (!ordersWaiting.isEmpty()) {
                Order current;
                synchronized (this) {
                    current = ordersWaiting.remove(0);
                }

                current.startingDelivery();
                try {
                    Thread.sleep((long) (current.getDistance() * 1000 * 2));
                }
                catch (InterruptedException ie) {
                    System.out.println("ie is delivering: " + ie.getMessage());
                }
                current.finishingDelivery();
            }
            try {
                driverLock.lock();
                availability = true;
                availabilityCondition.await();
            }
            catch (InterruptedException ie) {
                System.out.println("ie is waiting: " + ie.getMessage());
            }
            finally {
                driverLock.unlock();
            }
        }
    }
}
