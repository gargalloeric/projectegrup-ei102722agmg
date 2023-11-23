package es.uji.ei1027.clubesportiu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.logging.Logger;

@SpringBootApplication
public class ClubesportiuApplication {

	private static final Logger log = Logger.getLogger(ClubesportiuApplication.class.getName());
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(ClubesportiuApplication.class, args);
	}

}
