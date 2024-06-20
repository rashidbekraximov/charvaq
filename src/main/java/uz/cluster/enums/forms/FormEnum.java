package uz.cluster.enums.forms;

import lombok.Getter;
import lombok.Setter;

public enum FormEnum {
    //Sotuv
    PURCHASE(1),
    ESTINGUISH_DEBT(2),
    ORDER(3),
    PRICE(4),
    REMAINDER(5),
    DAILY_COST(7),

    //Logistika
    TECHNICIAN(102),
    DROBILKA(103),
    BRING_DROBILKA_PRODUCT(104),

    //Ishlab chiqarish
    READY_PRODUCT(112),
    PRODUCE_COST(113),

    //Leader Beton
    LB_PURCHASE(121),
    MIXER(122),
    INGREDIENT(123),

    //Nasos
    NASOS(130),
    NASOS_COST(132),

    //Ish haqi,
    TABEL(141),
    SALARY(142),
    EMPLOYEE(143),

    //Umumiy malumot
    INCOME_TO_WAREHOUSE(151),
    WAREHOUSE(152),
    SPEND_FROM_WAREHOUSE(153),
    SIMILAR_INFORMATION(111),

    //Admin Panel
    ADMIN_PANEL(160),

    //Admin Panel
    KASSA(180);


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
