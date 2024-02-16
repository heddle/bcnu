package cnuphys.bCNU.graphics.container;

import cnuphys.bCNU.threading.AReader;
import cnuphys.bCNU.threading.BlockingFIFO;

public class Refresher {
	
	private static  BlockingFIFO<BaseContainer> containers= new BlockingFIFO<BaseContainer>();
	

	private static AReader<BaseContainer> reader;
	
	
	public static void queueRefresh(BaseContainer container) {
		if (reader == null) {
			launchReader();
		}
		containers.queueUnique(container);
	}
	
	private static void launchReader() {
		reader = new AReader<BaseContainer>(containers) {
			@Override
			public void process(BaseContainer container) {
				
				container.repaint();

				if (container.getToolBar() != null) {
					if (container.getToolBar().getUserComponent() != null) {
						container.getToolBar().getUserComponent().repaint();
					}
				}
			}
		};
		reader.start();

	}

}
