package uz.cluster.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.cluster.component.CheckTimePermissionComponent;
import uz.cluster.component.UserComponent;
import uz.cluster.entity.auth.RoleFormPermission;
import uz.cluster.entity.auth.User;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.exceptions.ForbiddenException;
import uz.cluster.services.auth_service.AuthUserService;
import uz.cluster.services.auth_service.RoleService;
import uz.cluster.util.GlobalParams;
import uz.cluster.util.LanguageManager;

import static uz.cluster.enums.auth.SystemRoleName.SYSTEM_ROLE_ADMIN;
import static uz.cluster.enums.auth.SystemRoleName.SYSTEM_ROLE_SUPER_ADMIN;

@Component
@Aspect
public class CheckPermissionExecutor {

    @Before(value = "@annotation(checkPermission)")
    public void checkPermission(CheckPermission checkPermission) {
        User user = GlobalParams.getCurrentUser();

        FormEnum form = checkPermission.form();
        Action permission = checkPermission.permission();
        boolean exist = false;

        CheckTimePermissionComponent.checkTimePermission(form.getValue());
        if (user.getSystemRoleName() == SYSTEM_ROLE_SUPER_ADMIN){
            return;
        }else if (user.getSystemRoleName() == SYSTEM_ROLE_ADMIN){
            if (form == FormEnum.KASSA)
                throw new ForbiddenException("Moderator", "No such permission");
            return;
        }else if (user.getSystemRoleName() != SYSTEM_ROLE_SUPER_ADMIN && user.getSystemRoleName() != null){
            if (form == FormEnum.KASSA)
                throw new ForbiddenException("Moderator", "No such permission");

            for (RoleFormPermission roleFormPermission : UserComponent.getByRoleName(user.getSystemRoleName()).getRoleFormPermissions()) {
                if (permission.getValue() == 1000) {
                    if (roleFormPermission.getForm().getId() == form.getValue() && roleFormPermission.isCanView()) {
                        exist = true;
                        break;
                    }
                } else if (permission.getValue() == 100) {
                    if (roleFormPermission.getForm().getId() == form.getValue() && roleFormPermission.isCanInsert()) {
                        exist = true;
                        break;
                    }
                } else if (permission.getValue() == 10) {
                    if (roleFormPermission.getForm().getId() == form.getValue() && roleFormPermission.isCanEdit()) {
                        exist = true;
                        break;
                    }
                } else if (permission.getValue() == 1) {
                    if (roleFormPermission.getForm().getId() == form.getValue() && roleFormPermission.isCanDelete()) {
                        exist = true;
                        break;
                    }
                }
            }
        }else{
                throw new ForbiddenException("SYSTEM_ROLE_NAME", "System role is not defined in this user");
        }

        if (!exist) {
            throw new ForbiddenException(checkPermission.form() + " => " + checkPermission.permission(), LanguageManager.getLangMessage("forbidden"));
        }
    }
}
