package dev.slotflow.core;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 业务点注册表：扫描 {@link Action} 注解方法，按 {@code code} 索引。
 *
 * <p>对应原设计中的 {@code ActionParser} + {@code fi_workbench_action} 表。
 * 原实现是启动时扫描 Spring 容器所有 bean，这里简化为显式 {@link #register(Object)} 一个动作 bean。
 */
public final class ActionRegistry {

    private final Map<String, ActionPoint> byCode = new LinkedHashMap<>();

    /**
     * 注册一个动作 bean：扫描其所有 public 方法上的 {@link Action} 注解。
     */
    public void register(Object bean) {
        // 用 getMethods() 而非 getDeclaredMethods()，兼容动态代理/子类场景
        for (Method m : bean.getClass().getMethods()) {
            Action a = m.getAnnotation(Action.class);
            if (a == null) {
                continue;
            }
            String name = a.name().isEmpty() ? a.code() : a.name();
            ActionPoint p = new ActionPoint(bean, m, a.type(), a.code(), name);
            if (byCode.put(a.code(), p) != null) {
                throw new IllegalStateException("duplicate action code: " + a.code());
            }
        }
    }

    public ActionPoint get(String code) {
        ActionPoint p = byCode.get(code);
        if (p == null) {
            throw new IllegalStateException("action not found: " + code);
        }
        return p;
    }

    public boolean contains(String code) {
        return byCode.containsKey(code);
    }

    public int size() {
        return byCode.size();
    }
}
