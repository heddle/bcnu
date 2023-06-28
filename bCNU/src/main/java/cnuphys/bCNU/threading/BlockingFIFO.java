package cnuphys.bCNU.threading;


public class BlockingFIFO<T> extends ABlockingQueue<T> {


	/**
	 * Dequeue an object. If queue is empty, wait.
	 * Any number of reader threads can call this. 
	 * When an object arrives, one of the threads will be given the object,
	 * @return a object for processing.
	 */
	protected T dequeueObject() {
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
