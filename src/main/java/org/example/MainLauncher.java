package org.example;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainLauncher {
    public static void main(String[] args) {
        // listeningPort = 9999
        // address = 127.0.0.1
        // sendPort = 9911
        // operatingMode = 0
        int listeningPort = 0;
        int sendPort = 0;
        int operatingMode = 0;
        InetAddress address = null;
        try {
            listeningPort = Integer.parseInt(args[0]);
            sendPort = Integer.parseInt(args[2]);
            operatingMode = Integer.parseInt(args[3]);
            address = InetAddress.getByName(args[1]);
            checkPort(listeningPort);
            checkPort(sendPort);
        } catch(NumberFormatException e) {
            System.out.println("Port, listening mode and operating mode have to be integers");
            System.exit(1);
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (UnknownHostException e) {
            System.out.println("The specified address: "+args[1]+" is invalid.");
            System.exit(1);
        }

        if(operatingMode < 0 || operatingMode > 4) {
            System.out.println("Operating mode may only be 0, 1, 2 or 3");
            System.exit(1);
        }
        UDPListener listener = new UDPListener(listeningPort, address, sendPort, operatingMode);
        try {
            listener.startListening();
        } catch (IOException e) {
            System.out.println("Unexpected IOException occurred. Message: "+e.getMessage());
        }
    }
    private static void checkPort(int port) {
        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Port numbers have to be between 1024 and 65535");
        }
    }
}