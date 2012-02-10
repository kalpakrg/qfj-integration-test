package org.archenos.qfj.test.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.field.MsgType;

public class MultiMessageListener extends AbstractWaitableApplicationAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractWaitableApplicationAdapter.class);
    private final List<String> msgTypes;
    private final Direction dir;
    private final List<Message> messages;
    private final int count;

    public MultiMessageListener(String msgType, Direction dir, int count) {
        this(Arrays.asList(new String[] { msgType }), dir, count);
    }

    public MultiMessageListener(List<String> msgTypes, Direction dir, int count) {
        super(count);
        this.messages = new ArrayList<Message>(count);
        this.msgTypes = new ArrayList<String>(msgTypes);
        this.dir = dir;
        this.count = count;
    }

    public Message [] awaitMessages(long timeout, TimeUnit unit) throws InterruptedException {
        await(timeout, unit);
        return messages.toArray(new Message[messages.size()]);
    }

    private void processMessage(Message message, Direction handleDir) {
        if (messages.size() == count) {
            return;
        }

        if (dir == handleDir) {
            try {
                String type = message.getHeader().getField(new MsgType()).getValue();

                if (msgTypes.contains(type)) {
                    messages.add(message);
                    signal();
                }
                
            } catch (FieldNotFound e) {
                // unlikely
                LOG.warn("Field MsgType could not be read.", e);
            }
        }
    }
    
    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        processMessage(message, Direction.OUT);
    }


    @Override
    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        processMessage(message, Direction.IN);
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        processMessage(message, Direction.OUT);
    }

    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
            UnsupportedMessageType {
        processMessage(message, Direction.IN);
    }
}
