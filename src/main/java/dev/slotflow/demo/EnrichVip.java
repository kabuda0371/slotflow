package dev.slotflow.demo;

import dev.slotflow.core.Action;
import dev.slotflow.core.ActionType;

/**
 * 业务点：VIP 补全（仅对 MY 装配，展示“不改业务链、只改配置即可插拔扩展”）。
 */
public class EnrichVip implements EnrichSlot {

    @Override
    @Action(type = ActionType.ENRICH, code = "enrichVip", name = "VIP 补全")
    public void enrich(Order order) {
        order.setDiscountLevel("VIP");
        System.out.println("    [enrichVip] discountLevel=VIP");
    }
}
