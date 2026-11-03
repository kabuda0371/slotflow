package dev.slotflow.demo;

import dev.slotflow.core.ISlot;

/**
 * 卡槽能力接口：订单数据补全。
 *
 * <p>对应原设计中的 {@code ICompletionSlot}。
 */
public interface EnrichSlot extends ISlot {

    void enrich(Order order);
}
