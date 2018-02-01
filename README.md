# PLRecyclerView
给自个儿用的RecyclerView
主要目的：
1. 练习Github和JCenter仓库的使用；
2. 自己用；

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