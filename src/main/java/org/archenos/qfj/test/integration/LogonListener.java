package org.archenos.qfj.test.integration;

import java.util.concurrent.TimeUnit;

import quickfix.SessionID;


public final class LogonListener extends AbstractWaitableApplicationAdapter {

    public LogonListener() {
        super(1);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        signal();
    }

    public void awaitLogon(long timeout, TimeUnit unit) throws InterruptedException {
        await(timeout, unit);
    }
}
