package searchengine.services.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import searchengine.dto.ApiResponse;
import searchengine.exceptions.ApiException;
import searchengine.exceptions.MessagesAndErrorCodes;
import searchengine.services.interfaces.IndexingService;

@Service
@Getter
@Setter
@NoArgsConstructor
public class IndexingServiceImpl implements IndexingService {
    String url;
    public IndexingServiceImpl(String url) {
        this.url = url;
    }


    public String getUrl() {
        return this.url;
    }

    @Override
    public  boolean startIndexing() {
        return true;
    }

    @Override
    public boolean stopIndexing() {
        return true;
    }

    @Override
    public ResponseEntity pageIndexing() {
        if (true)
            return ResponseEntity.ok().body(ApiResponse.success());
        else
         throw new ApiException(MessagesAndErrorCodes.PAGE_OUTSIDE_SITES);
    }

    @Override
    public boolean checkingUrlFormat() {
        return false;
    }

    @Override
    public boolean isIndexingRunning() {
        return false;
    }
}
