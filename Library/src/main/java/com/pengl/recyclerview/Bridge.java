package com.pengl.recyclerview;

import com.pengl.PLRecyclerView;

/**
 *
 */
public interface Bridge {
    void doSomething(PLRecyclerView host);

    class Loading implements Bridge {
        @Override
        public void doSomething(PLRecyclerView host) {
            host.displayLoadingAndResetStatus();
        }
    }

    class Empty implements Bridge {
        private final int resId;
        private final String content;

        public Empty(int resId, String content) {
            this.resId = resId;
            this.content = content;
        }

        @Override
        public void doSomething(PLRecyclerView host) {
            host.displayEmptyAndResetStatus(resId, content);
        }
    }

    class Content implements Bridge {
        @Override
        public void doSomething(PLRecyclerView host) {
            host.displayContentAndResetStatus();
        }
    }

    class Error implements Bridge {
        private final int resId;
        private final String err;

        public Error(int resId, String err) {
            this.resId = resId;
            this.err = err;
        }

        @Override
        public void doSomething(PLRecyclerView host) {
            host.displayErrorAndResetStatus(resId, err);
        }
    }

    class NoMore implements Bridge {
        @Override
        public void doSomething(PLRecyclerView host) {
            host.showNoMoreIfEnabled();
        }
    }

    class LoadMoreFailed implements Bridge {
        @Override
        public void doSomething(PLRecyclerView host) {
            host.showLoadMoreFailedIfEnabled();
        }
    }

    class ResumeLoadMore implements Bridge {
        @Override
        public void doSomething(PLRecyclerView host) {
            host.resumeLoadMoreIfEnabled();
        }
    }

    class AutoLoadMore implements Bridge {
        @Override
        public void doSomething(PLRecyclerView host) {
            host.autoLoadMoreIfEnabled();
        }
    }

    class ManualLoadMore implements Bridge {
        @Override
        public void doSomething(PLRecyclerView host) {
            host.manualLoadMoreIfEnabled();
        }
    }

    class SwipeConflicts implements Bridge {

        private final boolean enabled;

        SwipeConflicts(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public void doSomething(PLRecyclerView host) {
            host.resolveSwipeConflicts(enabled);
        }
    }
}