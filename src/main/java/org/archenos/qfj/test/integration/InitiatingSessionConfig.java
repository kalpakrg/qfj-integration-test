package org.archenos.qfj.test.integration;

import quickfix.Initiator;
import quickfix.SessionFactory;

public final class InitiatingSessionConfig extends AbstractSessionConfig {

    public InitiatingSessionConfig(ProtocolVersion version, String senderCompId, String targetCompId) {
        super(version, senderCompId, targetCompId);
        getRawSettings().setString(SessionFactory.SETTING_CONNECTION_TYPE, SessionFactory.INITIATOR_CONNECTION_TYPE);
    }

    @Override
    public void setHost(String host) {
        getRawSettings().setString(Initiator.SETTING_SOCKET_CONNECT_HOST, host);
    }

    @Override
    public void setPort(int port) {
        getRawSettings().setLong(Initiator.SETTING_SOCKET_CONNECT_PORT, port);
    }
}
