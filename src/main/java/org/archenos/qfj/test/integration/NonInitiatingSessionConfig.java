package org.archenos.qfj.test.integration;

import quickfix.Acceptor;
import quickfix.SessionFactory;

public final class NonInitiatingSessionConfig extends AbstractSessionConfig {

    public NonInitiatingSessionConfig(ProtocolVersion version, String senderCompId, String targetCompId) {
        super(version, senderCompId, targetCompId);
        getRawSettings().setString(SessionFactory.SETTING_CONNECTION_TYPE, SessionFactory.ACCEPTOR_CONNECTION_TYPE);
    }

    @Override
    public void setHost(String host) {
        getRawSettings().setString(Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS, host);
    }

    @Override
    public void setPort(int port) {
        getRawSettings().setLong(Acceptor.SETTING_SOCKET_ACCEPT_PORT, port);
    }
}
