package uz.cluster.enums.purchase;

import lombok.Getter;
import lombok.Setter;

public enum PaymentEnum {


    NAQD(1);

    private final int value;

    @Getter
    @Setter
    private String name;

    PaymentEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    PaymentEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }


}

