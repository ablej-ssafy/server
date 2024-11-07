package me.noteme.headhunting.common.advice;

import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.alarm.service.MattermostNotificationService;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Enumeration;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CustomControllerAdvice {
    private final MattermostNotificationService notificationService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception, HttpServletRequest request) {
        Sentry.captureException(exception);
        sendNotification(exception, request);

        return ErrorResponse.of(new CustomException(ErrorCode.SERVER_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Sentry.captureException(exception);
        sendNotification(exception, request);

        BindingResult bindingResult = exception.getBindingResult();
        CustomException customException = new CustomException(ErrorCode.BAD_REQUEST, bindingResult);

        return ErrorResponse.of(customException);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse NoResourceFoundException(NoResourceFoundException exception, HttpServletRequest request) {
        Sentry.captureException(exception);
        sendNotification(exception, request);

        return ErrorResponse.of(new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, MissingServletRequestParameterException.class, HttpMessageNotReadableException.class, MultipartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(Exception exception, HttpServletRequest request) {
        Sentry.captureException(exception);
        sendNotification(exception, request);

        return ErrorResponse.of(new CustomException(ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception, HttpServletRequest request) {
        Sentry.captureException(exception);
        sendNotification(exception, request);

        return ResponseEntity.status(exception.getErrorCode().getStatus())
                .body(ErrorResponse.of(exception));
    }

    private void sendNotification(Exception e, HttpServletRequest request) {
        if(e instanceof CustomException) {
            log.error("errorMsg: {}", e.getMessage());
        }else{
            log.error("error: ", e);
        }
        notificationService.sendNotification(e, request.getRequestURI(), getParams(request));
    }

    private String getParams(HttpServletRequest req) {
        StringBuilder params = new StringBuilder();
        Enumeration<String> keys = req.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            params.append("- ").append(key).append(" : ").append(req.getParameter(key)).append("\n");
        }
        return params.toString();
    }
}
