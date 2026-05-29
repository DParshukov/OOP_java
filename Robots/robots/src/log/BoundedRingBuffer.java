package log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Потокобезопасный кольцевой буфер ограниченного размера.
 *
 * При переполнении новая запись вытесняет самую старую за O(1) (старое начало
 * перезаписывается, голова сдвигается), поэтому добавление не зависит от размера
 * буфера. Чтение сегмента по индексам и снимок для итерирования копируют только
 * запрошенные элементы, а не весь лог.
 *
 * Конкурентный доступ защищён {@link ReadWriteLock}: добавления сериализуются
 * между собой, но не блокируют друг друга при чтении. Итератор и
 * {@link #range(int, int)} возвращают неизменяемый снимок, поэтому добавление
 * данных во время обхода не разрушает итератор.
 *
 * Логический индекс 0 всегда соответствует самой старой из хранящихся записей;
 * после вытеснения индексы сдвигаются.
 */
public class BoundedRingBuffer<T> implements Iterable<T>
{
    private final Object[] m_buffer;
    private final ReadWriteLock m_lock = new ReentrantReadWriteLock();

    private int m_head;
    private int m_size;

    public BoundedRingBuffer(int capacity)
    {
        if (capacity <= 0)
        {
            throw new IllegalArgumentException("capacity must be positive: " + capacity);
        }
        m_buffer = new Object[capacity];
    }

    public int capacity()
    {
        return m_buffer.length;
    }

    public int size()
    {
        m_lock.readLock().lock();
        try
        {
            return m_size;
        }
        finally
        {
            m_lock.readLock().unlock();
        }
    }

    public void append(T element)
    {
        m_lock.writeLock().lock();
        try
        {
            if (m_size < m_buffer.length)
            {
                m_buffer[physicalIndex(m_size)] = element;
                m_size++;
            }
            else
            {
                m_buffer[m_head] = element;
                m_head = next(m_head);
            }
        }
        finally
        {
            m_lock.writeLock().unlock();
        }
    }

    /**
     * Снимок смежного сегмента записей, начиная с логического индекса
     * {@code startFrom} и не более {@code count} штук. Диапазон усекается по
     * границам буфера; некорректное начало даёт пустой список.
     */
    public List<T> range(int startFrom, int count)
    {
        m_lock.readLock().lock();
        try
        {
            if (startFrom < 0 || startFrom >= m_size || count <= 0)
            {
                return List.of();
            }
            int available = Math.min(count, m_size - startFrom);
            return copy(startFrom, available);
        }
        finally
        {
            m_lock.readLock().unlock();
        }
    }

    /**
     * Неизменяемый снимок всех хранящихся записей. Итератор по нему безопасен при
     * конкурентных добавлениях.
     */
    @Override
    public Iterator<T> iterator()
    {
        m_lock.readLock().lock();
        try
        {
            return copy(0, m_size).iterator();
        }
        finally
        {
            m_lock.readLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    private List<T> copy(int startFrom, int count)
    {
        List<T> snapshot = new ArrayList<>(count);
        for (int i = 0; i < count; i++)
        {
            snapshot.add((T) m_buffer[physicalIndex(startFrom + i)]);
        }
        return snapshot;
    }

    private int physicalIndex(int logicalIndex)
    {
        return (m_head + logicalIndex) % m_buffer.length;
    }

    private int next(int index)
    {
        return (index + 1) % m_buffer.length;
    }
}
