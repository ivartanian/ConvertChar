package fileio.channelwebserver;

import arrays.CharSet;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by i.vartanian on 30.09.2014.
 */
public class Client implements Runnable {

    private final int port;
    private final String host;

    private final int id;

    private final String pathRequest = "D:\\IGOR\\Temp\\request.txt";
    CharBuffer charBuffer;
    Charset charset = Charset.defaultCharset();
    CharsetDecoder decoder = charset.newDecoder();

    public Client(int port, String host, int id) {
        this.port = port;
        this.host = host;
        this.id = id;
    }

    public void startClient() throws IOException {

        byte[] request = getRequest();

        byte[] buff = new byte[1024 * 8];
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 8);

        try (Selector selector = Selector.open();
             SocketChannel socketChannel = SocketChannel.open()) {

            if (!(socketChannel.isOpen() && selector.isOpen())) {
                System.out.println("The socket channel or selector can't opened!");
                return;
            }

            socketChannel.configureBlocking(false);
            socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
            socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
            socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            socketChannel.connect(new InetSocketAddress(host, port));

            int pos = 0;
            while (selector.select(1000) > 0) {

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {

                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    try (SocketChannel keySocketChannel = (SocketChannel) selectionKey.channel()) {

                        if (!selectionKey.isConnectable()) {
                            continue;
                        }

                        if (keySocketChannel.isConnectionPending()) {
                            keySocketChannel.finishConnect();
                        }


                        while ((pos = keySocketChannel.read(byteBuffer)) != -1) {

                            System.out.println(new String(byteBuffer.array(), 0, pos));

                            if (byteBuffer.hasRemaining()) {
                                byteBuffer.compact();
                            } else {
                                byteBuffer.clear();
                            }

                            int r = new Random().nextInt(100);
                            if (r == 50) {
                                System.out.println("50 was generated! Close the socket channel!");
                                break;
                            } else {
                                keySocketChannel.write(ByteBuffer.wrap(request));
                            }

                        }

                    }

                }

            }

        } catch (IOException e) {
            e.getStackTrace();
        }

    }

    private byte[] getRequest() throws IOException {

        FileChannel channel = new FileInputStream(new File(pathRequest)).getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());

        byteBuffer.clear();
        channel.read(byteBuffer);

        return byteBuffer.array();

    }

    public static void main(String[] args) throws InterruptedException {

        long start = System.nanoTime();
        for (int i = 1; i < 50; i++) {
            Client client = new Client(8080, "localhost", i);
            Thread thread = new Thread(client);
            thread.start();
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
