package dev.slotflow.demo;

import dev.slotflow.core.ActionRegistry;
import dev.slotflow.core.Flow;
import dev.slotflow.core.ProcessRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FlowTest {

    private ActionRegistry actions() {
        ActionRegistry a = new ActionRegistry();
        a.register(new ValidateMandatory());
        a.register(new ValidateTax());
        a.register(new EnrichDefaults());
        a.register(new EnrichVip());
        return a;
    }

    private ProcessRegistry config() {
        ProcessRegistry c = new ProcessRegistry();
        c.bind("placeOrder", "T0", "SG", SlotDef.VERIFY.code(), "validateMandatory", 1);
        c.bind("placeOrder", "T0", "SG", SlotDef.VERIFY.code(), "validateTax", 2);
        c.bind("placeOrder", "T0", "SG", SlotDef.ENRICH.code(), "enrichDefaults", 1);

        c.bind("placeOrder", "T0", "MY", SlotDef.VERIFY.code(), "validateMandatory", 1);
        c.bind("placeOrder", "T0", "MY", SlotDef.ENRICH.code(), "enrichDefaults", 1);
        c.bind("placeOrder", "T0", "MY", SlotDef.ENRICH.code(), "enrichVip", 2);
        return c;
    }

    @Test
    void sgRunsValidateThenEnrich() {
        ActionRegistry a = actions();
        ProcessRegistry c = config();
        PlaceOrderProcess chain = new PlaceOrderProcess();

        Order sg = new Order("SG-1");
        sg.setCountry("SG");
        sg.setAmount(100);
        sg.setTaxRate(0.07);

        Flow.run("placeOrder", "T0", "SG", c, a, () -> chain.process(new OrderContext("T0", "SG", sg)));

        assertEquals("VALIDATED", sg.getStatus());
        assertEquals(0.07, sg.getTaxRate(), 1e-9);
        assertEquals("NONE", sg.getDiscountLevel()); // SG 未挂 VIP 补全
    }

    @Test
    void myRunsEnrichDefaultsThenVip() {
        ActionRegistry a = actions();
        ProcessRegistry c = config();
        PlaceOrderProcess chain = new PlaceOrderProcess();

        Order my = new Order("MY-1");
        my.setCountry("MY");
        my.setAmount(50);

        Flow.run("placeOrder", "T0", "MY", c, a, () -> chain.process(new OrderContext("T0", "MY", my)));

        assertEquals("VALIDATED", my.getStatus());
        assertEquals(0.05, my.getTaxRate(), 1e-9); // 默认税率
        assertEquals("VIP", my.getDiscountLevel());
    }

    @Test
    void mandatoryFailsOnNonPositiveAmount() {
        ActionRegistry a = actions();
        ProcessRegistry c = config();
        PlaceOrderProcess chain = new PlaceOrderProcess();

        Order bad = new Order("BAD-1");
        bad.setCountry("MY");
        bad.setAmount(0);

        assertThrows(IllegalStateException.class, () ->
                Flow.run("placeOrder", "T0", "MY", c, a, () -> chain.process(new OrderContext("T0", "MY", bad))));
    }

    @Test
    void flowContextIsClearedAfterRun() {
        ActionRegistry a = actions();
        ProcessRegistry c = config();
        PlaceOrderProcess chain = new PlaceOrderProcess();

        Order sg = new Order("SG-2");
        sg.setCountry("SG");
        sg.setAmount(1);
        sg.setTaxRate(0.07);

        Flow.run("placeOrder", "T0", "SG", c, a, () -> chain.process(new OrderContext("T0", "SG", sg)));

        assertEquals(null, dev.slotflow.core.FlowContext.peek());
    }
}
