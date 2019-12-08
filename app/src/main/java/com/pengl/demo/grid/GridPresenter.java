package com.pengl.demo.grid;

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
class GridPresenter {

    private CompositeDisposable mCompositeDisposable;

    private static int count = -1;
    private GridView mView;

    GridPresenter(Context context) {
        mCompositeDisposable = new CompositeDisposable();
    }

    void setDataLoadCallBack(GridView gridView) {
        mView = gridView;
    }

    void unsubscribeAll() {
        mCompositeDisposable.clear();
    }

    void loadData(final boolean isRefresh) {
        Disposable disposable = createObservable() //
                .subscribeOn(Schedulers.io())//
                .delay(2, TimeUnit.SECONDS)//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Consumer<List<GridBean>>() {
                    @Override
                    public void accept(List<GridBean> list) throws Exception {
                        // 这里接收数据项，相当于onNext
                        for (GridBean b : list) {
                            Log.v("info", "----->" + b.text);
                        }
                        mView.onDataLoadSuccess(list, isRefresh);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 这里接收onError
                        throwable.printStackTrace();
                        mView.onDataLoadFailed(isRefresh);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        // 这里接收onComplete。
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private Observable<List<GridBean>> createObservable() {
        count++;
        count %= 6;
        return Observable.create(new ObservableOnSubscribe<List<GridBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<GridBean>> subscriber) throws Exception {

                if (count == 3) {
                    subscriber.onError(new Throwable("on error"));
                    return;
                }
                if (count == 5) {
                    subscriber.onNext(new ArrayList<GridBean>());
                    return;
                }
                List<GridBean> mData = new ArrayList<>();
                for (int i = count * 5; i < count * 5 + 2; i++) {
                    GridBean bean = new GridBean(i + "");
                    mData.add(bean);
                }
                subscriber.onNext(mData);
                subscriber.onComplete();

            }
        });
    }
}
