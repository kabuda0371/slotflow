package dev.slotflow.demo;

import dev.slotflow.core.ActionRegistry;
import dev.slotflow.core.Flow;
import dev.slotflow.core.ProcessRegistry;

/**
 * 可执行演示：装配动作 + 配置运行态装配 + 跑两条业务链（SG / MY）。
 *
 * <pre>
 *   javac -d out $(find src/main/java -name '*.java')
 *   java -cp out dev.slotflow.demo.Demo
 * </pre>
 */
public class Demo {

    public static void main(String[] args) {
        // 1) 注册动作（对应原设计：启动时扫描 Spring 容器里带 @ActionPointAnnotation 的 bean）
        ActionRegistry actions = new ActionRegistry();
        actions.register(new ValidateMandatory());
        actions.register(new ValidateTax());
        actions.register(new EnrichDefaults());
        actions.register(new EnrichVip());

        // 2) 配置运行态装配（对应原设计：fi_workbench_process_config / action_config）
        ProcessRegistry config = new ProcessRegistry();

        // SG：校验 = 必填 + 税金；补全 = 默认
        config.bind("placeOrder", "T0", "SG", SlotDef.VERIFY.code(), "validateMandatory", 1);
        config.bind("placeOrder", "T0", "SG", SlotDef.VERIFY.code(), "validateTax", 2);
        config.bind("placeOrder", "T0", "SG", SlotDef.ENRICH.code(), "enrichDefaults", 1);

        // MY：校验 = 必填；补全 = 默认 + VIP
        config.bind("placeOrder", "T0", "MY", SlotDef.VERIFY.code(), "validateMandatory", 1);
        config.bind("placeOrder", "T0", "MY", SlotDef.ENRICH.code(), "enrichDefaults", 1);
        config.bind("placeOrder", "T0", "MY", SlotDef.ENRICH.code(), "enrichVip", 2);

        PlaceOrderProcess chain = new PlaceOrderProcess();

        // 3) 跑 SG
        Order sg = new Order("SG-1");
        sg.setCountry("SG");
        sg.setAmount(100);
        sg.setTaxRate(0.07);
        Flow.run("placeOrder", "T0", "SG", config, actions,
                () -> chain.process(new OrderContext("T0", "SG", sg)));

        // 4) 跑 MY（amount=50，税率交给补全卡槽填默认值）
        Order my = new Order("MY-1");
        my.setCountry("MY");
        my.setAmount(50);
        Flow.run("placeOrder", "T0", "MY", config, actions,
                () -> chain.process(new OrderContext("T0", "MY", my)));

        System.out.println();
        System.out.println("最终: SG=" + sg.getStatus() + " (税率 " + sg.getTaxRate() + ")");
        System.out.println("最终: MY=" + my.getStatus() + " (税率 " + my.getTaxRate()
                + ", 折扣档 " + my.getDiscountLevel() + ")");
    }
}
