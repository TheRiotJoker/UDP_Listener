package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class UDPListener {
    private final InetAddress sendAddress;
    private final int listeningPort;
    private final int sendPort;
    private final byte[] buffer = new byte[256];
    private final int operatingMode;

    public UDPListener(int listeningPort, InetAddress sendAddress, int sendPort, int operatingMode) {
        this.sendAddress = sendAddress;
        this.listeningPort = listeningPort;
        this.sendPort = sendPort;
        this.operatingMode = operatingMode;
    }

    public void startListening() throws IOException {
        try (DatagramSocket socket = new DatagramSocket(listeningPort)) {
            System.out.println("Started listening for packets...");
            while (true) {
                DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(incomingPacket);
                System.out.println("Received packet of size: " + incomingPacket.getLength());
                System.out.println("From: "+incomingPacket.getAddress());
                byte[] resultBytes = new byte[incomingPacket.getLength()];
                for(int i = 0; i < incomingPacket.getLength(); i++) {
                    resultBytes[i] = switch (operatingMode) {
                        case 0 -> (byte) (buffer[i] << 1); //bitshift to the left (*2)
                        case 1 -> (byte) (buffer[i] >> 1); //bitshift to the right (/2)
                        case 2 -> (byte) (buffer[i] + ((byte)1)); //addition
                        case 3 -> (byte) (buffer[i] - ((byte)1)); //subtraction
                        default -> throw new IllegalArgumentException("FATAL Error: This exception should never hit.");
                    };
                }
                System.out.println("Forwarding result = "+Arrays.toString(resultBytes));
                System.out.println("To address: "+sendAddress + ":"+ sendPort);
                DatagramPacket outgoingPacket = new DatagramPacket(resultBytes, resultBytes.length, sendAddress, sendPort);
                socket.send(outgoingPacket);
            }
        }
    }
}
