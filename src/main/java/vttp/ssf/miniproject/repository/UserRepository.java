package vttp.ssf.miniproject.repository;

import java.io.StringReader;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.ssf.miniproject.model.Register;
import vttp.ssf.miniproject.model.User;

@Repository
public class UserRepository {

    @Autowired @Qualifier("myredis")
    private RedisTemplate<String, String> template;

    public void saveUser(User user) {
        ValueOperations<String, String> valueOps = template.opsForValue();
        valueOps.set(user.getUsername(), user.toJsonObject().toString());
    }
    
    public User getUser(String key) {
        String s = (String) template.opsForValue().get(key);
        JsonReader jsonReader = Json.createReader(new StringReader(s));
        JsonObject jsonObj = jsonReader.readObject();
        String username = jsonObj.getString("username", "NULL");
        String password = jsonObj.getString("password", "NULL");
        String name = jsonObj.getString("name", "NULL");
        String dob = jsonObj.getString("dob", "NULL");
        
        User user_output = new User();
        user_output.setUsername(username);
        user_output.setPassword(password);
        user_output.setName(name);
        user_output.setDateOfBirth(LocalDate.parse(dob));

        // Register register = new Register();
        // register.setName(name);
        // register.setDateOfBirth(LocalDate.parse(dob));
        // user_output.setRegister(register);

        return user_output;
    }

    public boolean isUserValid1(String username) {
        return template.hasKey(username);
    }

    public boolean isUserValid2(String username, String password) {
        User u = getUser(username);
        if (u.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public boolean isPasswordSame(String password1, String password2) {
        if (password1.equals(password2)) {
            return true;
        }
        return false;
    }

    public void register(Register register) {
        User user = new User();
    
        user.setUsername(register.getUsername());
        user.setPassword(register.getPassword());
        user.setName(register.getName());
        user.setDateOfBirth(register.getDateOfBirth());
        saveUser(user);
    }
    
    
}
