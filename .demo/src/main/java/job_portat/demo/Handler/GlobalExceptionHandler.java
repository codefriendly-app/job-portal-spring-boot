package job_portat.demo.Handler;

import jakarta.servlet.http.HttpServletRequest;
import job_portat.demo.CustomeExceptions.UsernameAlreadyExists;
import job_portat.demo.ResponseDto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidateException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    )
    {
        Map<String ,String > errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );
        ApiErrorResponse response = new ApiErrorResponse(
                400,
                "Bad request",
                "Validation Faild",
                request.getRequestURI(),
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UsernameAlreadyExists.class)
    public ResponseEntity<ApiErrorResponse> userAlreadyExits(
            UsernameAlreadyExists ex,
            HttpServletRequest request
    ) {
        Map<String ,String> error = new HashMap<>();
        error.put("error",ex.getMessage());

        ApiErrorResponse response = new ApiErrorResponse(
                409,
                "Conflict",
                "Username Already exist",
                request.getRequestURI(),
                error
        );
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(response);
    }
}
