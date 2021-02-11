package memda.episen.dataaccesslayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "memda.episen")
@EntityScan(basePackages = "memda.episen")
public class DataAccessLayerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataAccessLayerApplication.class, args);
	}

}
