# PLRecyclerView

fork for PracticalRecyclerView
详见 <https://github.com/ssseasonnn/PracticalRecyclerView>


## 目的
1. 练习Github和JCenter仓库的使用；
2. 因原项目没啥动作了，逐步封装自个儿用；

## Update 20170612
去除 android:allowBackup="true"

## Update 20170325
新增接口showError(String error);
直接提供错误原因，避免引用时一堆的代码。
如果使用了自定义的view，避如，app:error_layout="@layout/pl_listview_error"，那么需包含id为tv_content的TextView才会显示。

## Update 20170314
新增接口AbstractAdapter.showEmpty();