package cnuphys.bCNU.threading;

import java.util.Vector;

public class BlockingFIFO<T> extends Vector<T> {

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
	 * Dequeue an object. If queue is empty, wait.
	 * Any number of reader threads can call this. 
	 * When an object arrives, one of the threads will be given the object,
	 * @return a object for processing.
	 */
	public synchronized T dequeue() {
		if (isEmpty()) {
			try {
				// wait until notified of arriving object
				wait();
			} catch (InterruptedException e) {
				System.out.println("Waiting thread interrupted.");
				return null;
			}

		}
		// return first element
		return remove(0);
	}
	

}
