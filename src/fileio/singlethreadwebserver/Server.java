package fileio.singlethreadwebserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by i.vartanian on 30.09.2014.
 */
public class Server {

    private final int port;
    private final String host;

    private final String pathResponse = "D:\\IGOR\\Temp\\response.txt";

    public Server(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void startServer() throws IOException {

        System.out.println("server started...");

        byte[] response = null;
        response = getResponse();

        ServerSocket serverSocket = new ServerSocket(port, 100);

        Socket socket = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {

            while (true) {

                socket = serverSocket.accept();

                byte[] buff = new byte[1024 * 8];

                System.err.println("request SERVER <- CLIENT: time:" + System.nanoTime());
                bufferedInputStream = new BufferedInputStream(socket.getInputStream());
                int receiveBufferSize = socket.getReceiveBufferSize();
                int read = bufferedInputStream.read(buff);

                Thread.sleep(300);

                bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                bufferedOutputStream.write(response);
                bufferedOutputStream.flush();
                System.err.println("response SERVER -> CLIENT: time:" + System.nanoTime());


                if (bufferedInputStream != null) bufferedInputStream.close();
                if (bufferedOutputStream != null) bufferedOutputStream.close();
                if (socket != null) socket.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) bufferedInputStream.close();
                if (bufferedOutputStream != null) bufferedOutputStream.close();
                if (socket != null) socket.close();
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private byte[] getResponse() throws IOException {

        FileChannel channel = new FileInputStream(new File(pathResponse)).getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());

        byteBuffer.clear();
        channel.read(byteBuffer);

        return byteBuffer.array();

    }

    public static void main(String[] args) {

        Server server = new Server(8080, "localhost");
        try {
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
