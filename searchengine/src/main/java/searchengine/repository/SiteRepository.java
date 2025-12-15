package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.PageModel;
import searchengine.model.SiteModel;
import searchengine.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SiteRepository extends JpaRepository<SiteModel, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE SiteModel s SET s.status = :status, s.statusTime = :statusTime WHERE s.id = :siteId")
    void updateStatus(@Param("siteId") Integer siteId,
                      @Param("status") Status status,
                      @Param("statusTime") LocalDateTime statusTime);

    @Query("SELECT s, " +
            "       (SELECT COUNT(p) FROM PageModel p WHERE p.site = s), " +
            "       (SELECT COUNT(l) FROM LemmaModel l WHERE l.site = s) " +
            "FROM SiteModel s")
    List<Object[]> getSitesStatistics();

    boolean existsByUrl(String url);

    @Modifying
    @Transactional
    @Query("UPDATE SiteModel s SET s.status = :status, s.statusTime = :statusTime, " +
            "s.lastError = :lastError WHERE s.id = :siteId")
    void updateStatusWithError(@Param("siteId") Integer siteId,
                               @Param("status") Status status,
                               @Param("statusTime") LocalDateTime statusTime,
                               @Param("lastError") String lastError);

    List<SiteModel> findByNameContainingIgnoreCase(String namePart);

    @Modifying
    @Transactional
    void deleteByStatus(Status status);


    int countByStatus(Status status);

    @Query("SELECT s FROM SiteModel s WHERE s.status = 'INDEXING' AND s.statusTime < :thresholdTime")
    List<SiteModel> findStuckIndexingSites(@Param("thresholdTime") LocalDateTime thresholdTime);

    @Modifying
    @Transactional
    @Query("UPDATE SiteModel s SET s.status = :status, s.statusTime = :statusTime " +
            "WHERE s.id IN :siteIds")
    void batchUpdateStatus(@Param("siteIds") List<Integer> siteIds,
                           @Param("status") Status status,
                           @Param("statusTime") LocalDateTime statusTime);

    @Query("SELECT DISTINCT s FROM SiteModel s LEFT JOIN FETCH s.pages")
    List<SiteModel> findAllWithPages();

    Optional<SiteModel> findByUrl(String url);




    @Transactional
    @Query("SELECT p FROM PageModel p WHERE p.site.id = :id")
    public List<PageModel> findByIdWithPages(@Param("id") Integer id);

    public default boolean existsByUrlIgnoringCaseAndProtocol() {
        return false;
    }
}

