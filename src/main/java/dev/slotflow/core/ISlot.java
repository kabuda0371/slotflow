package dev.slotflow.core;

/**
 * 卡槽（Slot）标记接口。
 *
 * <p>所有卡槽能力接口（例如 {@code VerifySlot}、{@code EnrichSlot}）都 {@code extends} 本接口，
 * 对应原设计中的 {@code com.yonyou.loc.workbench.slot.ISlot}。
 *
 * <p>卡槽是一个“具名的可扩展点/接缝”：业务链在固定位置调用卡槽，但卡槽后面挂哪些动作（Action）
 * 由运行态配置决定，与业务链代码解耦。
 */
public interface ISlot {
}
