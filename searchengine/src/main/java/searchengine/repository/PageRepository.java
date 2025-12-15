package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.dto.services.PageCrc32CollisionInfo;
import searchengine.model.PageModel;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<PageModel, Integer> {
//    @Query ("SELECT p FROM PageModel p where p.site_id = :site_id")
//    List<PageModel> findBySiteId(@Param("site_id") Integer siteId);


    @Query(value = """
        SELECT IFNULL(SUM(cnt), 0) 
        FROM (
            SELECT COUNT(*) - COUNT(DISTINCT path) as cnt
            FROM page 
            WHERE site_id = :siteId 
            GROUP BY path_crc32 
            HAVING COUNT(*) > COUNT(DISTINCT path)
        ) AS duplicates
        """, nativeQuery = true)
    int countTotalPathDuplicates(@Param("siteId") Integer siteId);
    @Query("SELECT COUNT(DISTINCT p.pathCRC32) FROM PageModel p WHERE p.site.id = :siteId")
    int countDistinctByPathCrc32(@Param("siteId") Integer siteId);

    int countBySiteId(Integer siteId);

    private int countPagesInCollisions(List<PageModel> collisions) {
        return (int)collisions.stream().map(PageModel::getPath).distinct().count();
    }


}
