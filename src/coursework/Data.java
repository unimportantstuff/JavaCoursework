package coursework;

import java.io.Serializable;

/**
 * @author Kartik Nair
 */

public class Data implements Serializable {
    private double distance;
    private double result;
    private double price;
    private double efficiency;
    private String type;

    Data(double distance, double efficiency, String type, double price, double result) {
        this.distance = distance;
        this.efficiency = efficiency;
        this.type = type;
        this.price = price;
        this.result = result;
    }

    public double getDistance() {
        return distance;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public double getPrice() {
        return price;
    }

    public double getResult() {
        return result;
    }

    public String getType() {
        return type;
    }
}
