package be.thomasmore.party;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*
@EnableJpaRepositories("be.thomasmore.party.repositories")
@EntityScan("be.thomasmore.party.model")
*/
@SpringBootApplication
public class PartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartyApplication.class, args);
    }

}
