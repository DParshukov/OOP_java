package log;

import java.util.ArrayList;

/**
 * Источник записей протокола. Сами записи хранятся в кольцевом буфере
 * ограниченного размера ({@link BoundedRingBuffer}): добавление и вытеснение
 * старых записей работают за O(1), а чтение и итерирование потокобезопасны и
 * устойчивы к конкурентным добавлениям.
 */
public class LogWindowSource
{
    private final BoundedRingBuffer<LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int iQueueLength)
    {
        m_messages = new BoundedRingBuffer<>(iQueueLength);
        m_listeners = new ArrayList<LogChangeListener>();
    }

    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }

    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.append(entry);
        LogChangeListener [] activeListeners = m_activeListeners;
        if (activeListeners == null)
        {
            synchronized (m_listeners)
            {
                if (m_activeListeners == null)
                {
                    activeListeners = m_listeners.toArray(new LogChangeListener [0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners)
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
