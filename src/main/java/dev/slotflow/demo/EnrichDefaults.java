package dev.slotflow.demo;

import dev.slotflow.core.Action;
import dev.slotflow.core.ActionType;

/**
 * 业务点：默认数据补全（所有国家装配）。
 */
public class EnrichDefaults implements EnrichSlot {

    @Override
    @Action(type = ActionType.ENRICH, code = "enrichDefaults", name = "默认补全")
    public void enrich(Order order) {
        if (order.getTaxRate() == null) {
            order.setTaxRate(0.05); // 默认 5%
        }
        order.setStatus("VALIDATED");
        System.out.println("    [enrichDefaults] 默认税率=5%, status=VALIDATED");
    }
}
