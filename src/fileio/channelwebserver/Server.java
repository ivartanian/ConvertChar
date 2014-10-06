package fileio.channelwebserver;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

/**
 * Created by i.vartanian on 30.09.2014.
 */
public class Server {

    private final int port;
    private final String host;

    private final String pathResponse = "D:\\IGOR\\Temp\\response.txt";

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();

    public Server(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void startServer() throws IOException {

        System.out.println("channel server started...");

        byte[] response = null;
        response = getResponse();

        try (ServerSocketChannel ssc = ServerSocketChannel.open();
             Selector selector = Selector.open()) {

            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(host, port), 100);
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {

                int select = selector.select();
                if (select == 0) {
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {

                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        accept(key, selector);
                    } else if (key.isReadable()) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    }

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void accept(SelectionKey key, Selector selector) {

        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = null;
        try {

            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            keepDataTrack.put(socketChannel, new ArrayList<byte[]>());
            socketChannel.register(selector, SelectionKey.OP_READ);

        } catch (IOException e) {
            e.printStackTrace();
            try {
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private void read(SelectionKey key) throws IOException {

        SocketChannel socketChannel = (SocketChannel) key.channel();
        byteBuffer.clear();
        int read = -1;
        try {
            read = socketChannel.read(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            key.cancel();
            socketChannel.close();
        }

        if (read == -1) {
            keepDataTrack.remove(socketChannel);
            socketChannel.close();
            key.cancel();
            return;
        }

        byte[] bytes = new byte[read];
        System.arraycopy(byteBuffer.array(), 0, bytes, 0, read);

        List<byte[]> channelData = keepDataTrack.get(socketChannel);
        channelData.add(bytes);

        key.interestOps(SelectionKey.OP_WRITE);

    }

    private void write(SelectionKey key) throws IOException {

        SocketChannel socketChannel = (SocketChannel) key.channel();

        List<byte[]> bytes = keepDataTrack.get(socketChannel);

        Iterator<byte[]> iterator = bytes.iterator();
        while (iterator.hasNext()){
            byte[] data = iterator.next();
            iterator.remove();
            socketChannel.write(ByteBuffer.wrap(data));
        }

        key.interestOps(SelectionKey.OP_READ);

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
