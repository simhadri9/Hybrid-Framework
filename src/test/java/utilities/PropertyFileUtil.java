package utilities;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyFileUtil {
public static String getValueForKey(String key) throws Throwable
{

	Properties pop = new Properties();
	pop.load(new FileInputStream("PropertyFiles/Environment.properties"));
	return pop.getProperty(key);
}
}
