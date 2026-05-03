package khuend.project.crm;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrmApplication {

	static {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		// Load all environment variables from .env file
		String[] envVars = { "DATABASE_URL", "DATABASE_USERNAME", "DATABASE_PASSWORD", "JWT_SECRET", "JWT_ISSUER",
				"JWT_ACCESS_TOKEN_TTL_MINUTES", "JWT_REFRESH_TOKEN_TTL_DAYS" };
		for (String var : envVars) {
			String value = dotenv.get(var);
			if (value != null) {
				System.setProperty(var, value);
			}
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(CrmApplication.class, args);
	}

}
