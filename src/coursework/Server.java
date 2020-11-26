package coursework;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kartik Nair
 */

class Server {

    public static void main(String argv[]) throws IOException, ClassNotFoundException {

        System.out.println("Server Ready!");

        // Create the Server Socket
        ServerSocket welcomeSocket = new ServerSocket(6789);

        while (true) {
            // Create a connection socket by accepting a connection
            Socket connectionSocket = welcomeSocket.accept();

            // Start a new Thread
            new Thread(() -> {
                try {
                    System.out.println("Thread started");

                    // Create a .csv file that's unique to each client using their socket address
                    File clientFile = new File("./" +
                            connectionSocket.getRemoteSocketAddress().toString()
                                    .replaceAll("[.]", "-")
                                    .replaceAll("[:]", "-")
                                    .replaceAll("[/]", "")
                            + ".csv");

                    /* For production usage where each client will have a different IP address
                    String ip = ((((InetSocketAddress) connectionSocket.getRemoteSocketAddress()).getAddress()).toString());
                    File clientFile = new File("./" + ip.replace("/","")
                            .replaceAll("[.]", "-") + ".csv");
                     */


                    if (clientFile.createNewFile()) System.out.println("New client joined. File created.");
                    else System.out.println("Existing client has returned. Using previous file.");

                    // Set connection to true
                    boolean connection = true;

                    // Create Input & Output streams
                    ObjectInputStream inStream = new ObjectInputStream(connectionSocket.getInputStream());
                    ObjectOutputStream outStream = new ObjectOutputStream(connectionSocket.getOutputStream());

                    while (connection) {
                        TextProtocol msg = (TextProtocol) inStream.readObject();
                        switch (msg.getHeader()) {
                            case "getTotal":
                                outStream.writeObject(new TextProtocol(
                                        "total",
                                        Double.toString(calculate(msg.getDistance(), msg.getEfficiency(), msg.getType()))));
                                break;
                            case "getCalculations":
                                outStream.writeObject(new ListProtocol("calculations", getCalculations(clientFile)));
                                break;
                            case "savePrice":
                                savePrice(msg.getDistance(), msg.getEfficiency(), msg.getType(), msg.getTotal(), clientFile);
                                break;
                            case "closeConnection":
                                connectionSocket.close();
                                connection = false;
                                break;
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static void savePrice(double distance, double efficiency, String type, double total, File file) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file.getName(), true));
        writer.println(String.format("%s, %s, %s, %s, %.2f", distance, efficiency, type, getPrice(type), total));
        writer.close();
    }

    public static double calculate(double distance, double efficiency, String type) {
        return distance / efficiency * (getPrice(type) * 3.785);
    }

    public static double getPrice(String type) {
        final double OCTANE_PRICE = 1.03;
        final double DIESEL_PRICE = 1.05;

        return type.equals("98 Octane") ? OCTANE_PRICE : DIESEL_PRICE;
    }

    public static List<Data> getCalculations(File file) throws IOException {
        File dataFile = new File(file.getName());
        dataFile.createNewFile();
        BufferedReader br = new BufferedReader(new FileReader(dataFile));

        List<Data> result = new ArrayList<>();

        String st;
        while ((st = br.readLine()) != null) {
            String[] split = st.split(", ");
            result.add(new Data(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
                    split[2], Double.parseDouble(split[3]), Double.parseDouble(split[4])));
        }
        return result;
    }
}