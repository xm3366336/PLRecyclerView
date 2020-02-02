package com.pengl.demo.multipleitem;

import android.content.Context;
import android.util.Log;

import com.pengl.PLRecyclerView.ItemType;

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
class MultiItemPresenter {

    private CompositeDisposable mCompositeDisposable;

    private static int count = -1;
    private MultiItemView mView;

    MultiItemPresenter(Context context) {
        mCompositeDisposable = new CompositeDisposable();
    }

    void setDataLoadCallBack(MultiItemView itemView) {
        mView = itemView;
    }

    void unsubscribeAll() {
        mCompositeDisposable.clear();
    }

    void loadData(final boolean isRefresh) {
        Disposable disposable = createObservable() //
                .subscribeOn(Schedulers.io())//
                .delay(1, TimeUnit.SECONDS)//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Consumer<List<ItemType>>() {
                    @Override
                    public void accept(List<ItemType> list) throws Exception {
                        // 这里接收数据项，相当于onNext
                        for (ItemType b : list) {
                            Log.v("info", "----->" + b.itemType());
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

    private Observable<List<ItemType>> createObservable() {
        count++;
        count %= 6;

        return Observable.create(new ObservableOnSubscribe<List<ItemType>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ItemType>> subscriber) throws Exception {

                if (count == 3) {
                    subscriber.onError(new Throwable("on error"));
                    return;
                }
                if (count == 5) {
                    subscriber.onNext(new ArrayList<ItemType>());
                    return;
                }
                List<ItemType> mData = new ArrayList<>();
                for (int i = count * 5; i < count * 5 + 2; i++) {
                    if (i % 2 == 0) {
                        ItemType bean = new TypeOneBean("type one");
                        mData.add(bean);
                    } else {
                        ItemType bean = new TypeTwoBean("type two");
                        mData.add(bean);
                    }
                }

                subscriber.onNext(mData);
                subscriber.onComplete();
            }
        });
    }
}
