package vttp.ssf.miniproject.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Register {

    @NotBlank(message = "Please enter a username.")
    private String username;

    @NotBlank(message = "Enter password again.")
    private String password_copy;

    @NotBlank(message = "Please enter a password.")
    private String password;

    @Pattern(regexp = "^[a-zA-Z_ ]*$", message = "Only characters allowed.")
    @NotEmpty(message = "Name is required.")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date of Birth must be a past date less than today.")
    @NotNull(message = "Please select a date.")
    private LocalDate dateOfBirth;

    public static String getDob(LocalDate dateOfBirth) {
        String dob = dateOfBirth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return dob;
    }

}
