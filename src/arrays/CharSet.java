package arrays;

import java.util.Arrays;

/**
 * Created by i.vartanian on 24.09.2014.
 */
public class CharSet {

    public static void main(String[] args) {

        String str = "\u1E8480";
        System.out.println(str);
        byte[] bytes =  stringToBytes(str);
        System.out.println(Arrays.toString(bytes));

        String result = bytesToString(bytes);
        System.out.println(result);

        String str1 = "\u1E8480";
        char[] chars = str1.toCharArray();
        for (char c: chars) {
            System.out.print(c);
        }


    }

    private static String bytesToString(byte[] bytes) {

        if (bytes == null) {
            throw new IllegalArgumentException("The array bytes can't be Null");
        }

        int[] resultInt = new int[bytes.length >> 2];

        int position;
        for (int i = 0; i < resultInt.length; i++) {

            position = i << 2;

            resultInt[i] = ((bytes[position] & 0x000000FF) << 24)
                         + ((bytes[position + 1] & 0x000000FF) << 16)
                         + ((bytes[position + 2] & 0x000000FF) << 8)
                         + ((bytes[position + 3] & 0x000000FF));

        }

        return new String(resultInt, 0, resultInt.length);
    }

    private static byte[] stringToBytes(String str) {

        if (str == null) {
            throw new IllegalArgumentException("The string can't be Null");
        }

        byte[] result = new byte[str.length() << 2];

        int currentChar;
        int position;
        for (int i = 0; i < str.length(); i++) {

            position = i << 2;

            currentChar = str.charAt(i);

            result[position]       = (byte) ((currentChar & 0xFF000000) >> 24);
            result[position + 1]   = (byte) ((currentChar & 0x00FF0000) >> 16);
            result[position + 2]   = (byte) ((currentChar & 0x0000FF00) >> 8);
            result[position + 3]   = (byte) ((currentChar & 0x000000FF));

        }

        return result;
    }

}
