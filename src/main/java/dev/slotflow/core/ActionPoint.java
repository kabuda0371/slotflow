package dev.slotflow.core;

import java.lang.reflect.Method;

/**
 * 解析后的业务点：持有动作所属对象、方法、类型、编码。
 *
 * <p>对应原设计中的 {@code FiWorkbenchAction}（业务点元数据）+ beanName 解析结果。
 * 运行态按配置的 {@code sequence} 排序后，逐个取 {@link #owner()} 并转成卡槽接口类型调用。
 * {@link #method()} 保留方法引用，便于后续用反射/全路径分发（对应原设计的 {@code fullApiPath}）。
 */
public final class ActionPoint {

    private final Object owner;
    private final Method method;
    private final ActionType type;
    private final String code;
    private final String name;

    public ActionPoint(Object owner, Method method, ActionType type, String code, String name) {
        this.owner = owner;
        this.method = method;
        this.type = type;
        this.code = code;
        this.name = name;
    }

    public Object owner() {
        return owner;
    }

    public Method method() {
        return method;
    }

    public ActionType type() {
        return type;
    }

    public String code() {
        return code;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return code;
    }
}
