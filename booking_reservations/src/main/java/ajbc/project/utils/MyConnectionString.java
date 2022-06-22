package ajbc.project.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.mongodb.ConnectionString;

public class MyConnectionString {

	private static String PROPERTIES_FILE = "connection.properties";

	public static ConnectionString uri() {

		try (FileInputStream fileStream = new FileInputStream(PROPERTIES_FILE);) {
			Properties props = new Properties();
			props.load(fileStream);

			String user = props.getProperty("user");
			String password = props.getProperty("password");
			String cluster = props.getProperty("cluster");
			String serverAddress = props.getProperty("server_address");
			String param1 = props.getProperty("param1");
			String param2 = props.getProperty("param2");

			String uri = "mongodb+srv://%s:%s@%s.%s/?%s&%s".formatted(user, password, cluster, serverAddress, param1,
					param2);

			return new ConnectionString(uri);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
