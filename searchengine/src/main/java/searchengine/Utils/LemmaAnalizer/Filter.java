package searchengine.Utils.LemmaAnalizer;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Filter {
    static String cleaner(String rawText) {
        if (rawText == null || rawText.isEmpty()) {
            return "";
        }

        //  Замена множественных пробелов на один
        String cleaned = rawText.replaceAll("\\s+", " ");

        // Удаление невидимых и управляющих символов (кроме обычных пробелов)
        cleaned = cleaned.replaceAll("[\\p{C}\\p{Zl}\\p{Zp}]", " ");

        // Удаление специальных HTML-символов, которые могли остаться
        cleaned = cleaned.replaceAll("&[a-z]+;", " ");

        //  Удаление лишних пунктуационных знаков
        cleaned = cleaned.replaceAll("[.!?,;:]{2,}", ".");

        //  Удаление цифр, окруженных пробелами (если это артефакты)
        cleaned = cleaned.replaceAll("\\s\\d+\\s", " ");

        //  Удаление изолированных символов (буквы/цифры, окруженные пробелами)
        cleaned = cleaned.replaceAll("\\s[а-яa-z0-9]\\s", " ");

        //  Удаление маркеров списков и специальных символов в начале строк
        cleaned = cleaned.replaceAll("^[•▪○◘◙◦⦿⚫⚪\u2022\u2023\u25E6\\-*+>]\s*«»", "");

        // Замена различных типов дефисов и тире на обычный дефис
        cleaned = cleaned.replaceAll("[\u2010-\u2015\u2212\u2E3A\u2E3B]", "-");

        // Удаление лишних дефисов
        cleaned = cleaned.replaceAll("-{2,}", "-");
        cleaned = cleaned.replaceAll("\\s-\\s", " ");

        //Удаление URL и email (если они не нужны)
        cleaned = cleaned.replaceAll("https?://\\S+", " ");
        cleaned = cleaned.replaceAll("\\S+@\\S+\\.\\S+", " ");

        //Удаление hex-кодов и escape-последовательностей
        cleaned = cleaned.replaceAll("\\\\x[0-9A-Fa-f]{2}", " ");
        cleaned = cleaned.replaceAll("\\\\u[0-9A-Fa-f]{4}", " ");

        //  Удаление оставшихся управляющих символов и непечатаемых
        cleaned = cleaned.replaceAll("[\\x00-\\x1F\\x7F]", " ");

        //  Финалная очистка пробелов
        cleaned = cleaned.trim();
        cleaned = cleaned.replaceAll("\\s+", " ");

        // Удаление повторяющихся точек в конце
        cleaned = cleaned.replaceAll("\\.{2,}$", ".");

        return cleaned;
    }

    public static String cleanRussianTextSimple(String text) {
        if (text == null) return "";

        // Удаляем нежелательные элементы
        text = text.replaceAll("\\d{1,2}[./]\\d{1,2}[./]\\d{2,4}|" +  // даты
                "\\d{1,2}[:.]\\d{2}([:.]\\d{2})?|" +   // время
                "\\b[a-zA-Z0-9]{8,}\\b|" +              // хэши
                "\\b[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(/[^\\s]*)?\\b", " "); // URL

        // Оставляем только русские буквы и пунктуацию
        text = text.replaceAll("[^а-яА-ЯёЁ\\s.,!?:;]", " ");

        // Разбиваем на слова и фильтруем
        String[] words = text.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            word = word.trim();
            if (word.isEmpty()) continue;

            // Пунктуация
            if (word.matches("[.,!?:;]+")) {
                if (!result.isEmpty() && !result.toString().endsWith(" ")) {
                    result.append(word);
                }
                continue;
            }

            // Русские слова длиннее 2 символов
            String russian = word.replaceAll("[^а-яА-ЯёЁ]", "");
            if (russian.length() > 2) {
                if (!result.isEmpty() && !result.toString().endsWith(" ")) {
                    result.append(" ");
                }
                result.append(russian);
            }
        }

        return result.toString().toLowerCase().trim();
    }

    public static String cleanSimple(String text) {
        if (text == null) return "";

        // Все шаги в одной строке:
        return Arrays.stream(text.toLowerCase()
                        .replace('ё', 'е')
                        .replaceAll("\\s+", " ")
                        .trim()
                        .split("\\s+"))
                .map(word -> word.replaceAll("[^а-я\\-]", ""))
                .filter(word -> word.length() >= 3)
                .filter(word -> word.matches("[а-я]+"))
                .filter(word -> !word.matches(".*\\d.*"))
                .collect(Collectors.joining(" "));
    }

}
