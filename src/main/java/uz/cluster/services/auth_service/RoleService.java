package uz.cluster.services.auth_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.entity.auth.RoleFormPermission;
import uz.cluster.entity.references.model.Form;
import uz.cluster.entity.references.model.Role;
import uz.cluster.enums.auth.SystemRoleName;
import uz.cluster.payload.auth.FormPermissionDTO;
import uz.cluster.payload.auth.RoleDTO;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.references.FormRepository;
import uz.cluster.repository.user_info.RoleRepository;
import uz.cluster.util.LanguageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final FormRepository formRepository;

//    @CheckPermission(form = FormEnum.ROlE_LIST, permission = Action.CAN_VIEW)
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

//    @CheckPermission(form = FormEnum.ROlE_LIST, permission = Action.CAN_VIEW)
    public List<Form> getAllFormByRole(SystemRoleName systemRoleName) {
        return formRepository.findAllByParentForm_Id(systemRoleName.getValue());
    }

    public Role getById(int id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role getByName(SystemRoleName name) {
        return roleRepository.findBySystemRoleName(name.name()).get();
    }

    @Transactional
    public ApiResponse addWithPermission(RoleDTO roleDTO) {
        if (roleDTO.getSystemRoleName() == null || roleDTO.getSystemRoleName().name().isEmpty())
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));

        if (roleDTO.getId() != 0){
            return editWithPermission(roleDTO,roleDTO.getId());
        }

        if (roleRepository.existsBySystemRoleName(roleDTO.getSystemRoleName()))
            return new ApiResponse(false, LanguageManager.getLangMessage("already_exist"));

        Role role = new Role(roleDTO.getSystemRoleName());

        List<RoleFormPermission> roleFormPermissionList = new ArrayList<>();
        int permissionCode = 0;

        for (FormPermissionDTO formPermissionDTO : roleDTO.getRoleFormPermissions()) {
            if (formPermissionDTO.isCanView())
                permissionCode += 1000;
            if (formPermissionDTO.isCanInsert())
                permissionCode += 100;
            if (formPermissionDTO.isCanEdit())
                permissionCode += 10;
            if (formPermissionDTO.isCanDelete())
                permissionCode += 1;
            roleFormPermissionList.add(new RoleFormPermission(
                    role,
                    formRepository.findByFormNumber(formPermissionDTO.getFormNumber()).orElse(null),
                    permissionCode,
                    formPermissionDTO.isCanView(),
                    formPermissionDTO.isCanInsert(),
                    formPermissionDTO.isCanEdit(),
                    formPermissionDTO.isCanDelete(),
                    formPermissionDTO.getTime()
            ));
            permissionCode = 0;
        }

        role.setRoleFormPermissions(roleFormPermissionList);
        Role savedRole = roleRepository.save(role);
        return new ApiResponse(true, savedRole.getId(), LanguageManager.getLangMessage("saved"));
    }

    @Transactional
    public ApiResponse editWithPermission(RoleDTO roleDTO, int id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isEmpty())
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));

        if (roleRepository.existsBySystemRoleNameAndIdNot(roleDTO.getSystemRoleName(), id)){
            return new ApiResponse(false, LanguageManager.getLangMessage("already_exist"));
        }

        Role editingRole = optionalRole.get();
        editingRole.setSystemRoleName(roleDTO.getSystemRoleName());

        List<RoleFormPermission> roleFormPermissionList = new ArrayList<>();
        int permissionCode = 0;

        int i = 0;
        for (RoleFormPermission roleFormPermission : editingRole.getRoleFormPermissions()) {
            if (roleDTO.getRoleFormPermissions().get(i).isCanView())
                permissionCode += 1000;
            if (roleDTO.getRoleFormPermissions().get(i).isCanInsert())
                permissionCode += 100;
            if (roleDTO.getRoleFormPermissions().get(i).isCanEdit())
                permissionCode += 10;
            if (roleDTO.getRoleFormPermissions().get(i).isCanDelete())
                permissionCode += 1;
            roleFormPermission.setRole(editingRole);
            roleFormPermission.setForm(formRepository.findByFormNumber(roleDTO.getRoleFormPermissions().get(i).getFormNumber()).orElse(null));
            roleFormPermission.setPermissionCode(permissionCode);
            roleFormPermission.setCanView(roleDTO.getRoleFormPermissions().get(i).isCanView());
            roleFormPermission.setCanInsert(roleDTO.getRoleFormPermissions().get(i).isCanInsert());
            roleFormPermission.setCanEdit(roleDTO.getRoleFormPermissions().get(i).isCanEdit());
            roleFormPermission.setCanDelete(roleDTO.getRoleFormPermissions().get(i).isCanDelete());
            roleFormPermission.setTime(roleDTO.getRoleFormPermissions().get(i).getTime());
            permissionCode = 0;
            i++;
        }

        editingRole.setRoleFormPermissions(roleFormPermissionList);
        roleRepository.save(editingRole);
        return new ApiResponse(true, id, LanguageManager.getLangMessage("edited"));
    }

//    @CheckPermission(form = FormEnum.ROlE_LIST, permission = Action.CAN_DELETE)
    public ApiResponse delete(int id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isEmpty())
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        roleRepository.delete(optionalRole.get());
        return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
    }
}
