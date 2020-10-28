package org.academiadecodigo.bootcamp.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        logger.info("Getting File [ " + file + " ]" + "\n");
        return file;
    }

    public static void setClientSocket(Socket clientSocket) {
        WebServer.clientSocket = clientSocket;
    }

    public static String getPath() {
        logger.info("Getting Path [ " + path + " ]" + "\n");
        return path;
    }

    public static Socket getSocket() {
        return clientSocket;
    }

    public static void sendRequest(BufferedReader request) {

        try {

            logger.info("Sending Request..." + "\n");

            String getRequest = request.readLine();

            String[] reqArray = getRequest.split(" ");

            for(int i = 0; i < reqArray.length; i++){

                System.out.println(reqArray[i]);
            }


            String filePath = reqArray[1].substring(1);

            if(filePath == null){

                logger.severe("ERROR! FILEPATH NULL");

            }

            logger.info(getRequest);

        } catch (Exception e) {

            logger.severe("Error in Send Request: " + e.getMessage());

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
                logger.warning("File does not exist.  Client requesting file at : " + getPath() + "\n");
                output.close();

            } else {

                output.write("HTTP/1.1 200 OK\r\n" + "Content-Type: text/html" + "\r\n\r\n");
                logger.info(output.toString());
                byte[] fileBytes = Files.readAllBytes(Paths.get(getPath()));
                String fileString = new String(fileBytes, StandardCharsets.UTF_8);
                output.write(fileString);

                logger.info("Finished writing content to output" + "\n");

                output.close();

            }

        } catch (Exception e) {

            logger.severe("Error in Send Response: " + e.getMessage() + "\n");

        }


    }

    public static BufferedReader getRequest() {
        return request;
    }

    public static void readRequest(ServerSocket serverSocket) {

        // Accept new client connection

        try {

            logger.info("Reading Request..." + "\n");

            clientSocket = serverSocket.accept();

            InputStream inputStream = clientSocket.getInputStream();

            request = new BufferedReader(new InputStreamReader(inputStream));


        } catch (Exception e) {

            logger.warning("Error in Read Request: " + e.getMessage() + "\n");

        }

    }


    public static void setupServer() {

        try {

            logger.info("Setting Up The Server..." + "\n");

            int port = 8080;

            serverSocket = new ServerSocket(port);

            logger.info("Server Started and listening to the port " + port + "\n");

            while (true) {

                readRequest(serverSocket);

                sendRequest(getRequest());

                sendResponse();

            }

        } catch (Exception e) {

            logger.warning(e.getMessage());
            System.out.println("Error in Setup Server: " + e.getMessage() + "\n");

        }

    }

    public static void logFile() {

        try {

            //Current Date and Time
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

            fileHandler = new FileHandler("/Users/codecadet/IdeaProjects/ac-webserver/src/org/academiadecodigo/bootcamp/webserver/errorlogs/ErrorLog_" + timeStamp + ".txt");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            logger.info("Error Log : " + timeStamp + "\n");


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
