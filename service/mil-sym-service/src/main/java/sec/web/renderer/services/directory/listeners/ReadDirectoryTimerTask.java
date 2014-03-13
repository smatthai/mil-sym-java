package sec.web.renderer.services.directory.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.TimerTask;

import sec.web.renderer.utils.ImagingUtils;

public class ReadDirectoryTimerTask extends TimerTask {
	private String directoryPath;
	private ArrayList<String> fileNames = new ArrayList<String>();

	public ReadDirectoryTimerTask(String dirPath) {
		directoryPath = dirPath;
	}

	@Override
	public void run() {
		try {
			// retain old list of plugins
			ArrayList<String> oldList = new ArrayList<String>();
			oldList.addAll(fileNames);

			// clear current list and rebuild
			clear();
			File dirToRead = new File(this.directoryPath);
			File[] files = dirToRead.listFiles();

			if (files != null) {
				for (File f : files) {
					fileNames.add(f.getName());
					// System.out.printf("adding:\t%s\n", f.getAbsolutePath());
				}
			}

			// compare original and updated list to see if there are differences. If so, reload the plugins.
			int oldSize = oldList.size();

			oldList.retainAll(fileNames);
			if (oldSize != fileNames.size()) {
				// System.out.println("reloading plugins!");
				ImagingUtils.reloadPlugins();
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}


	private void clear() {
		if (!this.fileNames.isEmpty()) {
			this.fileNames.clear();
		}
	}

	public ArrayList<String> getFileNames() {
		return this.fileNames;
	}

}
