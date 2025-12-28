package searchengine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import searchengine.dto.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<MessagesAndErrorApi> handleApiException(ApiException ex, HttpServletRequest request) {
        MessagesAndErrorApi error = MessagesAndErrorApi.builder().result(false).error(ex.getMessage()).build();
        System.out.println(ex.getErrorCode().getMessageTemplate());
        System.out.println(ex.getErrorCode().getCode());
        HttpStatus status = determineHttpStatus(ex.getErrorCode());
        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String errorDetails = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        MessagesAndErrorApi error = MessagesAndErrorApi.builder()
                .code(MessagesAndErrorCodes.VALIDATION_ERROR.getCode())
                .message(MessagesAndErrorCodes.VALIDATION_ERROR.getMessageTemplate())
                .details(errorDetails)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        MessagesAndErrorApi error = MessagesAndErrorApi.of(
                MessagesAndErrorCodes.INVALID_PARAMETERS,
                "Некорректное значение параметра: " + ex.getName()
        );
        error.setPath(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex, HttpServletRequest request) {

        MessagesAndErrorApi error = MessagesAndErrorApi.builder()
                .code(MessagesAndErrorCodes.INTERNAL_ERROR.getCode())
                .message(MessagesAndErrorCodes.INTERNAL_ERROR.getMessageTemplate())
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(error));
    }

    private HttpStatus determineHttpStatus(MessagesAndErrorCodes errorCode) {
        int code = errorCode.getCode();

        // Маппинг кодов ошибок на HTTP статусы
        return switch ((errorCode.getCode() & 0x0F00) >> 8) {
            case 1 -> HttpStatus.BAD_REQUEST;              //400
            case 2 -> HttpStatus.UNAUTHORIZED;             //401
            case 4 -> HttpStatus.NOT_FOUND;                //404
            case 5 -> HttpStatus.PAYMENT_REQUIRED;         //402
            case 6 -> HttpStatus.FORBIDDEN;                //403
            default -> HttpStatus.INTERNAL_SERVER_ERROR;   //500
        };
    }
}