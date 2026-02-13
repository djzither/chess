package server.Service;
import java.util.UUID;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.UserData;
import model.AuthData;

import javax.xml.crypto.Data;

//for first two
public class UserService {
    //RegisterResult is from the DAO and RegisterRequest is from HANDLER
    public RegisterResult register(RegisterRequest registerRequest) {
        try{
            if (registerRequest.getUserName() == null ||
                    registerRequest.getPassword() == null ||
                    registerRequest.getEmail() == null)
                throw new DataAccessException("Error: bad request");
            UserData userExists = DataAccess.getUser(registerRequest.getUserName());
            if (userExists != null){
                throw new DataAccessException("Error: already taken");
            }
            UserData newUser = new UserData(
                    registerRequest.getUserName(),
                    registerRequest.getPassword(),
                    registerRequest.getEmail()
            );
            DataAccess.createUser(newUser);

            String authToken = UUID.randomUUID().toString();

            AuthData authData = new AuthData(authToken, registerRequest.getUserName());
            DataAccess.createAuth(authData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}
