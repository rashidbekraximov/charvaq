package uz.cluster.annotation;

import uz.cluster.enums.auth.Action;
import uz.cluster.enums.auth.SystemRoleName;
import uz.cluster.enums.forms.FormEnum;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPermission {
    FormEnum form();
    Action permission();
    SystemRoleName systemRoleName() default SystemRoleName.SYSTEM_ROLE_SUPER_ADMIN;
}
