package searchengine.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.SiteModel;
import searchengine.repository.PageRepo;
import searchengine.repository.SiteRepo;
import searchengine.repository.impl.SiteRepoImpl;

@Service
public class DatabaseService {
//    private final PageRepo pages;
   private final SiteRepoImpl sites;
   @Autowired
    public DatabaseService(SiteRepoImpl siteRepository/*, PageRepo pageRepository*/){
   //    pages = pageRepository;
       sites = siteRepository;
   }

   public int addSite(SiteModel site){
       return sites.save(site);
   }
}
