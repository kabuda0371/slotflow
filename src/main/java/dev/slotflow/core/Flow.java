package dev.slotflow.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 引擎门面：装配 + 运行 + 类型化取卡槽动作。
 *
 * <p>对应原设计中的 {@code ProcessAnnotationAspect}（运行时装配上下文）+ {@code ActionsUtil}
 * （类型化取 bean）。最小版省略了 Spring AOP，用 {@link #run} 显式包裹业务链方法。
 */
public final class Flow {

    private Flow() {
    }

    /**
     * 执行一次业务链：
     * 解析运行态配置 → 安装 {@link FlowContext} → 执行 body → 恢复/清理上下文（支持链嵌套）。
     */
    public static void run(String processCode, String tenant, String country,
                           ProcessRegistry config, ActionRegistry actions, Runnable body) {
        Map<String, List<ActionPoint>> resolved = config.resolve(processCode, tenant, country, actions);
        FlowContext previous = FlowContext.peek();
        try {
            FlowContext.set(new FlowContext(processCode, tenant, country, resolved));
            body.run();
        } finally {
            if (previous != null) {
                FlowContext.set(previous); // 嵌套链：恢复外层上下文
            } else {
                FlowContext.remove();
            }
        }
    }

    /**
     * 类型化获取某卡槽已装配的动作（有序）。业务链代码里这样用：
     * <pre>{@code
     * for (VerifySlot a : Flow.actions(SlotDef.VERIFY, VerifySlot.class)) a.verify(order);
     * }</pre>
     */
    public static <S extends ISlot> List<S> actions(Seam seam, Class<S> contract) {
        List<ActionPoint> pts = FlowContext.current().actions(seam.code());
        return pts.stream().map(p -> contract.cast(p.owner())).collect(Collectors.<S>toList());
    }
}
