package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {
    public static String read(String key){
        Properties properties = new Properties();
        try{
            properties.load(new FileInputStream("src\\main\\resources\\resources.properties"));
            return properties.getProperty(key);
        } catch (IOException e){
            System.out.println("The error occurred, don't panic");
            e.printStackTrace();
        }
        return null;
    }
}
