package server.Service;
import java.util.UUID;
import dataaccess.DataAccess;
import dataaccess.exceptions.BadRequest;

import model.UserData;
import model.AuthData;

import javax.xml.crypto.Data;


// still have to do a lot of work with errors, getting which ones but I'm doing better

public class UserService {
    //reference to interface so I can call methods
    private final DataAccess dao;

    public UserService(DataAccess dao) {
        this.dao = dao;
    }

    //RegisterResult is from the DAO and RegisterRequest is from HANDLER
    public RegisterResult register(RegisterRequest registerRequest)
            throws IllegalArgumentException
    //will have to add maybe more exceptions
    {

        if (registerRequest.getUserName() == null ||
                registerRequest.getEmail() == null ||
                registerRequest.getPassword() == null){
            throw new BadRequest("Bad creation request");

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
