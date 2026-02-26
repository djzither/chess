package server.service;
import java.util.UUID;
import dataaccess.DataAccess;
import dataaccess.exceptions.*;

import model.UserData;
import model.AuthData;
import server.service.requestobjects.LoginRequest;
import server.service.requestobjects.AuthTokenRequest;
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
        if (user == null){
            throw new UnauthorizedException("Error: Unauthorized");
        }
        if (!user.password().equals(loginRequest.password()))
        {
            throw new UnauthorizedException("Error: Unauthorized");
        }
        dao.getUser(loginRequest.username());


        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, user.username());
        dao.createAuth(authData);

        return new RegisterLoginResult(true, user.username(), authToken);

    }


    public void logout(AuthTokenRequest authTokenRequest)
    throws UnauthorizedException, DataAccessException {
        //do I need to check for nulls? --yes because could return null or give null
        if (authTokenRequest.authToken() == null){
            throw new UnauthorizedException("Error: Unauthorized");
        }

        AuthData authData = dao.getAuth(authTokenRequest.authToken());

        //yes because I am creatign an object and what if it returns null
        if (authData == null){
            throw new UnauthorizedException("Error: Unauthorized");
        }


        dao.deleteAuth(authTokenRequest.authToken());
    }
}
