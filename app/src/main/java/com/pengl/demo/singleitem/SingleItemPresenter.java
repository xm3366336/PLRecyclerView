package com.pengl.demo.singleitem;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.pengl.demo.R;

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
class SingleItemPresenter {

    private CompositeDisposable mCompositeDisposable;

    private static int count = -1;
    private SingleItemView mView;
    private Context mContext;

    SingleItemPresenter(Context context) {
        mContext = context;
        mCompositeDisposable = new CompositeDisposable();
    }

    void setDataLoadCallBack(SingleItemView singleItemView) {
        mView = singleItemView;
    }

    void unsubscribeAll() {
        mCompositeDisposable.clear();
    }

    void loadData(final boolean isRefresh) {
        Disposable disposable = createObservable() //
                .subscribeOn(Schedulers.io())//
                .delay(2, TimeUnit.SECONDS)//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Consumer<List<NormalBean>>() {
                    @Override
                    public void accept(List<NormalBean> list) throws Exception {
                        // 这里接收数据项，相当于onNext
                        for (NormalBean b : list) {
                            Log.v("info", "----->" + b.mTitle);
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

    private Observable<List<NormalBean>> createObservable() {
        count++;
        count %= 6;
        Resources res = mContext.getResources();
        final String[] images = res.getStringArray(R.array.image);
        final String[] titles = res.getStringArray(R.array.title);
        final String[] contents = res.getStringArray(R.array.content);
        return Observable.create(new ObservableOnSubscribe<List<NormalBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NormalBean>> subscriber) throws Exception {

                if (count == 3) {
                    subscriber.onError(new Throwable("on error"));
                    return;
                }
                if (count == 5) {
                    subscriber.onNext(new ArrayList<NormalBean>());
                    return;
                }
                List<NormalBean> mData = new ArrayList<>();
                for (int i = count * 5; i < count * 5 + 2; i++) {
                    NormalBean bean = new NormalBean(images[i], titles[i], contents[i]);
                    mData.add(bean);
                }

                subscriber.onNext(mData);
                subscriber.onComplete();
            }
        });
    }

}
