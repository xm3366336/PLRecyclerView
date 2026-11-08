package com.pengl.recyclerview;

import androidx.annotation.NonNull;

import java.util.Observer;

/**
 * 旧版 {@link java.util.Observer} 到新版 {@link DataSetObserver} 的适配器。
 * <p>
 * {@link java.util.Observer} 与 {@link java.util.Observable} 自 Java 9 起已被标记为废弃。
 * 本库内部不再使用它们，仅在 {@link AbstractAdapter#registerObserver(Observer)} 这一兼容入口上
 * 使用，以便旧版本的使用者无需修改代码即可继续工作。
 *
 * @author pengl
 */
@SuppressWarnings("deprecation")
class LegacyObserver implements DataSetObserver {

    private final Observer mObserver;

    LegacyObserver(@NonNull Observer observer) {
        mObserver = observer;
    }

    @Override
    public void onDataSetChanged(@NonNull Bridge bridge) {
        // 旧版 Observer.update 的第一个参数原本是 Observable 实例，
        // 而 Observable 已废弃且该实例原本为包内私有类型，对使用者没有意义，
        // 因此这里传入 null，第二个参数（Bridge）保持不变。
        mObserver.update(null, bridge);
    }
}
