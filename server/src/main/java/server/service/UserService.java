package server.service;
import java.util.UUID;
import dataaccess.DataAccess;
import dataaccess.exceptions.BadCreationRequest;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UserNameTakenException;
import model.UserData;
import model.AuthData;
import server.service.requestobjects.RegisterRequest;
import server.service.requestobjects.RegisterResult;


// still have to do a lot of work with errors, getting which ones but I'm doing better

public class UserService {
    //reference to interface so I can call methods
    private final DataAccess dao;

    public UserService(DataAccess dao) {
        this.dao = dao;
    }

    //RegisterResult is from the DAO and RegisterRequest is from HANDLER
    public RegisterResult register(RegisterRequest registerRequest)
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

        return new RegisterResult(true, registerRequest.getUserName(), authToken);
    }

//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}
