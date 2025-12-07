package searchengine.repository;

import searchengine.config.Site;
import searchengine.model.SiteModel;

public interface SiteRepo {

    int save(SiteModel site);
}
