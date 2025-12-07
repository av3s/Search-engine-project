package searchengine.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

@Data
@Entity
@Table(name = "page", indexes = {
        @Index(name = "index_page_site", columnList = "site_id"),
        @Index(name = "index_page_site_code", columnList = "site_id,code"),
        @Index(name = "index_page_site_path", columnList = "site_id,path_crc32")
})
@NoArgsConstructor
public class PageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private SiteModel site;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String path;

    @Column(name = "path_crc32", nullable = false, insertable = false, updatable = false)
    private int pathCRC32;

    @Column(nullable = false)
    private int code;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "page", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IndexModel> indices = new ArrayList<>();

    public PageModel(SiteModel site, String path, Integer code, String content) {
        this.site = site;
        this.path = path;
        this.code = code;
        this.content = content;
        calculatePathCrc();
    }

    @PrePersist
    @PreUpdate
    private void calculatePathCrc() {
        if (path == null || path.isEmpty()) this.pathCRC32 = 0;
        else {
            CRC32 crc = new CRC32();
            crc.update(path.getBytes(StandardCharsets.UTF_8));
            this.pathCRC32 = (int) crc.getValue();
        }
    }

    public void setPath(String path) {
        this.path = path;
        calculatePathCrc();
    }
}
