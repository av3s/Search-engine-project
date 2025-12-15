package searchengine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.IndexingControlResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.StatisticsService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StatisticsService statisticsService;

    public ApiController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<IndexingControlResponse> startIndexing(){
        IndexingControlResponse startIndexingResponse = new IndexingControlResponse();
        startIndexingResponse.setResult(true);
        return ResponseEntity.ok(startIndexingResponse);
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<IndexingControlResponse> stopIndexing(){
        IndexingControlResponse stopIndexingResponse = new IndexingControlResponse();
        stopIndexingResponse.setResult(true);
        return ResponseEntity.ok(stopIndexingResponse);
    }
}
