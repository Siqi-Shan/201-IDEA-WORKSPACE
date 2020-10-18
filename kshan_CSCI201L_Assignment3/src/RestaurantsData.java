import java.util.ArrayList;

public class RestaurantsData {
    private ArrayList<RestaurantTemplate> data = new ArrayList<>();

    public RestaurantsData(ArrayList<RestaurantTemplate> data) {
        this.data = data;
    }

    public ArrayList<RestaurantTemplate> getData() {
        return data;
    }
}
