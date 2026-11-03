package dev.slotflow.core;

/**
 * 卡槽（Seam）描述：一个具名的可扩展点。
 *
 * <p>对应原设计中的 {@code SlotEnumEntity}。业务域通常用一个枚举实现本接口（见
 * {@code dev.slotflow.demo.SlotDef}），枚举常量携带：
 * <ul>
 *   <li>{@code code}：卡槽编码，也是运行时的路由 key；</li>
 *   <li>{@code name}：卡槽名称；</li>
 *   <li>{@code contract}：该卡槽的能力接口类型（{@code extends ISlot}），用于类型化取动作。</li>
 * </ul>
 */
public interface Seam {

    String code();

    String name();

    Class<? extends ISlot> contract();
}
