package fileio.multithreadwebserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by i.vartanian on 30.09.2014.
 */
public class Server implements Callable<Void>{

    private final int port;
    private final String host;
    private Socket socket;
    byte[] response;

    private final String pathResponse = "D:\\Temp\\response.txt";

    public Server(int port, String host) throws IOException {
        this.port = port;
        this.host = host;
        this.response = getResponse();
    }

    public void startServer() throws IOException, InterruptedException {

        System.out.println("multi server started...");

        ServerSocket serverSocket = new ServerSocket(port);

        while (true){

            socket = serverSocket.accept();

            FutureTask task = new FutureTask(this);
            Thread t = new Thread(task);
            t.start();

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

    @Override
    public Void call() throws IOException, InterruptedException, IllegalAccessException, InstantiationException {

        byte[] buff = new byte[1024 * 8];

        System.err.println("request SERVER <- CLIENT: time:" + System.nanoTime());
        BufferedInputStream bufferedInputStream = null;
        bufferedInputStream = new BufferedInputStream(socket.getInputStream());
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

        return Void.TYPE.newInstance();

    }

}
