package org.example;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainLauncher {
    public static void main(String[] args) {
        System.out.println("PROGRAM START: TCP");
        // listeningPort = 9999
        // address = 127.0.0.1
        // sendPort = 9911
        // operatingMode = 0
        int listeningPort = 0;
        int sendPort = 0;
        int operatingMode = 0;
        InetAddress address = null;
        try {
            listeningPort = Integer.parseInt(System.getenv("LISTENING_PORT"));
            sendPort = Integer.parseInt(System.getenv("SENDING_PORT"));
            operatingMode = Integer.parseInt(System.getenv("OPERATING_MODE"));
            address = InetAddress.getByName(System.getenv("SENDING_ADDRESS"));
            checkPort(listeningPort);
            checkPort(sendPort);
        } catch(NumberFormatException e) {
            System.out.println("Port, listening mode and operating mode have to be integers");
            System.exit(1);
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (UnknownHostException e) {
            System.out.println("The specified sending address is invalid.");
            System.exit(1);
        }

        if(operatingMode < 0 || operatingMode > 4) {
            System.out.println("Operating mode may only be 0, 1, 2 or 3");
            System.exit(1);
        }
        TCPListener listener = new TCPListener(listeningPort, address, sendPort, operatingMode);
        try {
            listener.startListening();
        } catch (IOException e) {
            System.out.println("Unexpected IOException occurred. Message: "+e.getMessage());
            System.out.println("Cause: "+e.getCause());
        }
    }
    private static void checkPort(int port) {
        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Port numbers have to be between 1024 and 65535");
        }
    }
}