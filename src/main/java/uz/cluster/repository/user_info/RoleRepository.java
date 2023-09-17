package uz.cluster.repository.user_info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.references.model.Role;
import uz.cluster.enums.auth.SystemRoleName;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "select * from roles t where t.system_role_name = :systemRoleName", nativeQuery = true)
    Optional<Role> findBySystemRoleName(@Param("systemRoleName") String systemRoleName);

    Boolean existsBySystemRoleName(SystemRoleName systemRoleName);

    Boolean existsBySystemRoleNameAndIdNot(SystemRoleName systemRoleName, int id);
}
