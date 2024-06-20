package uz.cluster.enums.purchase;

import lombok.Getter;
import lombok.Setter;

public enum PurchaseEnum {


    KLINES(1),
    SHEBEN(2),
    CHINOZ(3),
    POYMA(4),
    SHAGAL(5),
    KIRPICH(6),
    SH_BLOK(7),
    SEMENT(8),
    DOBAVKA(9),
    ANTIMAROZ(10);

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
