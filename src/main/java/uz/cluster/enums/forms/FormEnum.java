package uz.cluster.enums.forms;

import lombok.Getter;
import lombok.Setter;

public enum FormEnum {
    //Sotuv
    PURCHASE(1),
    ESTINGUISH_DEBT(2),
    ORDER(3),
    REMAINDER(4),
    PRICE(5),

    //Logistika
    TECHNICIAN(102),
    DROBILKA(103),
    BRING_DROBILKA_PRODUCT(104),

    //Ishlab chiqarish
    READY_PRODUCT(112),
    PRODUCE_COST(113),

    //Nasos
    NASOS(131),

    //Ish haqi,
    TABEL(141),
    SALARY(142),
    EMPLOYEE(143),

    //Umumiy malumot
    SIMILAR_INFORMATION(151),

    //Admin Panel
    ADMIN_PANEL(160);


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
