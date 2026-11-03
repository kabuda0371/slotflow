package dev.slotflow.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 线程本地执行上下文：保存当前业务链 code、scope 与已解析的“卡槽 → 有序动作”表。
 *
 * <p>对应原设计中的 {@code ProcessContextHolder}。由 {@link Flow#run} 在执行业务链前安装、
 * 执行后恢复/清理，业务链代码通过 {@link Flow#actions} 读取。
 */
public final class FlowContext {

    private final String processCode;
    private final String tenant;
    private final String country;
    private final Map<String, List<ActionPoint>> actionsBySlot;

    public FlowContext(String processCode, String tenant, String country,
                       Map<String, List<ActionPoint>> actionsBySlot) {
        this.processCode = processCode;
        this.tenant = tenant;
        this.country = country;
        this.actionsBySlot = actionsBySlot;
    }

    public String processCode() {
        return processCode;
    }

    public String tenant() {
        return tenant;
    }

    public String country() {
        return country;
    }

    /** 取某卡槽已装配的动作（有序）；未装配返回空列表。 */
    public List<ActionPoint> actions(String slotCode) {
        List<ActionPoint> l = actionsBySlot.get(slotCode);
        return l == null ? Collections.<ActionPoint>emptyList() : l;
    }

    // ---------- ThreadLocal ----------

    private static final ThreadLocal<FlowContext> HOLDER = new ThreadLocal<>();

    /** 返回当前上下文；若为空返回 null（供嵌套链保存/恢复外层上下文）。 */
    public static FlowContext peek() {
        return HOLDER.get();
    }

    /** 返回当前上下文；若为空抛异常（业务链执行期间必须存在）。 */
    public static FlowContext current() {
        FlowContext c = HOLDER.get();
        if (c == null) {
            throw new IllegalStateException("no FlowContext — ensure Flow.run(...) wraps this call");
        }
        return c;
    }

    public static void set(FlowContext c) {
        HOLDER.set(c);
    }

    public static void remove() {
        HOLDER.remove();
    }
}
