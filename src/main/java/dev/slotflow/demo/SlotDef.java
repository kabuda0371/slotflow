package dev.slotflow.demo;

import dev.slotflow.core.ISlot;
import dev.slotflow.core.Seam;

/**
 * 本业务域的卡槽枚举。
 *
 * <p>对应原设计中的 {@code SlotEnumEntity}：每个常量 = 卡槽编码 + 名称 + 能力接口类型。
 * 业务链通过 {@code Flow.actions(SlotDef.VERIFY, VerifySlot.class)} 按编码取动作。
 */
public enum SlotDef implements Seam {

    VERIFY("verify", "订单校验", VerifySlot.class),
    ENRICH("enrich", "订单补全", EnrichSlot.class);

    private final String code;
    private final String name;
    private final Class<? extends ISlot> contract;

    SlotDef(String code, String name, Class<? extends ISlot> contract) {
        this.code = code;
        this.name = name;
        this.contract = contract;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Class<? extends ISlot> contract() {
        return contract;
    }
}
