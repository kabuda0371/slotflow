package dev.slotflow.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务点（Action）注解：标记某个实现方法是一个“动作”。
 *
 * <p>对应原设计中的 {@code ActionPointAnnotation}。语义：
 * <ul>
 *   <li>{@code code}：全局稳定的业务点编码，是运行时装配的唯一标识（对应 {@code fi_workbench_action.code}）；</li>
 *   <li>{@code type}：业务点类型；</li>
 *   <li>{@code name}：展示名，缺省取 {@code code}。</li>
 * </ul>
 *
 * <p>注意：动作“挂在哪个卡槽、以什么顺序执行”不写在这里，而是写进运行态配置
 * （{@link ProcessRegistry}），对应原设计的 {@code fi_workbench_action_config}（slotCode + actionCode + sequence）。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {

    ActionType type();

    String code();

    String name() default "";
}
