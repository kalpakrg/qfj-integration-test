package org.archenos.qfj.test.integration;

import java.util.concurrent.TimeUnit;

public interface Waitable {
    void await(long timeout, TimeUnit unit) throws InterruptedException, TimedOutError;

    void signal();
}
