package org.academiadecodigo.bootcamp.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class WebServer {

    //Resource path
    private static String path;
    //Socket
    private static Socket socket;
    private static ServerSocket serverSocket;
    //Logger
    private static Logger logger = Logger.getLogger("ErrorLog");

    private static FileHandler fileHandler;
    private static BufferedReader request;


    public static String getPath() {
        return path;
    }

    public static void doRequest() {

    }

    public static void sendRequest(BufferedReader request) {

        try {

            String getRequest = request.readLine();
            String[] reqArray = getRequest.split(" ");


        } catch (Exception e) {

            logger.warning(e.getMessage());

        }

    }

    public static void do

    public static BufferedReader getRequest() {
        return request;
    }

    public static void readRequest(ServerSocket serverSocket) {

        // Accept new client connection
        try {

            Socket clientSocket = serverSocket.accept();

            InputStream inputStream = clientSocket.getInputStream();

            request = new BufferedReader(new InputStreamReader(inputStream));


        } catch (Exception e) {

            logger.warning(e.getMessage());

        }

    }

    public static void responseWaiter() {

    }

    public static void setupServer() {

        try {

            logger.info("Setup Server");

            int port = 8080;

            serverSocket = new ServerSocket(port);

            System.out.println("Server Started and listening to the port " + port);
            logger.info("Server Started and listening to the port " + port);

            readRequest(serverSocket);

            sendRequest(getRequest());

        } catch (Exception e) {

            logger.warning(e.getMessage());
            System.out.println("Error: " + e.getMessage());

        }

    }

    public static void logFile() {

        try {

            fileHandler = new FileHandler("/Users/codecadet/IdeaProjects/ac-webserver/src/org/academiadecodigo/bootcamp/webserver/errorlogs/ErrorLog.txt");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            logger.info("Error Log");


        } catch (SecurityException e) {

            e.printStackTrace();

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
