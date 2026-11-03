package dev.slotflow.demo;

import dev.slotflow.core.ISlot;

/**
 * 卡槽能力接口：订单校验。
 *
 * <p>对应原设计中的 {@code IVerifySlot<T>}。卡槽只约定“能做什么”，不关心“谁来做/做几个”。
 */
public interface VerifySlot extends ISlot {

    void verify(Order order);
}
