package uz.cluster.annotation.excutor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.cluster.component.UserComponent;
import uz.cluster.entity.auth.RoleFormPermission;
import uz.cluster.entity.auth.User;
import uz.cluster.enums.auth.SystemRoleName;
import uz.cluster.exceptions.ForbiddenException;
import uz.cluster.repository.user_info.RoleFormPermissionRepository;
import uz.cluster.util.Converter;
import uz.cluster.util.GlobalParams;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckTimePermission {

    private final RoleFormPermissionRepository roleFormPermissionRepository;

    public void checkTimePermission(int formId) {
        User user = GlobalParams.getCurrentUser();
        if (user.getSystemRoleName() != SystemRoleName.SYSTEM_ROLE_SUPER_ADMIN && UserComponent.getByRoleName(user.getSystemRoleName()) != null) {
            for (RoleFormPermission roleFormPermission : UserComponent.getByRoleName(user.getSystemRoleName()).getRoleFormPermissions()) {
                if (roleFormPermission.getForm().getId() == formId) {
                    if (Converter.getReductionToDouble(Duration.between(roleFormPermission.getModifiedOn(), LocalDateTime.now()).getSeconds() / 3600.0) > roleFormPermission.getTime()) {
                        roleFormPermission.setTime(0);
                        roleFormPermissionRepository.save(roleFormPermission);
                        throw new ForbiddenException("Time out", "Bu ma'lumotni o'zgartira olmaysiz.");
                    }
                }
            }
        }
    }
}
