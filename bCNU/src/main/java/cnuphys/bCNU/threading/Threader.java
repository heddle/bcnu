package cnuphys.bCNU.threading;

import java.util.ArrayList;

public abstract class Threader<T> {

	//the number of reader threader
	protected int _numReaders;

	//the blocking queue
	protected ABlockingQueue<T> _queue;

	//all the readers
	protected ArrayList<AReader<T>> _readers;

	/**
	 * Create a Threader using the number of available processors
	 * @param queue the queue to process
	 */
	public Threader(ABlockingQueue<T> queue) {
		this(Runtime.getRuntime().availableProcessors(), queue);
	}

	/**
	 * Create a Threader
	 * @param numReaders the number of reader threads to create
	 * @param queue the queue to process
	 */
	public Threader(int numReaders, ABlockingQueue<T> queue) {
		_numReaders = numReaders;
		_queue = queue;

		//create the readers
		_readers = new ArrayList<>();
		for (int i = 0; i < _numReaders; i++) {
			AReader<T> reader = createReader(_queue);
			_readers.add(reader);
			reader.start();
		}
	}

	/**
	 * Queue an object which will be grabbed by a reader
	 * @param object the object to queue
	 */
	public void queue(T object) {
		_queue.queue(object);
	}


	/**
	 * Create a reader thread
	 * @param queue the queue to read from
	 * @return a reader
	 */
	public abstract AReader<T> createReader(ABlockingQueue<T> queue);

	/**
	 * Main program for testing
	 * @param arg command line args (ignored)
	 */
	public static void main(String arg[]) {
		BlockingFIFO<Integer> fifo = new BlockingFIFO<>();

		Threader<Integer> threader = new Threader<>(fifo) {

			@Override
			public AReader<Integer> createReader(ABlockingQueue<Integer> queue) {
				return null;
			}
		};


		System.out.println("Number of threads to use: " + threader._numReaders);
	}

}
