package uz.cluster.enums.forms;

import lombok.Getter;
import lombok.Setter;

public enum FormEnum {
    FORM(100),
    FORM_1(1),
    FORM_2(2),
    FORM_3(3),


    REPORT(100),
    REFERENCES(110),
    SIMILAR_INFORMATION(111),
    TAXES(112),
    FORM_LIST(113),
    ROlE_LIST(114),

    ADMIN_PANEL(130),
    ADMIN_PANEL_USER_LIST(131),
    ADMIN_PANEL_FORM_USER(132),
    ADMIN_PANEL_ADD_ADMIN(133);


    private final int value;

    @Getter
    @Setter
    private String name;

    FormEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    FormEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
