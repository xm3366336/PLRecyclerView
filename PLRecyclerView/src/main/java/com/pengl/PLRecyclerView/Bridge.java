package com.pengl.PLRecyclerView;

/**
 * Author: Season(ssseasonnn@gmail.com)
 * Date: 2016/9/28
 * Time: 14:35
 * FIXME
 */
interface Bridge {
    void doSomething(RecyclerView host);

    class Loading implements Bridge {

        @Override
        public void doSomething(RecyclerView host) {
            host.displayLoadingAndResetStatus();
        }
    }

    class Empty implements Bridge {

        @Override
        public void doSomething(RecyclerView host) {
            host.displayEmptyAndResetStatus();
        }
    }

    class Content implements Bridge {

        @Override
        public void doSomething(RecyclerView host) {
            host.displayContentAndResetStatus();
        }
    }

    class Error implements Bridge {

        @Override
        public void doSomething(RecyclerView host) {
            host.displayErrorAndResetStatus();
        }
    }

    class NoMore implements Bridge {

        @Override
        public void doSomething(RecyclerView host) {
            host.showNoMoreIfEnabled();
        }
    }

    class LoadMoreFailed implements Bridge {

        @Override
        public void doSomething(RecyclerView host) {
            host.showLoadMoreFailedIfEnabled();
        }
    }

    class ResumeLoadMore implements Bridge {

        @Override
        public void doSomething(RecyclerView host) {
            host.resumeLoadMoreIfEnabled();
        }
    }

    class AutoLoadMore implements Bridge {

        @Override
        public void doSomething(RecyclerView host) {
            host.autoLoadMoreIfEnabled();
        }
    }

    class ManualLoadMore implements Bridge {

        @Override
        public void doSomething(RecyclerView host) {
            host.manualLoadMoreIfEnabled();
        }
    }

    class SwipeConflicts implements Bridge {

        private boolean enabled;

        SwipeConflicts(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public void doSomething(RecyclerView host) {
            host.resolveSwipeConflicts(enabled);
        }
    }
}