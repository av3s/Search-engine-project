package searchengine.dto.statistics;

import lombok.Data;
import lombok.NoArgsConstructor;
import searchengine.model.Status;

@Data
@NoArgsConstructor
public class DetailedStatisticsItem {
    private String url;
    private String name;
    private String status;
    private long statusTime;
    private String error;
    private int pages;
    private int lemmas;

    public DetailedStatisticsItem(String url, String name) {
        this.url = url;
        this.name = name;
        this.status = Status.NOT_FOUND.toString();
    }

}

