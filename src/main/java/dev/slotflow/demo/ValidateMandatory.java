package dev.slotflow.demo;

import dev.slotflow.core.Action;
import dev.slotflow.core.ActionType;

/**
 * 业务点：订单必填校验（标品动作，所有国家装配）。
 *
 * <p>对应原设计中带 {@code @ActionPointAnnotation} 的 {@code VerifySlot} 实现。
 * 注意：它只声明 {@code code=validateMandatory}，挂到哪个卡槽、按什么顺序由 {@code ProcessRegistry} 决定。
 */
public class ValidateMandatory implements VerifySlot {

    @Override
    @Action(type = ActionType.VERIFY, code = "validateMandatory", name = "订单必填校验")
    public void verify(Order order) {
        if (order.getAmount() <= 0) {
            throw new IllegalStateException("订单金额必须大于 0: " + order.getId());
        }
        System.out.println("    [validateMandatory] 通过: " + order.getId());
    }
}
