package uz.cluster.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.cluster.annotation.excutor.CheckTimePermission;

import java.time.LocalDateTime;

@Component
public class CheckTimePermissionComponent {

    private static CheckTimePermission checkTimePermission;

    @Autowired
    public CheckTimePermissionComponent(CheckTimePermission checkTimePermission) {
        CheckTimePermissionComponent.checkTimePermission = checkTimePermission;
    }

    public static void checkTimePermission(int formId){
        checkTimePermission.checkTimePermission(formId);
    }
}
