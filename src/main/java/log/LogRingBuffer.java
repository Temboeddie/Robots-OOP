package log;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Потокобезопасный кольцевой буфер, специально предназначенный для хранения записей журнала.
 * Этот буфер поддерживает фиксированную максимальную емкость и автоматически
 * удаляет самые старые записи при достижении предела.
 */

public class LogRingBuffer {
    private final int capacity;
    private final LinkedList<LogEntry> entries = new LinkedList<>();

    public LogRingBuffer(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void add(LogEntry entry) {
        if (entries.size() >= capacity) {
            entries.removeFirst(); // Remove the oldest entry
        }
        entries.addLast(entry);
    }

    public synchronized int size() {
        return entries.size();
    }

    public synchronized Iterable<LogEntry> range(int startFrom, int count) {
        int fromIndex = Math.max(0, startFrom);
        int toIndex = Math.min(entries.size(), fromIndex + count);
        return new ArrayList<>(entries.subList(fromIndex, toIndex));
    }

    public synchronized Iterable<LogEntry> all() {
        return new ArrayList<>(entries);
    }
}
