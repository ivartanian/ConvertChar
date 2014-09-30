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

    private final String pathResponse = "D:\\Temp\\response.txt";

    public Server(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void startServer() throws IOException, InterruptedException {

        System.out.println("server started...");

        byte[] response = getResponse();


        ServerSocket serverSocket = new ServerSocket(port);

        while (true){

            Socket socket = serverSocket.accept();

            byte[] buff = new byte[1024 * 8];

            System.err.println("request SERVER <- CLIENT: time:" + System.nanoTime());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            int receiveBufferSize = socket.getReceiveBufferSize();
            int read = bufferedInputStream.read(buff);

            Thread.sleep(300);

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            bufferedOutputStream.write(response);
            bufferedOutputStream.flush();
            System.err.println("response SERVER -> CLIENT: time:" + System.nanoTime());

            bufferedInputStream.close();
            bufferedOutputStream.close();
            socket.close();

        }

    }

    private byte[] getResponse() throws IOException {

        FileChannel channel = new FileInputStream(new File(pathResponse)).getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());

        byteBuffer.clear();
        channel.read(byteBuffer);

        return byteBuffer.array();

    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = new Server(8080, "localhost");
        server.startServer();

    }


}
