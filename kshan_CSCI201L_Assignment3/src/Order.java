import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Order extends Thread {
    private OrderTemplate OT;
    private ArrayList<Porter> porters;
    private double distance;

    private Lock orderLock = new ReentrantLock();
    private Condition deliveryCondition = orderLock.newCondition();

    public Order(OrderTemplate OT, ArrayList<Porter> porters, double distance) {
        this.OT = OT;
        this.porters = porters;
        this.distance = distance;
    }

    public void startingDelivery() {
        Util.printMessage("Starting delivery of " + OT.getFoodDelivered() + " from " + OT.getOrderLocation() + "!");
    }

    public void finishingDelivery() {
        Util.printMessage("Finished delivery of " + OT.getFoodDelivered() + " from " + OT.getOrderLocation() + "!");
        try {
            orderLock.lock();
            deliveryCondition.signal();
        }
        finally {
            orderLock.unlock();
        }
    }

    public double getDistance() {
        return distance;
    }

    public void run() {
        try {
            Thread.sleep(OT.getReadyTime() * 1000);
        }
        catch (InterruptedException ie) {
            System.out.println("ie order not activated: " + ie.getMessage());
        }

        Porter.addOrder(this);
        for (Porter porter : porters) {
            if (porter.getAvailability()) {
                porter.getPorter();
                break;
            }
        }

        try {
            orderLock.lock();
            deliveryCondition.await();
        }
        catch (InterruptedException ie) {
            System.out.println("ie getting delivery: " + ie.getMessage());
        }
        finally {
            orderLock.unlock();
        }
    }








}
