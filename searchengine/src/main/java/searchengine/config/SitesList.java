package searchengine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("ALL")
@Component
@ConfigurationProperties(prefix = "indexing-settings")
public class SitesList implements Iterable<Site>{
    private List<Site> sites;

    public List<Site> getSites() {
        return this.sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    @Override
    public Iterator<Site> iterator() {
        return sites.iterator();
    }
    public Stream<Site> stream(){
        return sites.stream();
    }
    public Optional<Site> findSiteByUrl(String url){
        return sites.stream()
                .filter(site -> url.equals(site.getUrl()))
                .findFirst();
    }
}
