package dev.slotflow.demo;

import dev.slotflow.core.Action;
import dev.slotflow.core.ActionType;

/**
 * 业务点：税金校验（仅对 SG 装配，展示“按国家差异化装配”）。
 */
public class ValidateTax implements VerifySlot {

    @Override
    @Action(type = ActionType.VERIFY, code = "validateTax", name = "税金校验")
    public void verify(Order order) {
        if (order.getTaxRate() == null) {
            throw new IllegalStateException("SG 订单缺少税率: " + order.getId());
        }
        System.out.println("    [validateTax] 税率=" + order.getTaxRate());
    }
}
