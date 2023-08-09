package uz.cluster.payload.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
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
    private String notes;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private Integer documentTypeId;
    private Integer degreeId;
    private Gender gender;
    private Integer roleId;

    private int clusterId;

    private boolean accountNonLocked;

    @Enumerated(value = EnumType.STRING)
    private SystemRoleName systemRoleName;
}
