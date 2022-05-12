package com.jo0oy.jwtsecurityapi.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse<T> {

    private boolean success;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "Asia/Seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime responseTime;
    private T data;
    private Error<T> error;


    public static <T> ResultResponse<T> res(final boolean success) {
        return res(success, null);
    }

    public static <T> ResultResponse<T> res(final boolean success, final T data) {
        return ResultResponse.<T>builder()
                .success(success)
                .responseTime(LocalDateTime.now())
                .data(data)
                .error(null)
                .build();
    }

    public static <T> ResultResponse<T> error(final Error<T> error) {
        return ResultResponse.<T>builder()
                .success(false)
                .responseTime(LocalDateTime.now())
                .data(null)
                .error(error)
                .build();
    }



    @Getter
    @NoArgsConstructor
    @Builder
    public static class Error<T> {
        private Integer errorCode;
        private String errorMessage;
        private T errorDetails;

        public Error(final Integer errorCode, final String errorMessage, final T errorDetails) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            this.errorDetails = errorDetails;
        }
    }
}

