package searchengine.exceptions;

public enum MessagesAndErrorCodes {
    // Ошибки индексации (1000-1099)

    INDEXING_ALREADY_STARTED(0x1100, "Индексация уже запущена"),
    INDEXING_NOT_STARTED(0x1101, "Индексация не запущена"),
    INDEXING_STOPPED_BY_USER(0x1102, "Индексация остановлена пользователем"),
    SITE_INDEXING_FAILED(0x1103, "Ошибка индексации сайта: %s"),

    // Ошибки страниц (2100-2199)

    PAGE_OUTSIDE_SITES(0x2100, "Данная страница находится за пределами сайтов, указанных в конфигурационном файле"),
    PAGE_NOT_FOUND(0x2401, "Страница не найдена"),
    PAGE_ALREADY_INDEXED(0x2102, "Страница уже проиндексирована"),
    PAGE_INVALID_URL(0x2103, "Некорректный URL страницы"),

    // Ошибки поиска (1200-1299)

    EMPTY_SEARCH_QUERY(0x3100, "Задан пустой поисковый запрос"),
    SITE_NOT_INDEXED(0x3101, "Сайт не проиндексирован"),
    SITE_NOT_FOUND(0x3102, "Сайт не найден"),
    SEARCH_QUERY_TOO_SHORT(0x3103, "Поисковый запрос слишком короткий"),
    NO_RESULTS_FOUND(0x3104, "По вашему запросу ничего не найдено"),

    // Ошибки конфигурации (1300-1399)

    CONFIG_SITES_NOT_FOUND(0x4300, "В конфигурации не указаны сайты для индексации"),
    CONFIG_INVALID_FORMAT(0x4301, "Некорректный формат конфигурации"),

    // Внутренние ошибки (1400-1499)

    INTERNAL_ERROR(5300, "Внутренняя ошибка сервера"),
    DATABASE_ERROR(5301, "Ошибка базы данных"),
    CONNECTION_ERROR(5302, "Ошибка соединения с сайтом"),
    PARSING_ERROR(5303, "Ошибка при обработке страницы"),

    // Ошибки авторизации (1500-1599)

    UNAUTHORIZED(6200, "Требуется авторизация"),
    FORBIDDEN(6201, "Доступ запрещен"),

    // Валидация (1600-1699)

    VALIDATION_ERROR(7600, "Ошибка валидации"),
    INVALID_PARAMETERS(7601, "Некорректные параметры запроса"),
    MISSING_PARAMETER(7602, "Отсутствует обязательный параметр: %s");

    private final int code;
    private final String messageTemplate;

    MessagesAndErrorCodes(int code, String messageTemplate) {
        this.code = code;
        this.messageTemplate = messageTemplate;
    }

    public int getCode() {
        return code;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public String formatMessage() {
        return String.format(messageTemplate);
    }
}