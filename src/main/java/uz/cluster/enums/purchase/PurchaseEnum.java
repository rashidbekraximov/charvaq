package uz.cluster.enums.purchase;

import lombok.Getter;
import lombok.Setter;

public enum PurchaseEnum {


    KLINES(1),
    CHINOZ(2),
    SHEBEN(3),
    SH_BLOK(7);

    private final int value;

    @Getter
    @Setter
    private String name;

    PurchaseEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    PurchaseEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }


}
