package searchengine.Utils.LemmaAnalizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ContentExtractor {

    public static String extractCleanContent(Document doc) {
        //  Удаление явных неконтентных элементов
        removeNonContentElements(doc);

        //  Попытка найти основной контентный блок
        String content = findMainContentBlock(doc);
        if (!content.trim().isEmpty()) {
            return content;
        }

        //  Эвристический анализ
        return heuristicExtraction(doc);
    }

    public static String CleanDocument(Document doc) {
        //  Удаление явных неконтентных элементов
        removeNonContentElements(doc);

        //  Попытка найти основной контентный блок
        String content = findMainContentBlock(doc);
        if (!content.trim().isEmpty()) {
            return content;
        }

        // Эвристический анализ
        return heuristicExtraction(doc);
    }

    private static void removeNonContentElements(Document doc) {

        // Удаляем по тегам
        doc.select("script, style, iframe, noscript, form, svg, canvas").remove();

        // Удаляем по классам (расширенный список)
        String[] classPatterns = {
                "footer", "nav", "header", "sidebar", "menu",
                "ad", "ads", "banner", "sponsor", "promo",
                "social", "share", "like", "comment", "rating",
                "modal", "popup", "overlay", "cookie",
                "widget", "toolbar", "button", "btn",
                "meta", "author", "date", "time", "category",
                "breadcrumb", "pagination", "navigation",
                "related", "recommended", "similar", "popular",
                "newsletter", "subscribe", "signup"
        };

        for (String pattern : classPatterns) {
            doc.select("." + pattern).remove();
            doc.select("[class*='" + pattern + "']").remove();
        }

        // Удаляем по id
        String[] idPatterns = {"footer", "nav", "header", "sidebar", "menu", "ad", "banner"};
        for (String pattern : idPatterns) {
            doc.select("#" + pattern).remove();
            doc.select("[id*='" + pattern + "']").remove();
        }

        // Удаляем элементы с определенными атрибутами
        doc.select("[data-ad], [data-widget], [role='navigation'], [role='banner'], " +
                "[aria-label*='ad'], [aria-label*='menu'], [aria-label*='cookie']").remove();
    }

    private static String findMainContentBlock(Document doc) {
        // Приоритетные селекторы для основного контента
        String[] contentSelectors = {
                "article", "main", ".content", ".post", ".article",
                ".entry", ".story", ".news", ".blog-post",
                "[role='main']", "[itemprop='articleBody']"
        };

        for (String selector : contentSelectors) {
            Element contentElement = doc.select(selector).first();
            if (contentElement != null) {
                // Очищаем найденный блок от оставшегося мусора
                contentElement.select(".related, .share, .author, .meta, .tags").remove();
                return contentElement.text();
            }
        }

        return "";
    }

    private static String heuristicExtraction(Document doc) {
        // Ищем все параграфы и заголовки
        Elements contentElements = doc.select("p, h1, h2, h3, h4, h5, h6");

        // Фильтруем по длине и количеству ссылок
        StringBuilder result = new StringBuilder();
        for (Element el : contentElements) {
            String text = el.text().trim();
            int linkCount = el.select("a").size();

            // Эвристика: если текст достаточно длинный и не состоит в основном из ссылок
            if (text.length() > 100 && linkCount < text.length() / 50) {
                result.append(text).append("\n\n");
            }
        }

        return result.toString();
    }
}