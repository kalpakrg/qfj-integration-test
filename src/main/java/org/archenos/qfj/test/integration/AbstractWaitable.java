package org.archenos.qfj.test.integration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class AbstractWaitable implements Waitable {
    private final CountDownLatch latch;

    protected AbstractWaitable(int count) {
        latch = new CountDownLatch(count);
    }

    @Override
    public final void await(long timeout, TimeUnit unit) throws InterruptedException {
        if (!latch.await(timeout, unit)) {
            throw new TimedOutError();
        }
    }

    @Override
    public final void signal() {
        latch.countDown();
    }
}
