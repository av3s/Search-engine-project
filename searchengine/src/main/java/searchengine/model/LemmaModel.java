package searchengine.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "lemma",
        indexes ={
        @Index(name = "index_site", columnList = "site_id"),
        @Index(name = "index_lemma", columnList = "lemma"),
        @Index(name = "index_frequency", columnList = "frequency DESC")},
        uniqueConstraints = @UniqueConstraint(
                name = "uc_site_lemma",
                columnNames = {"site_id","lemma"}
        ))
@NoArgsConstructor

public class LemmaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    SiteModel site;
    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    String lemma;
    @Column(nullable = false)
    int frequency;

    @OneToMany(mappedBy = "lemma",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<IndexModel> indexes = new ArrayList<>();
}
