import java.util.ArrayList;

public class RestaurantTemplate {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer drivers;
    private ArrayList<String> menu = new ArrayList<>();
    private transient double distance;

    public RestaurantTemplate(String name, String address, Double latitude, Double longitude, Integer drivers,
                      ArrayList<String> menu) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.drivers = drivers;
        this.menu = menu;
    }

    public Integer getDrivers() {
        return drivers;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public boolean verifyInformation() {
        return (this.name != null && this.address != null && this.latitude != null && this.longitude != null &&
                this.drivers != null && this.menu != null);
    }
}