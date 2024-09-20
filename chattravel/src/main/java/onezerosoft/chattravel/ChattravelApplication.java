package onezerosoft.chattravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChattravelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChattravelApplication.class, args);
	}

}
