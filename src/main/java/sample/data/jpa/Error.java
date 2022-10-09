package sample.data.jpa;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Error {

    DUPLICATED_USER("there is duplicated user information", HttpStatus.UNPROCESSABLE_ENTITY),
    LOGIN_INFO_INVALID("login information is invalid", HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_ID_PROF("the professional id is invalid", HttpStatus.UNPROCESSABLE_ENTITY),
    ALREADY_BOOKED("appointment already booked", HttpStatus.UNPROCESSABLE_ENTITY),
    APPOINTMENT_NOT_FOUND("appointment not found", HttpStatus.UNPROCESSABLE_ENTITY),
    ALREADY_UNREGISTER("already unregister", HttpStatus.UNPROCESSABLE_ENTITY),
    UNAUTHORIZED_UNREGISTER("you cannot unsubscribe a user other than yourself", HttpStatus.UNPROCESSABLE_ENTITY ),
    UNAUTHORIZED_SUPPRESSION("you cannot delete the appointment of another profession", HttpStatus.UNPROCESSABLE_ENTITY )

            ;

    private final String message;
    private final HttpStatus status;

    Error(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
