package searchengine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private  boolean result;
    private T data;
    private String error;

    public static <T> ApiResponse<T> create(boolean result, T data, String error) {
        return ApiResponse.<T>builder()
                .result(true)
                .data(data)
                .error(error)
                .build();
    }
    public static <T> ApiResponse<T> ok(){
        return create(true, null,null );
    }

    public <T> ApiResponse<T> error(MessagesAndErrorCodes error){
        return create(false, null, error.getMessageTemplate());
    }


}