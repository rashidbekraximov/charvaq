package uz.cluster.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.entity.references.model.Form;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormPermissionDTO {
    private String  formNumber;
    private boolean canView;
    private boolean canInsert;
    private boolean canEdit;
    private boolean canDelete;
    private Integer time;
//    private Form form;

}
