package uz.cluster.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.cluster.entity.auth.User;
import uz.cluster.entity.produce.Cost;
import uz.cluster.entity.references.model.Role;
import uz.cluster.enums.auth.SystemRoleName;
import uz.cluster.repository.produce.CostRepository;
import uz.cluster.services.auth_service.AuthUserService;
import uz.cluster.services.auth_service.RoleService;

@Component
public class UserComponent {

    private static AuthUserService authService;

    private static RoleService roleService;

    private static CostRepository costRepository;

    @Autowired
    public UserComponent(AuthUserService authService,RoleService roleService,CostRepository costRepository) {
        UserComponent.authService = authService;
        UserComponent.roleService = roleService;
        UserComponent.costRepository = costRepository;
    }

    public static User getById(int id){
        return authService.getByUserIdComponent(id);
    }

    public static Role getByRoleName(SystemRoleName name){
        return roleService.getByName(name.name());
    }

}
