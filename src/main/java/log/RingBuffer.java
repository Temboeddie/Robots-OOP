package log;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Потокобезопасный кольцевой буфер для log entries..
 *
 **/
public class RingBuffer implements Iterable<LogEntry> {
    private final LogEntry[] buffer;
    private final int capacity;
    private int start = 0;
    private int size = 0;

    public RingBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new LogEntry[capacity];
    }

    public synchronized void append(LogEntry entry) {
        int index = (start + size) % capacity;
        buffer[index] = entry;

        if (size < capacity) {
            size++;
        } else {
            start = (start + 1) % capacity;
        }
    }

    public synchronized List<LogEntry> range(int startIndex, int count) {
        List<LogEntry> result = new ArrayList<>();
        if (startIndex < 0 || startIndex >= size) return result;

        int actualCount = Math.min(count, size - startIndex);
        for (int i = 0; i < actualCount; i++) {
            int index = (start + startIndex + i) % capacity;
            result.add(buffer[index]);
        }
        return result;
    }

    public synchronized int size() {
        return size;
    }

    @Override
    public synchronized Iterator<LogEntry> iterator() {
        List<LogEntry> snapshot = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            snapshot.add(buffer[(start + i) % capacity]);
        }
        return snapshot.iterator();
    }
}
