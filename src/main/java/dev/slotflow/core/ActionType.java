package dev.slotflow.core;

/**
 * 业务点类型枚举。
 *
 * <p>对应用原设计中的 {@code ActionPointTypeEnum}：用来给动作（Action）分一个语义类别，
 * 便于在设计态按类型检索/约束（例如某卡槽只允许 {@code VERIFY} 类型的动作）。
 */
public enum ActionType {
    VERIFY("verify", "校验"),
    ENRICH("enrich", "数据补全"),
    MAPPING("mapping", "值映射"),
    SAVE("save", "数据保存"),
    API("api", "API调用"),
    STATUS_UPDATE("status_update", "状态更新");

    private final String code;
    private final String desc;

    ActionType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String code() {
        return code;
    }

    public String desc() {
        return desc;
    }

    public static ActionType of(String code) {
        for (ActionType t : values()) {
            if (t.code.equals(code)) {
                return t;
            }
        }
        throw new IllegalArgumentException("unknown action type: " + code);
    }
}
