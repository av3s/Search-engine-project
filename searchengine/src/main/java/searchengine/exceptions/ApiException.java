package searchengine.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;



        @Getter
        public class ApiException extends RuntimeException {
            private final MessagesAndErrorCodes errorCode;
            private final String details;


            public ApiException(MessagesAndErrorCodes errorCode, Object... args) {
                super(errorCode.formatMessage());
                this.errorCode = errorCode;

                this.details = null;
            }

            public ApiException(MessagesAndErrorCodes errorCode, String details, Object... args) {
                super(errorCode.formatMessage());
                this.errorCode = errorCode;

                this.details = details;
            }

            public MessagesAndErrorApi toErrorApi() {
                if (details != null) {
                    return MessagesAndErrorApi.of(errorCode, details);
                }
                return MessagesAndErrorApi.of(errorCode);
            }
        }

