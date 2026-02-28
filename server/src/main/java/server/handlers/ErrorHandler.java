package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.ServiceException;
import dataaccess.exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.jetbrains.annotations.NotNull;
import server.service.requestobjects.ErrorResponseResult;

public class ErrorHandler implements ExceptionHandler<ServiceException> {

    @Override
    public void handle(@NotNull ServiceException e, @NotNull Context context) {
        int status = switch(e){
            case BadRequestException b -> 400;

            case UnauthorizedException u -> 401;

            case AlreadyTakenException a -> 403;

            default -> 500;

        };
        context.status(status);
        context.result(new Gson().toJson(new ErrorResponseResult(e.getMessage())));

    }
}
