package uz.cluster.enums.forms;

import lombok.Getter;
import lombok.Setter;

public enum DirectionEnum {

    LOGISTIC(1),
    PRODUCE(2),
    LEADER_BETON(3),
    PURCHASE(4),
    NASOS(5),
    ALL(6);

    private final int value;

    @Getter
    @Setter
    private String name;

    DirectionEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    DirectionEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }


}
