package dev.slotflow.demo;

import dev.slotflow.core.Flow;

/**
 * 业务链：下单。
 *
 * <p>对应原设计中带 {@code @ProcessAnnotation(code="placeOrder", slots={VERIFY, ENRICH})} 的链方法。
 * 链体只做两件事：按卡槽顺序走步骤，并在每个卡槽处取“已装配、已排序”的动作逐个执行。
 * 具体挂哪些动作、顺序如何，链体完全不感知。
 */
public class PlaceOrderProcess {

    public void process(OrderContext ctx) {
        Order order = ctx.getOrder();
        System.out.println("== placeOrder 开始 " + order.getId() + " (country=" + ctx.getCountry() + ") ==");

        // 卡槽 1：校验
        for (VerifySlot a : Flow.actions(SlotDef.VERIFY, VerifySlot.class)) {
            a.verify(order);
        }

        // 卡槽 2：补全
        for (EnrichSlot a : Flow.actions(SlotDef.ENRICH, EnrichSlot.class)) {
            a.enrich(order);
        }

        System.out.println("== placeOrder 结束 status=" + order.getStatus()
                + ", discountLevel=" + order.getDiscountLevel() + " ==");
    }
}
