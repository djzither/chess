package server.service;
import java.util.UUID;
import dataaccess.DataAccess;
import dataaccess.exceptions.*;

import model.UserData;
import model.AuthData;
import server.service.requestobjects.LoginRequest;
import server.service.requestobjects.LogoutRequest;
import server.service.requestobjects.RegisterRequest;
import server.service.requestobjects.RegisterLoginResult;


// still have to do a lot of work with errors, getting which ones but I'm doing better

public class UserService {
    //reference to interface so I can call methods
    private final DataAccess dao;

    public UserService(DataAccess dao) {
        this.dao = dao;
    }

    //RegisterResult is from the DAO and RegisterRequest is from HANDLER
    public RegisterLoginResult register(RegisterRequest registerRequest)
            throws UserNameTakenException, DataAccessException, BadCreationRequest
    //will have to add maybe more exceptions
    {
        // this could be a separate function...bread these more later if i want

        if (registerRequest.getUserName() == null ||
                registerRequest.getEmail() == null ||
                registerRequest.getPassword() == null){
            throw new BadCreationRequest("Bad creation request");

        }

        String authToken;
        UserData newUser = new UserData(
                registerRequest.getUserName(),
                registerRequest.getPassword(),
                registerRequest.getEmail()
        );
        dao.createUser(newUser);
        authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, registerRequest.getUserName());
        dao.createAuth(authData);

        return new RegisterLoginResult(true, registerRequest.getUserName(), authToken);
    }

    public RegisterLoginResult login(LoginRequest loginRequest)
    throws BadLoginRequestException, UnauthorizedException, DataAccessException {
        if (loginRequest.getUserName() == null ||
                loginRequest.password() == null ) {
            throw new BadLoginRequestException("Error: Bad Request");

        }
        UserData user = dao.getUser(loginRequest.username());
        if (!user.password().equals(loginRequest.password()));
        {
            dao.getUser(loginRequest.username());
        }


        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, user.username());
        dao.createAuth(authData);

        return new RegisterLoginResult(true, user.username(), authToken);

    }
    public void logout(LogoutRequest logoutRequest)
    throws UnauthorizedException {
        AuthData authData = dao.getAuth(logoutRequest.authToken());
        if (!authData.authToken().equals(logoutRequest.getAuthToken()){
            throw new UnauthorizedException("Error Unauthorized");
        }
        Authdata
    }
}
