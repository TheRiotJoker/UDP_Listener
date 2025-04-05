package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class UDPListener {
    private final InetAddress sendAddress;
    private final int listeningPort;
    private final int sendingPort;
    private final byte[] buffer = new byte[1];
    private final int operatingMode;

    public UDPListener(int listeningPort, InetAddress sendAddress, int sendPort, int operatingMode) {
        this.sendAddress = sendAddress;
        this.listeningPort = listeningPort;
        this.sendingPort = sendPort;
        this.operatingMode = operatingMode;
    }

    public void startListening() throws IOException {
        DatagramSocket socket = new DatagramSocket(listeningPort);
        while(true) {
            System.out.println("Started listening for packets...");
            DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(incomingPacket);
            System.out.println("Received packet: "+incomingPacket);
            System.out.println("Buffer = "+ Arrays.toString(buffer));
            byte result = switch (operatingMode) {
                case 0 ->
                    //bitshift left (multiplication by 2)
                        (byte) (buffer[0] << 1);
                case 1 ->
                    //bitshift right (division by 2)
                        (byte) (buffer[0] >> 1);
                case 2 ->
                    //addition
                        (byte) (buffer[0] + 1);
                case 3 ->
                    //subtraction
                        (byte) (buffer[0] - 1);
                default -> throw new IllegalArgumentException("FATAL Error: This exception should never hit.");
            };
            System.out.println("Result is: "+ result);
            // do some math on the packet
            byte[] resultArray = {result};
            DatagramPacket outgoingPacket = new DatagramPacket(resultArray, resultArray.length, sendAddress, sendingPort);
            socket.send(outgoingPacket);
        }
    }
}
