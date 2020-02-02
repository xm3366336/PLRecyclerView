# PLRecyclerView
给自个儿用的RecyclerView

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