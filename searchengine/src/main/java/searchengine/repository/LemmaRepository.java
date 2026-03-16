package searchengine.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.LemmaModel;
import searchengine.model.SiteModel;

import java.util.Optional;
import java.util.Set;

@Repository
public interface LemmaRepository extends JpaRepository<LemmaModel, Integer> {
    Optional<LemmaModel> findBySiteAndLemma(SiteModel site, String lemma);

    Set<LemmaModel> findAllLemmaBySite(SiteModel site);

    Set<LemmaModel> findAllBySite(SiteModel site);

    Integer countBySite(SiteModel site);

}
