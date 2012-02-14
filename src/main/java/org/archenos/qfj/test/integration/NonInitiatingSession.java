package org.archenos.qfj.test.integration;

import java.util.concurrent.TimeUnit;

import quickfix.Acceptor;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultSessionFactory;
import quickfix.MemoryStoreFactory;
import quickfix.Message;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionFactory;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.ThreadedSocketAcceptor;

public final class NonInitiatingSession {
    private final NonInitiatingSessionConfig config;
    private Acceptor acceptor;
    private IntegrationTestApplication app = new IntegrationTestApplication();
    
    public NonInitiatingSession(NonInitiatingSessionConfig config) {
        super();
        this.config = config;
    }

    public void addListner(Application listener) {
        app.addListener(listener);
    }

    public void removeListener(Application listener) {
        app.removeListener(listener);
    }

    public void send(Message msg) throws SessionNotFound {
        Session.sendToTarget(msg, config.getSenderCompId(), config.getTargetCompId());
    }

    public void awaitAccept(long timeout, TimeUnit unit) throws ConfigError, InterruptedException {
        SessionFactory sessionFactory = new DefaultSessionFactory(app, new MemoryStoreFactory(),
                new ScreenLogFactory(true, true, true));

        SessionSettings settings = config.getRawSettings();

        LogonListener listener = new LogonListener();
        app.addListener(listener);
        acceptor = new ThreadedSocketAcceptor(sessionFactory, settings);
        acceptor.start();

        try {
            listener.awaitLogon(timeout, unit);
        } finally {
            app.removeListener(listener);
        }
    }

    public void stop() {
        acceptor.stop(false);
    }
}
