package searchengine.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.query.Page;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Builder
@Entity
@Table(name = "site")


@NoArgsConstructor
@AllArgsConstructor

public class SiteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "status_time", nullable = false)
    private LocalDateTime statusTime;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false, unique = true)
    private String url;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @OneToMany(mappedBy = "site", fetch = FetchType.EAGER)
    private List<PageModel> pages = new ArrayList<>();

    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    private List<LemmaModel> lemmas = new ArrayList<>();
}
