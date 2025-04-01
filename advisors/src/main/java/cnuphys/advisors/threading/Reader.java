package cnuphys.advisors.threading;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Reader<T> extends Thread {

	//the fifo being dequeued
	private BlockingFIFO<T> _fifo;

	// lag to stop the thread
	private AtomicBoolean running = new AtomicBoolean(false);

	/**
	 * Create a reader (dequeuer) for a BlockingFOFO
	 * @param fifo the fifo
	 */
	public Reader(BlockingFIFO<T> fifo) {
		_fifo = fifo;
	}

	@Override
	public void run() {
		running.set(true);
		while (running.get()) {
			T val = _fifo.dequeue();
			if (val != null) {
				process(val);
			}
		}
	}

	/**
	 * Tell the reader to stop reading.
	 */
	public void stopReader() {
		running.set(false);
	}

	/**
	 * Process the element that was dequeued
	 * @param element the element to process
	 */
	public abstract void process(T element);

}
