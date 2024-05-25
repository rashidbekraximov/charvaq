package uz.cluster.enums;

import lombok.Getter;
import lombok.Setter;

public enum ManufactureProduct {

    SEMENT(1);

    private final int value;

    @Getter
    @Setter
    private String name;

    ManufactureProduct(int value, String name) {
        this.value = value;
        this.name = name;
    }

    ManufactureProduct(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
