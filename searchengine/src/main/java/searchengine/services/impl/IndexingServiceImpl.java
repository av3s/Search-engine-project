package searchengine.services.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import searchengine.Utils.LemmaAnalizer.LemmaAnalyzer;
import searchengine.Utils.NetworkWorker;
import searchengine.config.ConnectionConfig;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.ApiResponse;
import searchengine.exceptions.ApiException;
import searchengine.exceptions.MessagesAndErrorCodes;
import searchengine.model.PageModel;
import searchengine.model.SiteModel;
import searchengine.model.Status;
import searchengine.services.interfaces.IndexingService;
import searchengine.Utils.Validation;

import java.io.IOException;
import java.time.LocalDateTime;


@Service
@Getter
@Setter
@RequiredArgsConstructor
public class IndexingServiceImpl implements IndexingService {
    private String url;
    @Autowired
    private final DataBaseService database;
    @Autowired
    private final SitesList sitesList;
    @Autowired
    private final SiteService siteService;
    @Autowired
    private final ConnectionConfig connectionConfig;

    @Override
    public boolean startIndexing() {
        return true;
    }

    @Override
    public boolean stopIndexing() {
        return true;
    }


    @Override
    public ResponseEntity pageIndexing(String url) {

        String domainWithProtocol = NetworkWorker.extractDomain(url);
        Site site = Validation.ValidateDomain(domainWithProtocol, sitesList);
        SiteModel siteModel = siteService.findSiteByUrlIgnoringProtocol(domainWithProtocol)
                .orElse(SiteModel.builder()
                        .url(site.getUrl())
                        .name(site.getName())
                        .statusTime(LocalDateTime.now())
                        .status(Status.INDEXING)
                        .build()
                );

        System.out.println(siteModel.getId());
        if (siteModel.getId() == null) {
            database.saveSite(siteModel);
        }
        String pagePath = NetworkWorker.getPagePath(url);
        Connection connection = NetworkWorker.getConnection(site.getUrl() + pagePath, connectionConfig);
        Connection.Response connectionResponse = NetworkWorker.getConnectionResponse(connection);

        PageModel page = database.getOrCreatePage(siteModel, pagePath);
        page.setCode(connectionResponse.statusCode());

        int codeType = connectionResponse.statusCode() / 100;
        if (codeType == 2) {
            Document document;
            try {
                page.setContent(connectionResponse.parse().html());
                 document =  NetworkWorker.getDocument(connection);
            } catch (IOException e) {
                throw new ApiException(MessagesAndErrorCodes.PAGE_INVALID_CONNECT_TO_URL);
            }
            LemmaAnalyzer analyzer = new LemmaAnalyzer(document);
            analyzer.analyze();
            analyzer.printLemmaStat(20);
        } else {
            page.setContent("none");
            database.savePage(page);
            switch (codeType) {
                case 1 -> throw new ApiException(MessagesAndErrorCodes.PAGE_INFORMATIONAL);
                case 3 -> throw new ApiException(MessagesAndErrorCodes.PAGE_REDIRECTION);
                case 4 -> throw new ApiException(MessagesAndErrorCodes.PAGE_NOT_FOUND);
                case 5 -> throw new ApiException(MessagesAndErrorCodes.INTERNAL_ERROR);

            }

        }
       return  ResponseEntity.ok(ApiResponse.ok());
    }

    @Override
    public boolean isIndexingRunning() {
        return false;
    }


}
