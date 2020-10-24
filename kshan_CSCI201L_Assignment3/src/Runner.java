import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Runner {
    private static ArrayList<RestaurantTemplate> restaurants = new ArrayList<>();
    private static ArrayList<OrderTemplate> orders = new ArrayList<>();
    private static ArrayList<Restaurant> openRestaurants = new ArrayList<>();
    private static RestaurantsData restaurantsData;
    private static double latitude;
    private static double longitude;

    private static void getRestaurantsInformation() {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        Gson gson = new Gson();

        System.out.print("What is the name of the file containing the restaurant information? ");
        while (running) {
            try {
                String path = scanner.nextLine();
                BufferedReader file = new BufferedReader(new FileReader(path));

                restaurantsData = gson.fromJson(file, RestaurantsData.class);
                restaurants = restaurantsData.getData();

                boolean valid = true;
                for (RestaurantTemplate restaurant : restaurants) {
                    if (!restaurant.verifyInformation()) {
                        System.out.print("\nMissing data parameters. Enter the file name again: ");
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    running = false;
                }
            }
            catch (FileNotFoundException error) {
                System.out.print("\nFile doesn't exist. Enter the file name again: ");
            }
            catch (JsonSyntaxException error) {
                System.out.print("\nData format incorrect. Enter the file name again: ");
            }
            catch (NullPointerException error) {
                System.out.print("\nFile is empty. Enter the file name again: ");
            }
        }
    }

    private static void getOrdersInformation() {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nWhat is the name of the file containing the schedule information? ");
        while (running) {
            try {
                String path = scanner.nextLine();
                BufferedReader file = new BufferedReader(new FileReader(path));

                String line = file.readLine();
                while (line != null) {
                    String[] orderInfo = line.split(", ");
                    orders.add(new OrderTemplate(Integer.parseInt(orderInfo[0]), orderInfo[1], orderInfo[2]));
                    line = file.readLine();
                }
                running = false;
            }
            catch (FileNotFoundException error) {
                System.out.print("\nFile doesn't exist. Enter the file name again: ");
            }
            catch (IOException error) {
                System.out.print("\nIncorrect input. Enter the file name again: ");
            }
            catch (NumberFormatException error)  {
                System.out.print("\nData format incorrect. Enter the file name again: ");
            }
            catch (ArrayIndexOutOfBoundsException error) {
                System.out.print("\nMissing data parameters. Enter the file name again: ");
            }
        }
    }

    private static void distributeOrders() {
        for (RestaurantTemplate restaurant : restaurants) {
            openRestaurants.add(new Restaurant(restaurant));
        }

        for (Restaurant restaurant : openRestaurants) {
            for (OrderTemplate order : orders) {
                if (restaurant.verifyLocation(order.getOrderLocation())) {
                    if (restaurant.verifyOrder(order.getFoodDelivered())) {
                        restaurant.addOrder(order);
                    }
                    else {
                        System.out.println("Order" + order.getFoodDelivered() + "invalid in Menu.");
                    }
                }
            }
        }
    }

    private static void getLocation() {
        Scanner scanner = new Scanner(System.in);
        boolean validity = false;

        while (!validity) {
            try {
                System.out.print("\nWhat is the latitude? ");
                latitude = scanner.nextDouble();
            }
            catch (InputMismatchException error) {
                System.out.print("\nInvalid input. Enter the latitude again: ");
            }

            try {
                System.out.print("\nWhat is the longitude? ");
                longitude = scanner.nextDouble();
                validity = true;
            }
            catch (InputMismatchException error) {
                System.out.print("\nInvalid input. Enter the longitude again: ");
            }
        }
    }

    private static void getDistance() {
        for (RestaurantTemplate restaurant : restaurants) {
            double distance;

            double delta = restaurant.getLongitude() - longitude;
            distance = Math.sin(Math.toRadians(restaurant.getLatitude())) *
                    Math.sin(Math.toRadians(latitude))
                    + Math.cos(Math.toRadians(restaurant.getLatitude())) *
                    Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(delta));
            distance = Math.acos(distance);
            distance = 3963.0 * distance;

            restaurant.setDistance(distance);
        }
    }

    public static void main(String[] args) {
        getRestaurantsInformation();
        getOrdersInformation();
        getLocation();
        getDistance();
        distributeOrders();

        System.out.println("\nStarting execution of program...");
        ExecutorService executor = Executors.newCachedThreadPool();

        for (Restaurant restaurant : openRestaurants) {
            executor.execute(restaurant);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.yield();
        }
        System.out.println("All orders complete!");
    }
}
