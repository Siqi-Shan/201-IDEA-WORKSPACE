public class OrderTemplate {
    private Integer readyTime;
    private String orderLocation;
    private String foodDelivered;

    public OrderTemplate(Integer readyTime, String orderLocation, String foodDelivered) {
        this.readyTime = readyTime;
        this.orderLocation = orderLocation;
        this.foodDelivered = foodDelivered;
    }

    public Integer getReadyTime() {
        return readyTime;
    }

    public String getOrderLocation() {
        return orderLocation;
    }

    public String getFoodDelivered() {
        return foodDelivered;
    }
}

