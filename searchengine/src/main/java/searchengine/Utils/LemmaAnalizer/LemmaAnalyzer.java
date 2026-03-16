package searchengine.Utils.LemmaAnalizer;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.morphology.russian.RussianAnalyzer;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import static java.awt.SystemColor.text;

public class LemmaAnalyzer {
    Map<String, Integer> lemmaStats = new HashMap<>();


   // private String text;
    private org.jsoup.nodes.Document pageDocument;

//    LemmaAnalyzer(String text) {
//        this.text = text;
//    }

    public LemmaAnalyzer(org.jsoup.nodes.Document pageDocument) {
        this.pageDocument = pageDocument;
    }

    public Map<String, Integer> analyze() {
        String cleanContent = ContentExtractor.extractCleanContent(pageDocument);
        if (cleanContent.isEmpty()) {
            return lemmaStats;
        }



        try {
            Document luceneDocument = new Document();

            luceneDocument.add(new TextField("body", cleanContent, Field.Store.YES));
            printDocument(luceneDocument);
            RussianAnalyzer analyzer = new RussianAnalyzer();

            System.out.println("Анализ текста:");
            System.out.println("==================================");

            TokenStream tokenStream = analyzer.tokenStream("body", luceneDocument.getField("body").stringValue());
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String lemma = charTermAttribute.toString();

                if (lemma.length() > 2) {
                    lemmaStats.put(lemma, lemmaStats.getOrDefault(lemma, 0) + 1);
                }
            }
            tokenStream.end();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lemmaStats;
    }

    public void printLemmaStat(int printLimit) {
        System.out.println("┌────────────────────────────┬──────────┐");
        System.out.println("│ Лемма                      │ Частота  │");
        System.out.println("├────────────────────────────┼──────────┤");
        lemmaStats.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                //  .limit(printLimit)
                .forEach(entry -> {
                    System.out.printf("│ %-26s │ %8d │\n", entry.getKey(), entry.getValue());
                });
        System.out.println("└────────────────────────────┴──────────┘");
    }

    private void printDocument(Document document) {
        String template = """
                name:   {0}
                    type:           {1}
                    string Value:   {2}
                """;
        document.getFields().stream().forEach(field -> {
            System.out.println(MessageFormat.format(template, field.name(), field.fieldType(), field.stringValue()));
        });
    }


}
