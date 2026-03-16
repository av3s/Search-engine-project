package searchengine.Utils;

import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.exceptions.ApiException;
import searchengine.exceptions.MessagesAndErrorCodes;

public class Validation {

    private static Site getSiteFromAllowedSites(String domainWithoutProtocol, SitesList allowedSites) {
        if (domainWithoutProtocol == null
                || domainWithoutProtocol.trim().isEmpty()
                || allowedSites == null) {
            throw new ApiException(MessagesAndErrorCodes.PAGE_INVALID_URL);
        }

        // Проверяем точное совпадение домена
        for (Site allowedSite : allowedSites) {
            System.out.println(allowedSite.getUrl());
            System.out.println(NetworkWorker.removeProtocol(allowedSite.getUrl()));
            System.out.println(domainWithoutProtocol);
            if (domainWithoutProtocol.equals(NetworkWorker.removeProtocol(allowedSite.getUrl()))) {
                return allowedSite;
            }
        }
        return null;
    }

    public static boolean checkUrlConnection(String url) {
        return NetworkWorker.checkUrlConnection(url, 5000);
    }

    public static Site ValidateDomain(String domain, SitesList allowedSites) {
        String extractedDomainWitoutProtocol = NetworkWorker.removeProtocol(domain);
        Site site = getSiteFromAllowedSites(extractedDomainWitoutProtocol, allowedSites);
        if (site == null) throw new ApiException(MessagesAndErrorCodes.PAGE_OUTSIDE_SITES);
        return site;
    }
}

