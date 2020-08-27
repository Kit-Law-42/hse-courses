import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size;
    private Item[] RQueue;

    // construct an empty randomized queue
    public RandomizedQueue() {
        RQueue = (Item[]) new Object[1];
        size = 0;
    }

    private class RQueueIterator implements Iterator<Item> {
        private Item[] copy = (Item[]) new Object[RQueue.length];
        private int copySize = size;

        public RQueueIterator() {
            for (int i = 0; i < RQueue.length; i++) {
                copy[i] = RQueue[i];
            }
        }

        @Override
        public boolean hasNext() {
            return copySize > 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int rd = StdRandom.uniform(copySize);
            Item item = copy[rd];
            if (rd != copySize - 1)
                copy[rd] = copy[copySize - 1];
            copy[copySize - 1] = null;
            copySize--;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (size == RQueue.length)
            resize(RQueue.length * 2);
        RQueue[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        int rd = StdRandom.uniform(size);
        Item item = RQueue[rd];
        if (rd != size - 1) {
            RQueue[rd] = RQueue[size - 1];
        }
        RQueue[size - 1] = null;
        size--;
        if (size > 0 && size == RQueue.length / 4)
            resize(RQueue.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        int rd = StdRandom.uniform(size);
        Item item = RQueue[rd];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RQueueIterator();
    }

    private void resize(int capacity) {
        assert capacity >= size;
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = RQueue[i];
        }
        RQueue = copy;
        copy = null;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> deque = new RandomizedQueue<Integer>();
        deque.enqueue(0);
        deque.enqueue(1);
        StdOut.println(deque.isEmpty());
        deque.enqueue(3);
        StdOut.println(deque.dequeue());
        deque.enqueue(5);
        deque.enqueue(6);
        deque.dequeue();
        StdOut.println(deque.sample());
    }

}
