package Client;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

/**
 * Created by spiollinux on 06.11.15.
 */
public class TCPComm {

    public static void main(String[] args) {
        TCPComm myComm = new TCPComm();
        Socket server = null;
        try {
            server = myComm.connectToHost("localhost");
        } catch (IOException e) {
            System.err.println("Verbindungsaufbau fehlgeschlagen");
            e.printStackTrace();
        }

        try {
            parseConnection(server);


        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null)
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            ;
        }
    }

    private static void parseConnection(Socket server) throws IOException {
        InputStream in = server.getInputStream();
        int readPos = 0, maxResponseLength = 255;
        byte[] response = new byte[maxResponseLength];
        while (readPos < maxResponseLength) {
            int responseLength = in.read(response, readPos,maxResponseLength - readPos);
            readPos += responseLength;
        }
        //Paket gelesen nach response

        for (int i = 0; i < 3; ++i) {
           if (response[i] != (byte) 0xFF) {
               throw (new RuntimeException("erste Bytes haben falsches Format"));
           }
        }
        int messageLength = response[3];
        for (int i = 4; i < 3 + messageLength; ++i) {
            if (i == 4) {
                //Typangabe
            }
            System.out.println("Byte " + i + ": " + response[5]);
        }

    }


    public Socket connectToHost(String hostname) throws IOException {
       SocketAddress hostAdress = new InetSocketAddress(hostname, 2342);
       Socket socket = new Socket();
       socket.connect(hostAdress,100);
       return socket;
   }
}
