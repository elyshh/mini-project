package vttp.ssf.miniproject.model;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private String username;
    private String password;
    private String name;
    private LocalDate dateOfBirth;

    public JsonObject toJsonObject() {
        JsonObject obj = Json.createObjectBuilder()
            .add("username", this.username)
            .add("password", this.password)
            .add("name", this.name)
            .add("dob", Register.getDob(this.dateOfBirth))
            .build();

        return obj;
    }

    
}

// public Register getRegister() {
    //     return register;
    // }