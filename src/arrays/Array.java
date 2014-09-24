package arrays;

import java.util.Arrays;

/**
 * Created by i.vartanian on 23.09.2014.
 */
public class Array {

    public static void main(String[] args) {

        byte[] bytes = {1, 2, 3, 4 ,5, 0};
        short[] shorts = {1, 2, 3, 4, 5, 0};
        int[] integer = {1, 2, 3, 4, 5, 0};

        byte a = -62;
        byte b = -126;
        System.out.println(Integer.toBinaryString(a));
        System.out.println(Integer.toBinaryString(b));
        a = (byte) (a^b);
        b = (byte) (a^b);
        a = (byte) (a^b);
        System.out.println(a);
        System.out.println(b);

        String text = "\u0082";
        String key = "hotline";

        byte[] textByte = text.getBytes();
        byte[] keyByte = key.getBytes();

        byte[] result = new byte[textByte.length];

        for (int i = 0; i < textByte.length; i++) {
            result[i] = (byte) (textByte[i] ^ keyByte[1]);
        }

        System.out.println(new String(result));

        byte[] dec = new byte[result.length];
        for (int i = 0; i < result.length; i++) {
            dec[i] = (byte) (result[i] ^ keyByte[1]);
        }

        System.out.println(new String(dec));

    }

}

class Array2{
    public static void main(String[] args) {

        int a = 17;
        int b = 16;

        System.out.println(a^b);
        System.out.println(a%b);

        System.out.println(0.00/0.00);
        System.out.println(1/0.00);

        char c1 = '1';
        char c2 = '\u0082';
        char c3 = 49;
        System.out.println(Character.getNumericValue(c1));
        System.out.println(Character.getNumericValue(c2));
        System.out.println(Character.getNumericValue(c3));
        System.out.println(c1 + c2 + c3);

        for (int i = 1; i < 1000000; i++) {
            if (Character.toChars(i).length > 1) {
                System.out.println(i);
                break;
            }
//            System.out.println("#" + i + " = " + Arrays.toString(Character.toChars(i)) + " ");;
        }

    }
}