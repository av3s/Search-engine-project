package searchengine.dto.services;

import lombok.Data;
import searchengine.model.Status;
import java.time.LocalDateTime;

@Data
public class SiteDTO {
    private Integer id;
    private String name;
    private String url;
    private Status status;
    private LocalDateTime statusTime;
    private String lastError;
    private int pagesCount;
    private int lemmasCount;

    // Можно добавить дополнительные поля для статистики
    private int uniquePagesCount;
    private int duplicatesCount;
}