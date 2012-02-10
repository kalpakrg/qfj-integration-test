package org.archenos.qfj.test.integration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import quickfix.Message;

public final class SingleMessageListener extends MultiMessageListener {
    public SingleMessageListener(String msgType, Direction dir) {
        super(Arrays.asList(new String[] { msgType }), dir, 1);
    }

    public Message awaitMessage(long timeout, TimeUnit unit) throws InterruptedException {
        Message [] msgs = awaitMessages(timeout, unit);
        return msgs[0];
    }
}
