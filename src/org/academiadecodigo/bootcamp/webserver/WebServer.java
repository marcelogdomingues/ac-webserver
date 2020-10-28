package org.academiadecodigo.bootcamp.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class WebServer {

    //Resource path
    private static String path = "src/org/academiadecodigo/bootcamp/webserver/resources/index.html";
    //Socket
    private static Socket clientSocket;
    private static ServerSocket serverSocket;
    //Logger
    private static Logger logger = Logger.getLogger("ErrorLog");

    private static FileHandler fileHandler;
    private static BufferedReader request;

    private static File file;
    private static PrintWriter output;

    public static File getFile() {
        return file;
    }

    public static void setClientSocket(Socket clientSocket) {
        WebServer.clientSocket = clientSocket;
    }

    public static String getPath() {
        return path;
    }

    public static Socket getSocket() {
        return clientSocket;
    }

    public static void sendRequest(BufferedReader request) {

        try {

            logger.info("Sending Request...");

            String getRequest = request.readLine();

            logger.info(getRequest);

        } catch (Exception e) {

            logger.warning("Error in Send Request: " + e.getMessage());

        }

    }

    public static void doResponse() {

        try {

            logger.info("Performing Response ...");

            output = new PrintWriter(getSocket().getOutputStream());
            file = new File(getPath());

        } catch (Exception e) {

            logger.severe(e.getLocalizedMessage());

        }
    }

    public static void sendResponse() {

        doResponse();

        try {

            if (!file.exists()) {

                output.write("HTTP/1.1 404 Not Found\r\n" + "Content-Type: text/html" + "\r\n\r\n");
                logger.info("File does not exist.  Client requesting file at : " + getPath());
                output.close();

            } else {

                output.write("HTTP/1.1 200 OK\r\n" + "Content-Type: text/html" + "\r\n\r\n");
                logger.info(output.toString());
                byte[] fileBytes = Files.readAllBytes(Paths.get(getPath()));
                String fileString = new String(fileBytes, StandardCharsets.UTF_8);
                output.write(fileString);

                logger.info("Finished writing content to output");

                output.close();

            }

        } catch (Exception e) {

            logger.warning("Error in Send Response: " + e.getMessage());

        }


    }

    public static BufferedReader getRequest() {
        return request;
    }

    public static void readRequest(ServerSocket serverSocket) {

        // Accept new client connection
        try {

            logger.info("Reading Request...");

            clientSocket = serverSocket.accept();

            InputStream inputStream = clientSocket.getInputStream();

            request = new BufferedReader(new InputStreamReader(inputStream));


        } catch (Exception e) {

            logger.warning("Error in Read Request: " + e.getMessage());

        }

    }


    public static void setupServer() {

        try {

            logger.info("Setting Up The Server...");

            int port = 8080;

            serverSocket = new ServerSocket(port);

            logger.info("Server Started and listening to the port " + port);

            readRequest(serverSocket);

            sendRequest(getRequest());

            sendResponse();

        } catch (Exception e) {

            logger.warning(e.getMessage());
            System.out.println("Error in Setup Server: " + e.getMessage());

        }

    }

    public static void logFile() {

        try {

            fileHandler = new FileHandler("/Users/codecadet/IdeaProjects/ac-webserver/src/org/academiadecodigo/bootcamp/webserver/errorlogs/ErrorLog.txt");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            logger.info("Error Log");


        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public static void start() {

            setupServer();

    }

    public static void main(String[] args) {

        logFile();
        start();

    }

}
