package cnuphys.bCNU.threading;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base implementation for a Reader thread from a blocking queue
 * @author heddle
 *
 * @param <T>
 */
public abstract class AReader<T> extends Thread {
	
	//the queue being dequeued
	private ABlockingQueue<T> _queue;
	
	// lag to stop the thread
	private AtomicBoolean running = new AtomicBoolean(false);
	
	/**
	 * Create a reader (dequeuer) for a BlockingQuueue
	 * @param queue the queue
	 */
	public AReader(ABlockingQueue<T> queue) {
		_queue = queue;
	}
	
	@Override
	public void run() {
		running.set(true);
		while (running.get()) {
			T object = _queue.dequeue();
			if (object != null) {
				process(object);
			}
		}
	}
	
	/**
	 * Tell the reader to stop reading.
	 */
	public void stopReader() {
		running.set(false);
	};
	
	/**
	 * Process the element that was dequeued
	 * @param element the element to process
	 */
	public abstract void process(T element);

}
