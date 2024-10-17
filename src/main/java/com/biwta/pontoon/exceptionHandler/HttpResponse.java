package com.biwta.pontoon.exceptionHandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nasimkabir
 * ৮/৮/২৩
 */
@Data
@NoArgsConstructor
public class HttpResponse {
    private Map<String, List<FieldValidationError>> errors= new HashMap<String, List<FieldValidationError>>();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss", timezone = "Asia/Dhaka")
    private Date timeStamp;
    private int httpStatusCode; // 200, 201, 400, 500
    private HttpStatus httpStatus;
    private String reason;
    private String message;

    public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message) {
        this.timeStamp = new Date();
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.reason = reason;
        this.message = message;
    }

    public HttpResponse(int httpStatusCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    public HttpResponse(Date timeStamp,int httpStatusCode, String message) {
        this.timeStamp=timeStamp;
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }
}
