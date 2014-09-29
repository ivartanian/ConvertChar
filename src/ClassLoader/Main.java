package classloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by i.vartanian on 25.09.2014.
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {

        String[] str = new String[]{"D:\\Temp"};

        while (true){

            DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(str);

            Class<?> clazz = Class.forName("classloader.Student", true, dynamicClassLoader);
            Object object = clazz.newInstance();

            System.out.println(object);

            new BufferedReader(new InputStreamReader(System.in)).readLine();

        }

    }

}
