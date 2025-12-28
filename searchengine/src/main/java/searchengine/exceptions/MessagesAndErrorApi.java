package searchengine.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessagesAndErrorApi {
    private int code;
    private String message;
    private String details; // Детали для отладки
    private LocalDateTime timestamp;
    private String path;
    private Map<String, Object> params; // Параметры для форматирования
    private boolean result;
    private String error;

    // Статический метод создания ошибки
    public static MessagesAndErrorApi of(MessagesAndErrorCodes messagesAndErrorCodes, Object... args) {

        return MessagesAndErrorApi.builder()
                .code(messagesAndErrorCodes.getCode())
                .message(messagesAndErrorCodes.formatMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static MessagesAndErrorApi of(MessagesAndErrorCodes messagesAndErrorCodes, String details, Object... args) {
        return MessagesAndErrorApi.builder()
                .code(messagesAndErrorCodes.getCode())
                .message(messagesAndErrorCodes.formatMessage())
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }
}