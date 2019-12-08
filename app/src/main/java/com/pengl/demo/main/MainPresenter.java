package com.pengl.demo.main;

import android.content.Context;
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
class MainPresenter {

    private CompositeDisposable mCompositeDisposable;
    private Context mContext;
    private MainView mMainView;

    MainPresenter(Context context) {
        mContext = context;
        mCompositeDisposable = new CompositeDisposable();
    }

    void setDataLoadCallBack(MainView mainView) {
        mMainView = mainView;
    }

    void unsubscribeAll() {
        mCompositeDisposable.clear();
    }

    void loadData() {
        Disposable disposable = createObservable() //
                .subscribeOn(Schedulers.io())//
                .delay(2, TimeUnit.SECONDS)//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Consumer<List<MenuBean>>() {
                    @Override
                    public void accept(List<MenuBean> menuBeans) throws Exception {
                        // 这里接收数据项，相当于onNext
                        for (MenuBean b : menuBeans) {
                            Log.v("info", "----->" + b.getMenu());
                        }
                        mMainView.onLoadSuccess(menuBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 这里接收onError
                        mMainView.onLoadFailed();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        // 这里接收onComplete。
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private Observable<List<MenuBean>> createObservable() {
        final String[] resource = mContext.getResources().getStringArray(R.array.main_fun);

        return Observable.create(new ObservableOnSubscribe<List<MenuBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MenuBean>> emitter) {
                List<MenuBean> mData = new ArrayList<>();
                for (int i = 0; i < resource.length; i++) {
                    mData.add(new MenuBean(resource[i], i));
                }

                emitter.onNext(mData);
                emitter.onComplete();
            }
        });
    }
}
