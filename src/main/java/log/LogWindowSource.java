package log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений
 * ограниченного размера)
 */
public class LogWindowSource
{
    private int m_iQueueLength;

    private final RingBuffer m_messages;
    private final List<WeakReference<LogChangeListener>> m_listeners = new ArrayList<>();

    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int iQueueLength)
    {
        this.m_iQueueLength = iQueueLength;
        this.m_messages = new RingBuffer(iQueueLength);

    }

    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(new WeakReference<>(listener));
            m_activeListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.removeIf(ref -> {
                LogChangeListener l = ref.get();
                return l == null || l == listener;
            });
            m_activeListeners = null;
        }
    }

    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.append(entry);
        List<LogChangeListener> listenersToNotify = new ArrayList<>();

        synchronized (m_listeners) {
            m_listeners.removeIf(ref -> ref.get() == null); // clean up GC'd references

            for (WeakReference<LogChangeListener> ref : m_listeners) {
                LogChangeListener listener = ref.get();
                if (listener != null) {
                    listenersToNotify.add(listener);
                }
            }
        }
        for (LogChangeListener listener : listenersToNotify)
        {
            listener.onLogChanged();
        }
    }

    public int size()
    {
        return m_messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        return m_messages.range(startFrom, count);
    }

    public Iterable<LogEntry> all()
    {
        return m_messages;
    }
}