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
public class Server {

    private final int port;
    private final String host;
    byte[] response;

    private final String pathResponse = "D:\\IGOR\\Temp\\response.txt";

    public Server(int port, String host) {
        this.port = port;
        this.host = host;
        this.response = getResponse();
    }

    public void startServer() throws IOException {

        System.out.println("multi server started...");

        ServerSocket serverSocket = new ServerSocket(port, 100);

        try {
            while (true) {

                Socket socket = serverSocket.accept();

                ServerWorker serverWorker = new ServerWorker(socket);
                Thread thread = new Thread(serverWorker);
                thread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getResponse() {

        FileChannel channel = null;
        ByteBuffer byteBuffer = null;
        try {
            channel = new FileInputStream(new File(pathResponse)).getChannel();
            byteBuffer = ByteBuffer.allocate((int) channel.size());

            byteBuffer.clear();

            channel.read(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

    public class ServerWorker implements Runnable{

        private final Socket socket;

        public ServerWorker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            byte[] buff = new byte[1024 * 8];

            BufferedInputStream bufferedInputStream = null;
            BufferedOutputStream bufferedOutputStream = null;

            System.err.println("request SERVER <- CLIENT: time:" + System.nanoTime());
            try {
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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (bufferedInputStream != null) bufferedInputStream.close();
                    if (bufferedOutputStream != null) bufferedOutputStream.close();
                    if (socket != null) socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
