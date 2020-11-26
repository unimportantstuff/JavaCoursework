package coursework;

import java.util.List;

/**
 * @author Kartik Nair
 */

public class ListProtocol extends TextProtocol {
    private List<Data> calculations;

    ListProtocol(String header, List<Data> list) {
        super(header);
        this.calculations = list;
    }

    public List<Data> getCalculations() {
        return this.calculations;
    }
}
