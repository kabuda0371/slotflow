package dev.slotflow.demo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 业务上下文（数据袋）：贯穿整条业务链，供各动作读写共享中间数据。
 *
 * <p>对应原设计中的 {@code WorkBenchContext}（含 {@code extData}）。
 */
public class OrderContext {

    private final String tenant;
    private final String country;
    private final Order order;
    private final Map<String, Object> extras = new LinkedHashMap<>();

    public OrderContext(String tenant, String country, Order order) {
        this.tenant = tenant;
        this.country = country;
        this.order = order;
    }

    public OrderContext put(String key, Object value) {
        extras.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) extras.get(key);
    }

    public String getTenant() {
        return tenant;
    }

    public String getCountry() {
        return country;
    }

    public Order getOrder() {
        return order;
    }
}
