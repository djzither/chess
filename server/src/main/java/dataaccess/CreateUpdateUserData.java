package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.UserData;
import java.util.HashMap;
import java.util.Map;

public class CreateUpdateUserData {
    private final Map<String, UserData> users = new HashMap<>();

    public void createUser(UserData user) throws DataAccessException{
        if (users.containsKey(user.username())){
            throw new DataAccessException("Username taken");
        }
        users.put(user.username(), user);

    }
    /
    public UserData getUser(String userName) throws DataAccessException{
        if (!users.containsKey(userName)){
            throw new DataAccessException("User not found");
        }
        return users.get(userName);
    }
    public void updateUser(UserData user) throws DataAccessException{
        if (!users.containsKey(user.username())){
            throw new DataAccessException("User doesn't exist")
        }
        users.put(user.username(), user);
    }
}
