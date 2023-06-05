package cnuphys.advisors.threading;

import java.util.Vector;


public class BlockingFIFO<T> extends Vector<T> {

	
	/**
	 * Queue an object. Notify threads that are waiting.
	 * Any thread including the GUI thread can queue an object. It does not block.
	 * @param message the data to queue
	 */
	public synchronized void queue(T element) {
		add(element);
	}
	
	@Override
	public synchronized boolean add(T element) {
		if (element == null) {
			return true;
		}

		boolean b = super.add(element);

		// notify any threads waiting for an object
		notifyAll();
		return b;
	}

	/**
	 * Dequeue an object. If queue is empty, wait.
	 * 
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
	
	
	//test the notify wait pattern
	public static void main(String arg[]) {
		final BlockingFIFO<Integer> fifo = new BlockingFIFO<>();
		
		Reader<Integer> reader = new Reader<Integer>(fifo) {

			@Override
			public void process(Integer element) {
				System.out.println(element);
			}
		};
		

			
		reader.start();
		
		for (int i = 1; i <= 100; i++) {
			fifo.queue(i);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

				
		reader.stopReader();
		System.out.println("Main thread done");
	}

}
