package uz.cluster.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.enums.Gender;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private boolean success;
    private String fullName;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private Gender gender;
    private String login;
    private String systemRoleName;
    private boolean enabled;
}
