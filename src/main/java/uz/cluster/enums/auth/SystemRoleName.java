package uz.cluster.enums.auth;

public enum SystemRoleName {

    SYSTEM_ROLE_SUPER_ADMIN(1),
    SYSTEM_ROLE_ADMIN(2),
    PURCHASE_ADMIN(100),
    LOGISTIC_ADMIN(101),
    PRODUCE_ADMIN(110),
    LEADER_BETON_ADMIN(120),
    WAREHOUSE_ADMIN(150),
    NASOS_ADMIN(130),
    SALARY_ADMIN(140);

    private final int value;

    SystemRoleName(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
