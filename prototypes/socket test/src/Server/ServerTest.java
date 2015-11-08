package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by spiollinux on 06.11.15.
 */
public class ServerTest {

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(2342);
        } catch (IOException e) {
            System.err.println("Port bereits belegt");
            System.exit(-1);
        }

        while (true) {
            Socket client = null;

            try {
                client = server.accept();
                System.out.println("Incoming connection: accepted");
                handleConnection(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (client != null)
                    try { client.close(); } catch (IOException e) {}
            }
        }
    }

    private static void handleConnection(Socket client) throws IOException {
        InputStream in = client.getInputStream();
        OutputStream out = client.getOutputStream();
        byte[] message = new byte[255];
        message[0] = (byte) 0xFF;
        message[1] = (byte) 0xFF;
        message[2] = (byte) 0xFF;
        message[3] = (byte) 2; //Länge
        message[4] = (byte) 1; //Typ
        message[5] = (byte) 42;

        for (int i = 6; i < 255; ++i) {
            message[i] = (byte) 0x00;
        }

        out.write(message);
        out.flush();
    }
}
