package searchengine.dto.statistics;

import lombok.Data;

@Data
public class IndexingControlResponse {
    boolean result;
    String error;
}
