package uz.cluster.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.enums.auth.SystemRoleName;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private int id;
    private SystemRoleName systemRoleName;
    private List<FormPermissionDTO> roleFormPermissions;
}

