import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Restaurant extends Thread {
    private RestaurantTemplate restaurant;
    private ArrayList<Porter> porters;
    private ArrayList<OrderTemplate> orders;

    public Restaurant(RestaurantTemplate restaurant) {
        Integer driverNum = restaurant.getDrivers();
        porters = new ArrayList<Porter>();
        for (int i = 0; i < driverNum; i++) {
            porters.add(new Porter());
        }

        this.restaurant = restaurant;
        orders = new ArrayList<>();
    }

    public void addOrder(OrderTemplate newOT) {
        orders.add(newOT);
    }

    public boolean verifyLocation(String location) {
        return (restaurant.getName().equalsIgnoreCase(location));
    }

    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();

        for (OrderTemplate order : orders) {
            executor.execute(new Order(order, porters, restaurant.getDistance()));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.yield();
        }

        /*for (Porter porter : porters) {
            porter.getPorter();
        }*/
    }
}
