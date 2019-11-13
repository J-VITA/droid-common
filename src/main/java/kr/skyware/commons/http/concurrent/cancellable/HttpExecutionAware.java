package kr.skyware.commons.http.concurrent.cancellable;

import kr.skyware.commons.http.concurrent.Cancellable;

public interface HttpExecutionAware {

    boolean isAborted();

    /**
     * Sets {@link Cancellable} for the ongoing operation.
     */
    void setCancellable(Cancellable cancellable);

}

