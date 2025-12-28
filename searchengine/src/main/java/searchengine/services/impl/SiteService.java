package searchengine.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import searchengine.dto.services.Site;
import searchengine.model.PageModel;
import searchengine.model.SiteModel;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class SiteService {
    @Autowired
    private final SiteRepository siteRepository;
    @Autowired
    private final PageRepository pageRepository;

    public long siteCount() {
        return siteRepository.count();
    }

    public List<SiteModel> getAll() {
        return siteRepository.findAll();
    }

    @Transactional
    public List<Site> getAllSitesWithPagesDTO() {
        return siteRepository.findAll().stream().map(siteModel -> {
            List<String> pages = siteRepository.findByIdWithPages(siteModel.getId()).stream()
                    .map(PageModel::getPath)
                    .toList();
            return new Site(siteModel.getId(), siteModel.getName(), pages, siteModel.getStatus());
        }).toList();
    }

    public long countAllUniquePages(){
        AtomicLong countUniquePages = new AtomicLong();
        siteRepository.findAll().forEach(siteModel -> {
            countUniquePages.addAndGet(countUniquePages(siteModel.getId()));
        });
        return countUniquePages.get();
    }

    public int countUniquePages(Integer siteId) {
        int uniqueCrc32count = pageRepository.countDistinctByPathCrc32(siteId);
        int totalPages = pageRepository.countBySiteId(siteId);
        if (totalPages == uniqueCrc32count) {
            return uniqueCrc32count;
        } else {
            return totalPages - pageRepository.countTotalPathDuplicates(siteId);
        }
    }

    Optional<SiteModel> findByUrlIgnoringProtocol(String url) {
        final String regEx = "^https?://";
        String normalizedUrl = url.replaceFirst(regEx, "");
        return siteRepository.findAll().stream()
                .filter(site -> site.getUrl().replaceFirst(regEx, "").equalsIgnoreCase(normalizedUrl))
                .findFirst();
    }
}
