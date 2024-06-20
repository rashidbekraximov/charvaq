package uz.cluster.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import uz.cluster.entity.references.model.Form;
import uz.cluster.entity.references.model.Role;
import uz.cluster.entity.Auditable;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
public class RoleFormPermission extends Auditable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Role role;

    @ManyToOne
    private Form form;

    @Column
    private int permissionCode;

    @Column
    private boolean canView;

    @Column
    private boolean canInsert;

    @Column
    private boolean canEdit;

    @Column
    private boolean canDelete;

    @Column(columnDefinition = "real default 0")
    private Integer time; //minutes


    public RoleFormPermission(Role role, Form form, int permissionCode) {
        this.role = role;
        this.form = form;
        this.permissionCode = permissionCode;
    }

    public RoleFormPermission(Role role, Form form, int permissionCode, boolean canView, boolean canInsert, boolean canEdit, boolean canDelete) {
        this.role = role;
        this.form = form;
        this.permissionCode = permissionCode;
        this.canView = canView;
        this.canInsert = canInsert;
        this.canEdit = canEdit;
        this.canDelete = canDelete;
    }

    public RoleFormPermission(Role role, Form form, int permissionCode, boolean canView, boolean canInsert, boolean canEdit, boolean canDelete, Integer time) {
        this.role = role;
        this.form = form;
        this.permissionCode = permissionCode;
        this.canView = canView;
        this.canInsert = canInsert;
        this.canEdit = canEdit;
        this.canDelete = canDelete;
        this.time = time;
    }

    public RoleFormPermission(String id,Form form) {
        this.form = form;
        this.id =new UUID(0,0);
    }

    public String getPermissionCodeString() {
        String permissionCodeString;
        if (String.valueOf(permissionCode).length() == 1)
            permissionCodeString = "000" + permissionCode;
        else if (String.valueOf(permissionCode).length() == 2)
            permissionCodeString = "00" + permissionCode;
        else if (String.valueOf(permissionCode).length() == 3)
            permissionCodeString = "0" + permissionCode;
        else if (String.valueOf(permissionCode).length() == 4)
            permissionCodeString = "" + permissionCode;
        else
            permissionCodeString = "0000";
        return permissionCodeString;
    }

    public boolean isCanView() {
        return getPermissionCodeString().toCharArray()[0] == '1';
    }

    public boolean isCanInsert() {
        return getPermissionCodeString().toCharArray()[1] == '1';
    }

    public boolean isCanEdit() {
        return getPermissionCodeString().toCharArray()[2] == '1';
    }

    public boolean isCanDelete() {
        return getPermissionCodeString().toCharArray()[3] == '1';
    }
}
