package searchengine.Utils.LemmaAnalizer;

import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecondAnalyzer {

    public static void analyze(Document document) throws IOException {
        RussianLuceneMorphology russianLuceneMorphology = new RussianLuceneMorphology();
        analyzeText(russianLuceneMorphology, ContentExtractor.extractCleanContent(document));
    }


    private static void analyzeText(RussianLuceneMorphology morphology, String text) {

        Pattern pattern = Pattern.compile("[а-яё]+", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(text);

        int wordCount = 0;
        while (matcher.find()) {
            String word = matcher.group().toLowerCase();

            if (word.length() < 2) continue;

            try {
                List<String> morphInfo = morphology.getMorphInfo(word);
                System.out.println("\n=== Слово: " + word + " ===");
                morphInfo.forEach(System.out::println);
                wordCount++;

                if (wordCount >= 50) {
                    System.out.println("\n... и так далее (проанализировано " + wordCount + " слов)");
                    break;
                }
            } catch (Exception e) {
                System.err.println("Ошибка при обработке слова '" + word + "': " + e.getMessage());
            }
        }

        if (wordCount == 0) {
            System.out.println("В тексте не найдено русских слов для анализа.");
        }
    }

    public List<String> extractRussianWords(String text) {
        Pattern pattern = Pattern.compile("[а-яё]+", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(text);

        java.util.ArrayList<String> words = new java.util.ArrayList<>();
        while (matcher.find()) {
            String word = matcher.group().toLowerCase();
            if (word.length() >= 2) {
                words.add(word);
            }
        }
        return words;
    }

    public List<String> getLemmas(String text) throws IOException {
        RussianLuceneMorphology morphology = new RussianLuceneMorphology();
        List<String> russianWords = extractRussianWords(text);
        java.util.ArrayList<String> lemmas = new java.util.ArrayList<>();

        for (String word : russianWords) {
            try {
                List<String> normalForms = morphology.getNormalForms(word);
                lemmas.addAll(normalForms);
            } catch (Exception e) {
            }
        }

        return lemmas;
    }
}