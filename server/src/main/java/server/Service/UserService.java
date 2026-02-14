package server.Service;
import java.util.UUID;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.exceptions.UserNameTakenException;
import model.UserData;
import model.AuthData;


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
            if (registerRequest.getUserName() == null ||
                    registerRequest.getPassword() == null ||
                    registerRequest.getEmail() == null) {
                throw new DataAccessException("Error: username exists");
            }
            UserData userExists = dao.getUser(registerRequest.getUserName());
            if (userExists != null) {
                throw new UserNameTakenException("Error: username already taken");
            }
            UserData newUser = new UserData(
                    registerRequest.getUserName(),
                    registerRequest.getPassword(),
                    registerRequest.getEmail()
            );
            dao.createUser(newUser);

            authToken = UUID.randomUUID().toString();

            AuthData authData = new AuthData(authToken, registerRequest.getUserName());
            dao.createAuth(authData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e); // this will have to change
        }
        return new RegisterResult(true, registerRequest.getUserName(), authToken);
    }

//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}
