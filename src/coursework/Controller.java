package coursework;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * @author Kartik Nair
 */

public class Controller {
    @FXML private Text result;
    @FXML private TextField distance;
    @FXML private TextField fuelEfficiency;
    @FXML private ChoiceBox fuelType;

    // Make socket that will be used for all requests
    private static Socket clientSocket;
    private static ObjectOutputStream outStream;
    private static ObjectInputStream inStream;

    {
        try {
            // Set the previous socket variables
            clientSocket = new Socket("localhost", 6789);
            outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) throws Exception {
        try {
            // Calculate the total
            double total = getPrice(Double.parseDouble(distance.getText()),
                    Double.parseDouble(fuelEfficiency.getText()), (String) fuelType.getValue());

            // Show the user the total
            result.setText(String.format("The total cost will be: %.2f", total));
            // Call savePrice() to save the price
            savePrice(distance.getText(), fuelEfficiency.getText(), (String) fuelType.getValue(), total);
        } catch (NumberFormatException e) {
            // If there are issues in input, tell the user
            result.setText(String.format("Your values seem wrong. Please try again."));
        }
    }

    @FXML
    protected void getPreviousCalculations(ActionEvent e) throws Exception {
        // Create pane for new window
        StackPane secondaryLayout = new StackPane();
        // Create scene for new window
        Scene secondScene = new Scene(secondaryLayout, 400, 400);

        // Initialize the List that will hold all of our data
        List<Data> result = getCalculations();

        // Create the table for our data
        TableView table = new TableView();

        // Tell the tables what columns are there and how to access them
        TableColumn<String, Data> column1 = new TableColumn<>("Distance");
        column1.setCellValueFactory(new PropertyValueFactory<>("distance"));

        TableColumn<String, Data> column2 = new TableColumn<>("Efficiency");
        column2.setCellValueFactory(new PropertyValueFactory<>("efficiency"));

        TableColumn<String, Data> column3 = new TableColumn<>("Fuel Type");
        column3.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<String, Data> column4 = new TableColumn<>("Fuel Price");
        column4.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<String, Data> column5 = new TableColumn<>("Total Cost");
        column5.setCellValueFactory(new PropertyValueFactory<>("result"));

        // Add all the columns to the table
        table.getColumns().addAll(column1, column2, column3, column4, column5);
        table.getItems().addAll(result);

        // Add the table to the pane
        secondaryLayout.getChildren().add(table);

        // Create the new window
        Stage newWindow = new Stage();
        newWindow.setTitle("Calculations");
        newWindow.setScene(secondScene);
        newWindow.show();
    }
    public static void savePrice(String distance, String efficiency, String type, double total)
            throws IOException {
        // Tell the server to save price & provide the values
        outStream.writeObject(new TextProtocol("savePrice", Double.parseDouble(distance), Double.parseDouble(efficiency), type, total));
    }

    public static double getPrice(double distance, double efficiency, String type) throws IOException, ClassNotFoundException {
        // Request price from server
        outStream.writeObject(new TextProtocol("getTotal", distance, efficiency, type));
        // Read the reply
        TextProtocol serverMessage = (TextProtocol) inStream.readObject();
        // Get the double value from reply & return it
        double price = Double.parseDouble(serverMessage.getMessage());
        return price;
    }

    public static List<Data> getCalculations() throws IOException, ClassNotFoundException {
        // Make request to server for previous calculations
        outStream.writeObject(new TextProtocol("getCalculations"));
        // Read server's reply & return only the List
        ListProtocol serverMessage = (ListProtocol) inStream.readObject();
        return serverMessage.getCalculations();
    }

    public static void handleClose() throws IOException {
        // Ask server to close the connection
        outStream.writeObject(new TextProtocol("closeConnection"));
        // Close the client's socket as well
        clientSocket.close();
    }
}
