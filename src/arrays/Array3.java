package arrays;

/**
 * Created by i.vartanian on 23.09.2014.
 */
public class Array3 {

    public static void main(String[] args) {

//        int x = 0;
//        System.out.println(++x);
//        System.out.println(++x);
//
//        System.out.print(x++ == ++x);

        int[] a  = new int[5];
        int[] b = new int[5];
        System.out.println(a[1]);
        System.arraycopy(a, 0, b, 0, a.length);
        a[1] = 544;
        System.out.println(b[1]);

    }

}
