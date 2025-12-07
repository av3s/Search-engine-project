package searchengine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import searchengine.model.SiteModel;
import searchengine.model.Status;
import searchengine.services.DatabaseService;

@SpringBootApplication
@EntityScan("searchengine.model")
public class Application {
    final DatabaseService dataBase;
    @Autowired
    public Application(DatabaseService databaseService){
        dataBase = databaseService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

   @Bean
    public CommandLineRunner demo() {
        return args -> {
            SiteModel site = new SiteModel();
            site.setStatus(Status.FAILED);
            site.setName("name");
            site.setUrl("http://name.site");
            //database.addSite(site);
            //dataBase.
            System.out.println("добавлено " + dataBase.addSite(site));
        };
    }
}

