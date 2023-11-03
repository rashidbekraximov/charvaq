package uz.cluster.enums;

import lombok.Getter;
import lombok.Setter;

public enum Unit {

    KUB(1),
    DONA(2);


    private final int value;

    @Getter
    @Setter
    private String name;

    Unit(int value, String name) {
        this.value = value;
        this.name = name;
    }

    Unit(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}
