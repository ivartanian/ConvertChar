package fileio.workwithchannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by i.vartanian on 29.09.2014.
 */
public class MainIO {

    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(new File("D:\\Temp\\source\\temp.txt"));
        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\Temp\\source\\tempdest.txt"));

        FileChannel channelInput = fileInputStream.getChannel();
        FileChannel channelOutput = fileOutputStream.getChannel();

        //copy with transfer
//        channelOutput.transferFrom(channelInput, 0, channelInput.size());

        //copy with file channel
//        MappedByteBuffer map = channelInput.map(FileChannel.MapMode.READ_ONLY, 0, channelInput.size());
//        channelOutput.write(map);

        //copy with file channel
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect((int)channelInput.size());
//        long n = 0;
//        while (n < channelInput.size()){
//            byteBuffer.clear();
//            int read = channelInput.read(byteBuffer);
//            if (read < 0){
//                break;
//            }
//            byteBuffer.flip();
//            n += channelOutput.write(byteBuffer);
//        }


        channelInput.close();
        channelOutput.close();
        fileInputStream.close();
        fileOutputStream.close();


    }

}
