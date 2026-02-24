package server.Service;
import java.util.UUID;
import dataaccess.DataAccess;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UserNameTakenException;
import io.javalin.validation.ValidationException;
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
    public RegisterResult register(RegisterRequest registerRequest) {
        String authToken;
        try {
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

        } catch (UserNameTakenException e) {
            throw e; // this will have to change
        }
        catch (ValidationException e){
            throw e;
        }
        catch (DataAccessException e){
            throw new RuntimeException(e);
        }

    }

//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}
