package uz.cluster.payload.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import uz.cluster.enums.Gender;
import uz.cluster.enums.auth.SystemRoleName;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String login;
    private String password;
    private String email;
    private String documentSerialNumber;

    private Gender gender;

    private String file;

    private boolean enabled;

    @Enumerated(value = EnumType.STRING)
    private SystemRoleName systemRoleName;
}
