package searchengine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.ApiResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.impl.IndexingServiceImpl;
import searchengine.services.interfaces.StatisticsService;

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

//    @GetMapping("/startIndexing")
//    public ResponseEntity<> startIndexing(){
//        return ResponseEntity.ok(startIndexingResponse);
//    }
//
//    @GetMapping("/stopIndexing")
//    public ResponseEntity<IndexingControlResponse> stopIndexing(){
//        IndexingControlResponse stopIndexingResponse = new IndexingControlResponse();
//        stopIndexingResponse.setResult(true);
//        return ResponseEntity.ok(stopIndexingResponse);
//    }

    @PostMapping("/indexPage")
    public ResponseEntity<ApiResponse<Void>> indexPage(String url){
        IndexingServiceImpl indexingService = new IndexingServiceImpl(url);
        return  indexingService.pageIndexing();
      // if (indexingService.pageIndexing()) return ResponseEntity.badRequest().body("{'result': false, 'error': \"sdsdsds\"}");
       //return ResponseEntity.ok().build();
    }
}
