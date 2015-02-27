package sec.web.renderer.services.directory.listeners;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author User
 * @deprecated 
 */
public class DirectoryReaderTaskScheduler {
	private final static Logger LOGGER = Logger.getLogger(DirectoryReaderTaskScheduler.class.getName());	
	
	private ReadDirectoryTimerTask timeTask;	
	private final int NO_DELAY = 0;
	public ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	
	public void startTimer(String path, int interval) {		
		timeTask = new ReadDirectoryTimerTask(path);
		service.scheduleAtFixedRate(timeTask, NO_DELAY, interval, TimeUnit.MILLISECONDS);
		System.out.println("timer has been scheduled to watch " + path + " for every " + interval + " milliseconds");
	}
	
	public void stopTimer() {
		LOGGER.log(Level.INFO, "Timer shutdown initiated");
		service.shutdown();
	}
	
	public synchronized ArrayList<String> getFileNames() {
		ArrayList<String> temp = new ArrayList<String>();
		if (this.timeTask != null) {
			temp = timeTask.getFileNames();
		}
		return temp;
	}
}
