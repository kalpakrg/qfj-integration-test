package org.archenos.qfj.test.integration;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import quickfix.Application;
import quickfix.ApplicationAdapter;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;

public final class IntegrationTestApplication extends ApplicationAdapter {
    private final Set<Application> listeners = new LinkedHashSet<Application>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void addListener(Application app) {
        lock.writeLock().lock();
        try {
            listeners.add(app);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeListener(Application app) {
        lock.writeLock().lock();
        try {
            listeners.remove(app);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void onCreate(SessionID sessionId) {
        lock.readLock().lock();
        try {
            for (Application app : listeners) {
                app.onCreate(sessionId);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void onLogon(SessionID sessionId) {
        lock.readLock().lock();
        try {
            for (Application app : listeners) {
                app.onLogon(sessionId);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void onLogout(SessionID sessionId) {
        lock.readLock().lock();
        try {
            for (Application app : listeners) {
                app.onLogout(sessionId);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        lock.readLock().lock();
        try {
            for (Application app : listeners) {
                app.toAdmin(message, sessionId);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        lock.readLock().lock();
        try {
            for (Application app : listeners) {
                app.toApp(message, sessionId);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        lock.readLock().lock();
        try {
            for (Application app : listeners) {
                app.fromAdmin(message, sessionId);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
            UnsupportedMessageType {
        lock.readLock().lock();
        try {
            for (Application app : listeners) {
                app.fromApp(message, sessionId);
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}
