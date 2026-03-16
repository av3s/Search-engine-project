package searchengine.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import searchengine.Utils.NetworkWorker;
import searchengine.dto.services.SiteDTO;
import searchengine.exceptions.ApiException;
import searchengine.exceptions.MessagesAndErrorCodes;
import searchengine.model.PageModel;
import searchengine.model.SiteModel;
import searchengine.model.Status;
import searchengine.repository.SiteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SiteService {

    private final SiteRepository siteRepository;
    @Autowired
    private final DataBaseService dataBaseService;

    // CREATE
    @Transactional
    public SiteModel create(SiteModel site) {
        if (site == null) {
            throw new IllegalArgumentException("Site cannot be null");
        }

        // Проверяем уникальность URL
        if (siteRepository.existsByUrl(site.getUrl())) {                            // заменить на возврат ID? обновить?
            throw new ApiException(MessagesAndErrorCodes.SITE_ALREADY_EXISTS,
                    "Site with URL " + site.getUrl() + " already exists");
        }

        // Устанавливаем значения по умолчанию
        if (site.getStatus() == null) {
            site.setStatus(Status.INDEXING);
        }
        if (site.getStatusTime() == null) {
            site.setStatusTime(LocalDateTime.now());
        }

        return siteRepository.save(site);
    }

    @Transactional
    public SiteModel createFromDTO(SiteDTO siteDTO) {
        SiteModel site = new SiteModel();
        site.setName(siteDTO.getName());
        site.setUrl(siteDTO.getUrl());
        site.setStatus(Status.INDEXING);
        site.setStatusTime(LocalDateTime.now());
        return create(site);
    }

    // READ
    public SiteModel getSiteById(Integer id) {
        return siteRepository.findById(id).orElseThrow(() -> new ApiException(MessagesAndErrorCodes.SITE_NOT_FOUND,
                "Site with ID " + id + " not found"));
    }


    public List<SiteModel> findAll() {
        return siteRepository.findAll();
    }

    public List<SiteModel> findAllWithPages() {
        return siteRepository.findAllWithPages();
    }

    public Page<SiteModel> findAll(Pageable pageable) {
        return siteRepository.findAll(pageable);
    }

    public Optional<SiteModel> findByUrl(String url) {
        return siteRepository.findByUrl(url);
    }

    public Optional<SiteModel> findSiteByUrlIgnoringProtocol(String url) {

        String siteDonainWithoutProtocol = NetworkWorker.removeProtocol(NetworkWorker.extractDomain(url));
        Optional<SiteModel> optional = siteRepository.findAll().stream()
                .filter(site -> site.getUrl()
                        .replaceFirst(NetworkWorker.regExProtocol, "")
                        .equalsIgnoreCase(siteDonainWithoutProtocol))
                .findFirst();
        return optional;
    }
//    public Optional<PageModel> findPageBySiteAndUrl{
//
//    }

    public List<SiteModel> findByNameContaining(String namePart) {
        return siteRepository.findByNameContainingIgnoreCase(namePart);
    }

    public boolean existsByUrl(String url) {
        return siteRepository.existsByUrl(url);
    }

    public long count() {
        return siteRepository.count();
    }

    public long countByStatus(Status status) {
        return siteRepository.countByStatus(status);
    }

    public List<SiteModel> findStuckIndexingSites(LocalDateTime thresholdTime) {
        return siteRepository.findStuckIndexingSites(thresholdTime);
    }

    // UPDATE
    @Transactional
    public SiteModel update(SiteModel site) {
        if (site == null || site.getId() == null) {
            throw new IllegalArgumentException("Site or site ID cannot be null");
        }

        // Проверяем существование сайта
        SiteModel existingSite = siteRepository.getById(site.getId());

        // Обновляем поля
        if (site.getName() != null) {
            existingSite.setName(site.getName());
        }
        if (site.getUrl() != null && !site.getUrl().equals(existingSite.getUrl())) {
            // Проверяем уникальность нового URL
            if (siteRepository.existsByUrl(site.getUrl())) {
                //         siteRepository.findByUrl();
            }
            existingSite.setUrl(site.getUrl());
        }
        if (site.getStatus() != null) {
            existingSite.setStatus(site.getStatus());
        }
        if (site.getStatusTime() != null) {
            existingSite.setStatusTime(site.getStatusTime());
        }
        if (site.getLastError() != null) {
            existingSite.setLastError(site.getLastError());
        }

        return siteRepository.save(existingSite);
    }

    @Transactional
    public SiteModel updateStatus(Integer siteId, Status status) {
        return updateStatus(siteId, status, LocalDateTime.now());
    }

    @Transactional
    public SiteModel updateStatus(Integer siteId, Status status, LocalDateTime statusTime) {
        SiteModel site = siteRepository.getById(siteId);
        site.setStatus(status);
        site.setStatusTime(LocalDateTime.now());
        return siteRepository.save(site);
    }

    @Transactional
    public SiteModel updateStatusWithError(Integer siteId, Status status, String error) {
        SiteModel site = siteRepository.getById(siteId);
        site.setStatus(status);
        site.setStatusTime(LocalDateTime.now());
        site.setLastError(error);
        return siteRepository.save(site);
    }

    @Transactional
    public void batchUpdateStatus(List<Integer> siteIds, Status status) {
        siteRepository.batchUpdateStatus(siteIds, status, LocalDateTime.now());
    }

    @Transactional
    public SiteModel partialUpdate(Integer id, SiteModel partialSite) {
        SiteModel existingSite = siteRepository.getById(id);

        if (partialSite.getName() != null) {
            existingSite.setName(partialSite.getName());
        }
        if (partialSite.getLastError() != null) {
            existingSite.setLastError(partialSite.getLastError());
        }

        return siteRepository.save(existingSite);
    }

    // DELETE
    @Transactional
    public void delete(Integer id) {
        if (!siteRepository.existsById(id)) {
            throw new ApiException(MessagesAndErrorCodes.SITE_NOT_FOUND,
                    "Site with ID " + id + " not found");
        }
        siteRepository.deleteById(id);
    }

    @Transactional
    public void deleteByUrl(String url) {
        siteRepository.findByUrl(url)
                .ifPresent(site -> siteRepository.deleteById(site.getId()));
    }

//    @Transactional
//    public void deleteByStatus(Status status) {
//        siteRepository.deleteByStatus(status);
//    }

    // DTO преобразования
    public SiteDTO toDTO(SiteModel site) {
        SiteDTO dto = new SiteDTO();
        dto.setId(site.getId());
        dto.setName(site.getName());
        dto.setUrl(site.getUrl());
        dto.setStatus(site.getStatus());
        dto.setStatusTime(site.getStatusTime());
        dto.setLastError(site.getLastError());
        dto.setPagesCount(site.getPages() != null ? site.getPages().size() : 0);
        dto.setLemmasCount(site.getLemmas() != null ? site.getLemmas().size() : 0);
        return dto;
    }

    public List<SiteDTO> toDTOList(List<SiteModel> sites) {
        return sites.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Вспомогательные методы
    public List<PageModel> getPagesForSite(Integer siteId) {
        return siteRepository.findByIdWithPages(siteId);
    }

    public int getPagesCount(Integer siteId) {
        return getPagesForSite(siteId).size();
    }

    public int getLemmasCount(Integer siteId) {
        SiteModel site = siteRepository.getReferenceById(siteId);
        return site.getLemmas() != null ? site.getLemmas().size() : 0;
    }

    public void deleteAllDataBaseRecords() {
        dataBaseService.clearDataBase();
    }
}