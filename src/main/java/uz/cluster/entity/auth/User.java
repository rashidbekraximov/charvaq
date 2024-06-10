package uz.cluster.entity.auth;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.FileEntity;
import uz.cluster.entity.references.model.Role;
import uz.cluster.enums.Gender;
import uz.cluster.enums.auth.SystemRoleName;
import uz.cluster.payload.auth.UserDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"login"})})
public class User extends Auditable  implements UserDetails {
    @Hidden
    @Id
    @SequenceGenerator(allocationSize = 1, name = "users_sq", sequenceName = "users_sq")
    @GeneratedValue(generator = "users_sq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @Schema(example = "name", description = "Fill the name")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "varchar(10) default 'MALE'")
    private Gender gender = Gender.MALE;

    @Schema(example = "0", defaultValue = "0")
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "image_id",nullable = true)
    private FileEntity file;

    @Column(name = "document_serial_number", unique = true)
    private String documentSerialNumber;

    @Column
    private boolean enabled = true; //Mail checking turned off

    @Column
    private boolean accountNonExpired = true;

    @Column
    private boolean accountNonLocked = true;

    @Column
    private boolean credentialsNonExpired = true;


    @Enumerated(value = EnumType.STRING)
    @Column(name = "system_role_name")
    SystemRoleName systemRoleName;

    public User(
            String firstName, String lastName, String middleName,
            String documentSerialNumber, String login,
            String password, String email, Gender gender,
            SystemRoleName systemRoleName,FileEntity file, boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.documentSerialNumber = documentSerialNumber;
        this.login = login;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.systemRoleName = systemRoleName;
        this.file = file;
        this.enabled = enabled;
    }

    public UserDTO getDto() {
        UserDTO dto = new UserDTO();
        dto.setId(this.id);
        dto.setFirstName(this.firstName);
        dto.setMiddleName(this.middleName);
        dto.setLastName(this.lastName);
        dto.setLogin(this.login);
        dto.setEmail(this.email);
        dto.setDocumentSerialNumber(this.documentSerialNumber);
        dto.setGender(this.gender);
        dto.setEnabled(this.enabled);
        dto.setSystemRoleName(this.systemRoleName);
        dto.setFile(this.file == null ? null : this.file.getFileName());
        return dto;
    }

    public void copy(User user) {
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.middleName = user.middleName;
        this.documentSerialNumber = user.documentSerialNumber;
        this.login = user.login;
        this.password = user.password;
        this.email = user.email;
        this.gender = user.gender;
    }

    public String getFio() {
        return lastName + " " + firstName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.systemRoleName == null) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(SystemRoleName.SYSTEM_ROLE_SUPER_ADMIN.name());
            return Collections.singleton(simpleGrantedAuthority);
        }
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(this.systemRoleName.name());
        return Collections.singleton(simpleGrantedAuthority);
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    public String getAuthority() {
        return null;
    }
}