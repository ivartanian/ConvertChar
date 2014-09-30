package fileio.singlethreadwebserver;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by i.vartanian on 30.09.2014.
 */
public class Client implements Runnable {

    private final int port;
    private final String host;

    private final int id;

    private final String pathRequest = "D:\\Temp\\request.txt";

    public Client(int port, String host, int id) {
        this.port = port;
        this.host = host;
        this.id = id;
    }

    public void startClient() throws IOException {

        byte[] request = getRequest();

        byte[] buff = new byte[1024 * 8];

        Socket socket = new Socket(host, port);

        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            bufferedOutputStream.write(request);
            bufferedOutputStream.flush();
            System.err.println("request #" + id + " CLIENT -> SERVER: time:" + System.nanoTime());

//        Thread.sleep(200);

            System.err.println("response #" + id + " CLIENT <- SERVER: time:" + System.nanoTime());
            bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            buff = new byte[socket.getReceiveBufferSize()];
            int read = bufferedInputStream.read(buff);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) bufferedInputStream.close();
                if (bufferedOutputStream != null) bufferedOutputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private byte[] getRequest() throws IOException {

        FileChannel channel = new FileInputStream(new File(pathRequest)).getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());

        byteBuffer.clear();
        channel.read(byteBuffer);

        return byteBuffer.array();

    }

    public static void main(String[] args) {

        long start = System.nanoTime();
        for (int i = 1; i < 100; i++) {
            Client client = new Client(8080, "localhost", i);
            Thread thread = new Thread(client);
            thread.run();
        }
        long end = System.nanoTime();

        System.err.println("run time, ms: " + ((end - start) / 1000000));

    }

    @Override
    public void run() {
        try {
            startClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
