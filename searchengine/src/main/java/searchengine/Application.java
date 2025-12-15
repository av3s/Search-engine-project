package searchengine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import searchengine.dto.services.Site;
import searchengine.model.SiteModel;
import searchengine.model.Status;
import searchengine.services.SiteService;

import javax.swing.text.Utilities;
import java.text.MessageFormat;

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

   @Bean
   @Transactional
    public CommandLineRunner demo() {
        return args -> {
            SiteModel site = new SiteModel();
            site.setStatus(Status.FAILED);
            site.setName("name");
            site.setUrl("http://name.site");
            //database.addSite(site);
            //dataBase.
            for (Site s : dataBase.getAllSitesWithPagesDTO()) {
                System.out.println(MessageFormat.format("{0}) {1} - {2}\n ",
                        s.getId(),
                        s.getName(),
                        s.getStatus()));
                s.getPageList().forEach(page ->  System.out.println("\t" +  page));
            }
            System.out.println("-------------------\n В базе "+ dataBase.siteCount() + " сайтов");


        };
    }
}

