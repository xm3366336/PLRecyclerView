package com.pengl.demo.drag;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */
class DragPresenter {

    private CompositeDisposable mCompositeDisposable;
    private DragView mView;

    DragPresenter(Context context) {
        mCompositeDisposable = new CompositeDisposable();
    }

    void setDataLoadCallBack(DragView view) {
        mView = view;
    }

    void unsubscribeAll() {
        mCompositeDisposable.clear();
    }

    void loadData() {
        Disposable disposable = createObservable() //
                .subscribeOn(Schedulers.io())//
                .delay(1, TimeUnit.SECONDS)//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Consumer<List<DragBean>>() {
                    @Override
                    public void accept(List<DragBean> list) throws Exception {
                        // 这里接收数据项，相当于onNext
                        for (DragBean b : list) {
                            Log.v("info", "----->" + b.text);
                        }
                        mView.onDataLoadSuccess(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 这里接收onError
                        throwable.printStackTrace();
                        mView.onDataLoadFailed();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        // 这里接收onComplete。
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private Observable<List<DragBean>> createObservable() {
        return Observable.create(new ObservableOnSubscribe<List<DragBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<DragBean>> subscriber) throws Exception {

                List<DragBean> mData = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    DragBean bean = new DragBean(i + "");
                    mData.add(bean);
                }

                subscriber.onNext(mData);
                subscriber.onComplete();
            }
        });
    }
}
