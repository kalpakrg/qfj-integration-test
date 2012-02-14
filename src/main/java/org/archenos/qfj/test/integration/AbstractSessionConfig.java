package org.archenos.qfj.test.integration;

import quickfix.ConfigError;
import quickfix.FieldConvertError;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;

public abstract class AbstractSessionConfig {
    private final SessionSettings settings;
    private final SessionID sessionId;

    public AbstractSessionConfig(ProtocolVersion version, String senderCompId, String targetCompId) {
        this.sessionId = new SessionID(version.getBeginString(), senderCompId, targetCompId);
        this.settings = new SessionSettings();
        setBeginString(version.getBeginString());
        setSenderCompId(senderCompId);
        setTargetCompId(targetCompId);
    }

    public abstract void setHost(String host);

    public abstract void setPort(int port);

    private void setBeginString(String beginString) {
        settings.setString(sessionId, SessionSettings.BEGINSTRING, beginString);
    }

    private void setSenderCompId(String senderCompId) {
        settings.setString(sessionId, SessionSettings.SENDERCOMPID, senderCompId);
    }

    private void setTargetCompId(String targetCompId) {
        settings.setString(sessionId, SessionSettings.TARGETCOMPID, targetCompId);
    }
    
    public String getSenderCompId() {
        return getStringGraceful(SessionSettings.SENDERCOMPID);
    }

    public String getTargetCompId() {
        return getStringGraceful(SessionSettings.TARGETCOMPID);
    }

    public void setDataDictionary(String dataDictionary) {
        settings.setString(sessionId, Session.SETTING_DATA_DICTIONARY, dataDictionary);
    }

    public String getDataDictionary() {
        return getStringGraceful(Session.SETTING_DATA_DICTIONARY);
    }

    public void setLogonTimeout(long logonTimeout) {
        settings.setLong(Session.SETTING_LOGON_TIMEOUT, logonTimeout);
    }

    public long getLogonTimeout() {
        return getLongGraceful(Session.SETTING_LOGON_TIMEOUT);
    }

    public void setHeartbeatInterval(long heartbeatInterval) {
        settings.setLong(sessionId, Session.SETTING_HEARTBTINT, heartbeatInterval);
    }

    public long getHeartbeatInterval() {
        return getLongGraceful(Session.SETTING_HEARTBTINT);
    }

    public void setResetOnDisconnect(boolean resetOnDisconnect) {
        settings.setBool(Session.SETTING_RESET_ON_DISCONNECT, resetOnDisconnect);
    }

    public boolean isResetOnDisconnect() {
        return getBooleanGraceful(Session.SETTING_RESET_ON_DISCONNECT);
    }

    public void setResetOnLogout(boolean resetOnLogout) {
        settings.setBool(Session.SETTING_RESET_ON_LOGOUT, resetOnLogout);
    }

    public boolean isResetOnLogout() {
        return getBooleanGraceful(Session.SETTING_RESET_ON_LOGOUT);
    }

    public void setPersistMessages(boolean persistMessages) {
        settings.setBool(Session.SETTING_PERSIST_MESSAGES, persistMessages);
    }

    public boolean isPersistMessages(boolean persistMessages) {
        return getBooleanGraceful(Session.SETTING_PERSIST_MESSAGES);
    }

    SessionSettings getRawSettings() {
        return settings;
    }

    public void setBool(String key, boolean value) {
        settings.setBool(sessionId, key, value);
    }

    public void setString(String key, String value) {
        settings.setString(sessionId, key, value);
    }

    public void setLong(String key, long value) {
        settings.setLong(sessionId, key, value);
    }

    public void setDouble(String key, double value) {
        settings.setDouble(sessionId, key, value);
    }

    private String getStringGraceful(String key) {
        try {
            return settings.getString(sessionId, key);
        } catch (ConfigError e) {
            return null;
        } catch (FieldConvertError e) {
            // This is unlikely as long as we internally comply with the format.
            throw new IllegalStateException(e);
        }
    }


    private Long getLongGraceful(String key) {
        try {
            return settings.getLong(sessionId, key);
        } catch (ConfigError e) {
            return null;
        } catch (FieldConvertError e) {
            // This is unlikely as long as we internally comply with the format.
            throw new IllegalStateException(e);
        }
    }

    private Double getDoubleGraceful(String key) {
        try {
            return settings.getDouble(sessionId, key);
        } catch (ConfigError e) {
            return null;
        } catch (FieldConvertError e) {
            // This is unlikely as long as we internally comply with the format.
            throw new IllegalStateException(e);
        }
    }

    private Boolean getBooleanGraceful(String key) {
        try {
            return settings.getBool(sessionId, key);
        } catch (ConfigError e) {
            return null;
        } catch (FieldConvertError e) {
            // This is unlikely as long as we internally comply with the format.
            throw new IllegalStateException(e);
        }
    }
}
