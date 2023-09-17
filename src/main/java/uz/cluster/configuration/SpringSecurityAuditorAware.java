package uz.cluster.configuration;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.cluster.entity.auth.User;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<Integer> {

    public @NotNull Optional<Integer> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        int id = -1;
        if (authentication.getPrincipal() instanceof Integer)
            id = (Integer) authentication.getPrincipal();

        return Optional.of(id);
    }


    public @NotNull Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        return Optional.of(((User) authentication.getPrincipal()));
    }

}