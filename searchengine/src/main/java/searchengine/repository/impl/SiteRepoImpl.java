package searchengine.repository.impl;

import org.springframework.stereotype.Repository;
import searchengine.config.Site;
import searchengine.model.SiteModel;
import searchengine.repository.SiteRepo;
@Repository
public class SiteRepoImpl implements SiteRepo {

    @Override
    public int save(SiteModel site) {

        return 0;
    }

    public void setSiteRepoImpl(){

    }
}
