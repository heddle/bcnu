package cnuphys.bCNU.threading;

import java.util.Vector;

public abstract class ABlockingQueue<T> extends Vector<T>{

	/**
	 * Put an object in the FIFO queue. Any thread can do this.
	 * @param element the object to put in the queue
	 */
	public synchronized void queue(T object) {
		if (object != null) {
			add(object);

			// notify a SINGLE thread among all threads
			// waiting for an object to be queued
			notify();
		}
	}

	/**
	 * Put an object in the FIFO queue. Any thread can do this.
	 * If the object is already in the queue, it is not added.
	 * @param object the object to put in the queue
	 */
	public synchronized void queueUnique(T object) {
		if (object != null) {

			if (contains(object)) {
				return;
			}

			add(object);

			// notify a SINGLE thread among all threads
			// waiting for an object to be queued
			notify();
		}
	}

	/**
	 * Dequeue an object. If queue is empty, wait.
	 * Any number of reader threads can call this.
	 * When an object arrives, one of the threads will be given the object,
	 * @return a object for processing.
	 */
	public synchronized T dequeue() {
		return dequeueObject();
	}

	/**
	 * Dequeue an object. If queue is empty, wait.
	 * Any number of reader threads can call this.
	 * When an object arrives, one of the threads will be given the object,
	 * @return a object for processing.
	 */
	protected abstract T dequeueObject();

}
