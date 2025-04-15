package log;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
    private final int maxSize;
    private final LogRingBuffer buffer;
    private final List<WeakReference<LogChangeListener>> listeners = new CopyOnWriteArrayList<>();

    public LogWindowSource(int maxSize) {
        this.maxSize = maxSize;
        this.buffer = new LogRingBuffer(maxSize);
    }

    public void registerListener(LogChangeListener listener) {
        listeners.add(new WeakReference<>(listener));
    }

    public void unregisterListener(LogChangeListener listener) {
        listeners.removeIf(ref -> {
            LogChangeListener l = ref.get();
            return l == null || l == listener;
        });
    }

    public void append(LogLevel level, String message) {
        buffer.add(new LogEntry(level, message));
        notifyListeners();
    }

    public int size() {
        return buffer.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        return buffer.range(startFrom, count);
    }

    public Iterable<LogEntry> all() {
        return buffer.all();
    }

    private void notifyListeners() {
        for (WeakReference<LogChangeListener> ref : listeners) {
            LogChangeListener l = ref.get();
            if (l != null) {
                l.onLogChanged();
            }
        }
    }
    public void addMessage(String message) {
        append(LogLevel.Info, message);
    }
}
