package uz.cluster.enums.produce;

import lombok.Getter;
import lombok.Setter;

public enum ProduceEnum {

    AMORTIZATION(1),
    ELECTR(2),
    PRODUCE_COST(3),
    REAL_COST(1000),
    PERMAMENT_COST(2000);

    private final int value;

    @Getter
    @Setter
    private String name;

    ProduceEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    ProduceEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
