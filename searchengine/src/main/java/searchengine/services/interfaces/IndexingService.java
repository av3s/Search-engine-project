package searchengine.services.interfaces;

import org.springframework.http.ResponseEntity;

public interface IndexingService {

    boolean startIndexing();

    boolean stopIndexing();

    ResponseEntity pageIndexing();

    boolean checkingUrlFormat();

    boolean isIndexingRunning();


}
