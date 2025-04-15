package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPListener {
    private final InetAddress sendAddress;
    private final int listeningPort;
    private final int sendPort;
    private final int operatingMode;

    public TCPListener(int listeningPort, InetAddress sendAddress, int sendPort, int operatingMode) {
        this.sendAddress = sendAddress;
        this.listeningPort = listeningPort;
        this.sendPort = sendPort;
        this.operatingMode = operatingMode;
    }

    public void startListening() throws IOException {
        try (ServerSocket socket = new ServerSocket(listeningPort)) {
            System.out.println("Waiting for a connection.");
            Socket client = socket.accept();
            System.out.println("Success! Incoming connection from: "+client.getInetAddress());
            InputStream in = client.getInputStream();
            int bytesRead;

            byte[] buffer = new byte[1024];
            System.out.println("Attempting to connect to receiver.");
            Socket receiverSocket = new Socket(sendAddress, sendPort);
            System.out.println("Success! Connected to receiver: "+receiverSocket);
            // Read until end of stream
            while ((bytesRead = in.read(buffer)) != -1) {
                byte[] resultBytes = new byte[bytesRead];
                for(int i = 0; i < bytesRead; i++) {
                    resultBytes[i] = switch (operatingMode) {
                        case 0 ->
                            //bitshift left (multiplication by 2)
                                (byte) (buffer[i] << 1);
                        case 1 ->
                            //bitshift right (division by 2)
                                (byte) (buffer[i] >> 1);
                        case 2 ->
                            //addition
                                (byte) (buffer[i] + ((byte)1));
                        case 3 ->
                            //subtraction
                                (byte) (buffer[i] - ((byte)1));
                        default -> throw new IllegalArgumentException("FATAL Error: This exception should never hit.");
                    };
                }
                receiverSocket.getOutputStream().write(resultBytes);
                receiverSocket.getOutputStream().flush();
            }
            receiverSocket.close();
        }
    }
}
