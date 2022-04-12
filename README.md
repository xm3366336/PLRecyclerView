# PLRecyclerView
给自个儿用的RecyclerView
[![](https://jitpack.io/v/xm3366336/PLRecyclerView.svg)](https://jitpack.io/#xm3366336/PLRecyclerView)

> 1、使用

```
implementation 'com.github.xm3366336:PLRecyclerView:2.0.2'
```

> 2、Demo下载
* [Google Play](https://play.google.com/store/apps/details?id=com.pengl.PLRecyclerView.demo)

> 3、更新日志

## Update 20220412
1. 升级gradle；
2. 以后只用com.pengl.PLRecyclerview；
3. 删除了上个版本标记的过时方法；
4. 修改ExpandActivity，默认展开某项；

## Update 20210523
1. 去除JCenter，替换为jitpack.io；
2. 由原来的com.pengl.PLRecyclerView.PLRecyclerView，换成com.pengl.PLRecyclerView；
3. 由包名更换，版本改为2.0.0，删除了上个版本标记的过时方法，请使用get().xxx替代。

## Update 20200722
1. 重新整理Demo；
2. 去除ConfigureError和ConfigureEmpty接口，使用ConfigureAdapter替代；
3. 插入或移除数据，做保护措施，避免越界；
4. 标记一些过时方法，将在下个版本移除；
5. ui稍做些调整；

## Update 20200202
1. 迁移至AndroidX；

## Update 20191208
1. gradle up to 5.4.1；
2. 更新demo；
3. 修改默认的几个layout，用dp代替sp；
4. 下拉刷新后，支持手动刷新状态；

## Update 20181129
1. compileSdkVersion up to 28
2. gradle up to 4.6
3. 换两个图标

## Update 20180828
1. compileSdkVersion up to 27
2. gradle up to 3.1.4

## Update 20180201
1. 新增接口showEmpty(int resId, String content);
2. 新增接口showError(int resId, String content);
3. 替换一些资源。

## Update 20170612
1. 去除 android:allowBackup="true"

## Update 20170325
1. 新增接口showError(String error);
2. 直接提供错误原因，避免引用时一堆的代码。
3. 如果使用了自定义的view，避如，app:error_layout="@layout/pl_listview_error"，那么需包含id为tv_content的TextView才会显示。

## Update 20170314
1. 新增接口AbstractAdapter.showEmpty();