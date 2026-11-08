package com.pengl.recyclerview;

import androidx.annotation.NonNull;

/**
 * 数据集变化观察者。
 * <p>
 * 用于替代已废弃的 {@link java.util.Observable} / {@link java.util.Observer} 组合，
 * 避免在 {@code compileSdk} 较高时产生 "java.util.Observable 已弃用" 的告警。
 * <p>
 * 该接口与旧的 {@code java.util.Observer} 不兼容，若需要兼容旧代码，
 * 请使用 {@code AbstractAdapter#registerObserver(java.util.Observer)}（已标记废弃），
 * 新代码请直接实现本接口。
 *
 * @author pengl
 */
public interface DataSetObserver {

    /**
     * 当数据集状态发生变化时回调。
     *
     * @param bridge 描述本次变化的桥接对象，不可为 null
     */
    void onDataSetChanged(@NonNull Bridge bridge);
}
