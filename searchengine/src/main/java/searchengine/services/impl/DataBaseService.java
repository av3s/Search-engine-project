package searchengine.services.impl;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import searchengine.Utils.NetworkWorker;
import searchengine.config.SitesList;
import searchengine.exceptions.ApiException;
import searchengine.exceptions.MessagesAndErrorCodes;
import searchengine.model.PageModel;
import searchengine.model.SiteModel;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

@RequiredArgsConstructor
@Service

public class DataBaseService {
    private final SitesList sites;
    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;
    private final IndexRepository indexRepository;
    private final LemmaRepository lemmaRepository;

    public void clearDataBase(){
        pageRepository.deleteAllInBatch();
        siteRepository.deleteAllInBatch();
        indexRepository.deleteAllInBatch();
        lemmaRepository.deleteAllInBatch();

        System.out.println("Все записи удалены");
    }

    public  SiteModel saveSite(SiteModel siteModel){
        return siteRepository.save(siteModel);
    }

    public PageModel getOrCreatePage(SiteModel site, String path ){
        CRC32 crc = new CRC32();
        String url = site.getUrl()+path;
        System.out.println(url);
        crc.update(path.getBytes(StandardCharsets.UTF_8));

//        try {
//            Document document = NetworkWorker.getDocument();
//            System.out.println(document.title());
//        } catch (IOException e) {
//            throw new ApiException(MessagesAndErrorCodes.CONNECTION_ERROR);
//        }
        if(pageRepository.existsBySiteIdAndPathCRC32(site.getId(), crc.getValue()))
            return pageRepository.findBySiteIdAndPathCRC32(site.getId(), crc.getValue()).get();
         else return pageRepository.save(PageModel.builder()
                 .path(path).site(site).content("none").code(0).build());
    }
    public void savePage(PageModel page){
        pageRepository.save(page);
    }
}
