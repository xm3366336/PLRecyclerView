package com.pengl.demo.expand;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.pengl.demo.R;

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
class ExpandPresenter {

    private CompositeDisposable mCompositeDisposable;

    private ExpandView mView;
    private Context mContext;

    ExpandPresenter(Context context) {
        mContext = context;
        mCompositeDisposable = new CompositeDisposable();
    }

    void setDataLoadCallBack(ExpandView gridView) {
        mView = gridView;
    }

    void unsubscribeAll() {
        mCompositeDisposable.clear();
    }

    void loadData() {
        Disposable disposable = createObservable() //
                .subscribeOn(Schedulers.io())//
                .delay(2, TimeUnit.SECONDS)//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Consumer<MockResponse>() {
                    @Override
                    public void accept(MockResponse bean) throws Exception {
                        mView.onDataLoadSuccess(bean.mData);
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

    private Observable<MockResponse> createObservable() {
        return Observable.create(new ObservableOnSubscribe<MockResponse>() {
            @Override
            public void subscribe(ObservableEmitter<MockResponse> subscriber) throws Exception {

                Resources res = mContext.getResources();
                final String mockResponse = res.getString(R.string.mock_response);
                MockResponse response = new Gson().fromJson(mockResponse, MockResponse.class);

                subscriber.onNext(response);
                subscriber.onComplete();
            }
        });
    }
}
