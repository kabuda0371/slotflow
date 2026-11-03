package dev.slotflow.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 运行态装配配置：{@code processCode + scope(租户/国家)} → 每个卡槽的有序动作列表。
 *
 * <p>对应原设计中的 {@code fi_workbench_process_config} + {@code fi_workbench_action_config}
 * 及其缓存（{@code ProcessConfigCacheLoader}）。设计意图：
 * <ul>
 *   <li>动作（Action）只声明“是什么”，本配置声明“挂在哪、按什么顺序”；</li>
 *   <li>同一业务链在不同租户/国家可以挂不同动作集合（如 SG 挂税金校验，MY 挂 VIP 补全）；</li>
 *   <li>{@code sequence} 决定同一卡槽内多个动作的执行顺序。</li>
 * </ul>
 */
public final class ProcessRegistry {

    /** 一条装配记录：slotCode + actionCode + 顺序。 */
    public static final class Binding {
        public final String slotCode;
        public final String actionCode;
        public final int sequence;

        public Binding(String slotCode, String actionCode, int sequence) {
            this.slotCode = slotCode;
            this.actionCode = actionCode;
            this.sequence = sequence;
        }
    }

    private final Map<String, List<Binding>> configs = new LinkedHashMap<>();

    private static String key(String processCode, String tenant, String country) {
        return processCode + "|" + (tenant == null ? "" : tenant) + "|" + (country == null ? "" : country);
    }

    /**
     * 绑定：某 processCode 在 (tenant, country) 下，把 actionCode 挂到 slotCode，sequence 为执行顺序。
     */
    public void bind(String processCode, String tenant, String country,
                     String slotCode, String actionCode, int sequence) {
        configs.computeIfAbsent(key(processCode, tenant, country), k -> new ArrayList<>())
                .add(new Binding(slotCode, actionCode, sequence));
    }

    /**
     * 解析出 slotCode → 按 sequence 升序的 ActionPoint 列表。
     */
    public Map<String, List<ActionPoint>> resolve(String processCode, String tenant, String country,
                                                  ActionRegistry actions) {
        List<Binding> bs = configs.get(key(processCode, tenant, country));
        if (bs == null) {
            bs = Collections.emptyList();
        }
        List<Binding> sorted = new ArrayList<>(bs);
        sorted.sort(Comparator.comparingInt(b -> b.sequence));

        Map<String, List<ActionPoint>> map = new LinkedHashMap<>();
        for (Binding b : sorted) {
            map.computeIfAbsent(b.slotCode, k -> new ArrayList<>()).add(actions.get(b.actionCode));
        }
        return map;
    }
}
