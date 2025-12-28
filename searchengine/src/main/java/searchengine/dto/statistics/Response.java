package searchengine.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import searchengine.exceptions.MessagesAndErrorApi;
import searchengine.exceptions.MessagesAndErrorCodes;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response<T> {
    private boolean success;
    private T data;
    private MessagesAndErrorApi resultMessage;

    public static <T> Response<T> success() {
        return Response.<T>builder()
                .success(true)
                .build();
    }

    public static <T> Response<T> error(MessagesAndErrorCodes errorCode, Object... args) {
        return Response.<T>builder()
                .success(false)
                .resultMessage(MessagesAndErrorApi.of(errorCode, args))
                .build();
    }

    public static <T> Response<T> error(MessagesAndErrorCodes errorCode, String details, Object... args) {
        return Response.<T>builder()
                .success(false)
                .resultMessage(MessagesAndErrorApi.of(errorCode, details, args))
                .build();
    }

}
