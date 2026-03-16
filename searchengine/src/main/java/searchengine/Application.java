package searchengine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import searchengine.services.impl.SiteService;

@SpringBootApplication
@EntityScan("searchengine.model")
public class Application {
    final SiteService dataBase;
    @Autowired
    public Application(SiteService siteService ){
        dataBase = siteService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }
//


//   @Bean
//   @Transactional
//    public CommandLineRunner demo() {
//        return args -> {
//            dataBase.deleteAllDataBaseRecords();
//
//        };
//  }
}

