package coderank.impl.staticanalysis;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class Configuration {
    private static String[] packages = new String[0];

    public static void initialize(String propertiesFileName) throws IOException {
        InputStream input = new FileInputStream(propertiesFileName);
        Properties properties = new Properties();
        properties.load(input);

        String packagesList = properties.getProperty("process-packages");
        String deliminator = "[,][ ]";
        packages = packagesList.split(deliminator);
    }

    public static boolean processPackage(String name) {
        for (String token : packages) {
            if (name.startsWith(token) || name.startsWith(token.replace('/', '.'))) {
                return true;
            }
        }
        return false;
    }

}
