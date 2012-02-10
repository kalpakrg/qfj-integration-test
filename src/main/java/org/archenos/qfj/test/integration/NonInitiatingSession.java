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
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.ThreadedSocketAcceptor;

public final class NonInitiatingSession {
    private final SessionConfig config;
    private Acceptor acceptor;
    private IntegrationTestApplication app = new IntegrationTestApplication();
    
    public NonInitiatingSession(SessionConfig config) {
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

        SessionSettings settings = new SessionSettings();
        SessionID sessionId = new SessionID("FIX.4.2", config.getSenderCompId(), config.getTargetCompId());

        settings.setString(sessionId, SessionFactory.SETTING_CONNECTION_TYPE, SessionFactory.ACCEPTOR_CONNECTION_TYPE);
        settings.setString(sessionId, SessionSettings.BEGINSTRING, "FIX.4.2");
        settings.setString(sessionId, "ResetOnDisconnect", "Y");
        settings.setString(sessionId, "ResetOnLogout", "Y");
        settings.setString(sessionId, "DataDictionary", "FLEX_FX_FIX42.xml");
        settings.setString(sessionId, "StartTime", "00:00:00");
        settings.setString(sessionId, "EndTime", "00:00:00");
        settings.setString(sessionId, "NonStopSession", "Y");
        settings.setLong(sessionId, "LogonTimeout", 30);
        settings.setBool(sessionId, Session.SETTING_CHECK_LATENCY, false);

        settings.setString(sessionId, SessionSettings.SENDERCOMPID, config.getSenderCompId());
        settings.setString(sessionId, SessionSettings.TARGETCOMPID, config.getTargetCompId());
        settings.setString(sessionId, Acceptor.SETTING_SOCKET_ACCEPT_PROTOCOL, "TCP");
        settings.setString(sessionId, Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS, config.getHost());
        settings.setString(sessionId, Acceptor.SETTING_SOCKET_ACCEPT_PORT, String.valueOf(config.getPort()));
        settings.setLong(sessionId, "HeartBtInt", config.getHeartbeatInterval());
        
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
