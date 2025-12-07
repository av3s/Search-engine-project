package searchengine.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "`index`",
        indexes = {
            @Index(name = "index_page", columnList = "page_id"),
            @Index(name = "lemma", columnList = "lemma_id"),
            @Index(name = "index_page_lemma",columnList = "page_id,lemma_id")
        })

@NoArgsConstructor

public class IndexModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", nullable = false)
    private PageModel page;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lemma_id", nullable = false)
    private LemmaModel lemma;

    @Column(name = "`rank`", nullable = false)
    private float rank;
}
