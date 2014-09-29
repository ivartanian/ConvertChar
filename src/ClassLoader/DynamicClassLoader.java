package classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by i.vartanian on 25.09.2014.
 */
public class DynamicClassLoader extends ClassLoader {

    private final String[] classPath;
    private Map<String, Class> classesHash = new HashMap<>();

    public DynamicClassLoader(String[] classPath) {
        this.classPath = classPath;
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        Class result = findClass(name);

        if (resolve) {
            resolveClass(result);
        }

        return result;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        Class result = classesHash.get(name);

        if (result != null) {

            System.out.println("% Class " + name + " found in cache");

            return result;

        }

        File file = findFile(name.replace('.', '/'), ".class");

        System.out.println("% Class " + name + (file == null ? "" : " found in " + file));


        if (file == null) {

            return findSystemClass(name);

        }

        try {

            byte[] classBytes = loadFileAsBytes(file);

            result = defineClass(name, classBytes, 0, classBytes.length);

        } catch (IOException e) {

            throw new ClassNotFoundException("Cannot load class " + name + ": " + e);

        } catch (ClassFormatError e) {

            throw new ClassNotFoundException("Format of class file incorrect for class " + name + ": " + e);

        }

        classesHash.put(name, result);

        return result;

    }

    private File findFile(String name, String extension) {

        for (int k = 0; k < classPath.length; k++) {

            File f = new File((new File(classPath[k])).getPath() + File.separatorChar + name.replace('/', File.separatorChar) + extension);

            if (f.exists()) return f;

        }

        return null;

    }

    public static byte[] loadFileAsBytes(File file) throws IOException {

        byte[] result = new byte[(int) file.length()];

        FileInputStream f = new FileInputStream(file);

        try {

            f.read(result, 0, result.length);

        } finally {

            try {

                f.close();

            } catch (Exception e) {

                e.getStackTrace();

            }

        }

        return result;

    }

}
