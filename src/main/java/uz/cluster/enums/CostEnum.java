package uz.cluster.enums;

import lombok.Getter;
import lombok.Setter;

public enum CostEnum {

    GAS(1),
    BALLON(2),
    OIL(3),
    AMORTIZATION(4),
    SALARY(5),
    FOOD(6),
    ALL_COST(7),
    REAL_COST(8),
    ALL_INCOME(9),
    ALL_PROFIT(10);

    private final int value;

    @Getter
    @Setter
    private String name;

    CostEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    CostEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }


}
