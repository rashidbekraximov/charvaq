package uz.cluster.enums;

import lombok.Getter;
import lombok.Setter;

public enum TaxEnum {

    QQS(1),
    YAIT(2);

    private final int value;

    @Getter
    @Setter
    private String name;

    TaxEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    TaxEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
