package coursework;

import java.io.Serializable;

/**
 * @author Kartik Nair
 */

public class TextProtocol implements Serializable {

    private String header;
    private String message;
    private double distance;
    private double efficiency;
    private String type;
    private double total;

    public TextProtocol(String header) {
        this.header = header;
    }

    public TextProtocol(String header, double distance, double efficiency, String type, double total) {
        this.header = header;
        this.distance = distance;
        this.efficiency = efficiency;
        this.type = type;
        this.total = total;
    }

    public TextProtocol(String header, double distance, double efficiency, String type) {
        this.header = header;
        this.distance = distance;
        this.efficiency = efficiency;
        this.type = type;
    }

    public TextProtocol(String header, String message) {
        this.header = header;
        this.message = message;
    }

    public String getHeader() {
        return this.header;
    }

    public String getMessage() {
        return this.message;
    }

    public double getDistance() {
        return this.distance;
    }

    public double getEfficiency() {
        return this.efficiency;
    }

    public String getType() {
        return this.type;
    }

    public double getTotal() {
        return this.total;
    }
}