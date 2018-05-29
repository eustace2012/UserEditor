package bliznyuk.stanislav.usereditor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class InvalidDataException extends Exception
{
    private String reason;


    public InvalidDataException(String reason) {
        super(String.format("Ошибка: %s", reason));
        this.reason = reason;

    }

}