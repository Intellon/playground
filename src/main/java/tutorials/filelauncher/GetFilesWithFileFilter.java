package tutorials.filelauncher;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class GetFilesWithFileFilter {

	public static void main(String[] args) throws IOException {

		File directory = new File("resources\\tutorials.filelauncher.logs");
				
		//create a FileFilter and override its accept-method
		FileFilter fileFilter = new FileFilter() {
		//Override accept method
		public boolean accept(File file) {
			//if the file extension is .txt return true, else false and check the filename as well
			if (file.getName().endsWith(".txt") && !file.getName().startsWith("Startup")) {
				return true;
			}
			return false;
			}
		};
		   
		displayFiles(directory, fileFilter);
	}

	public static void displayFiles(File directory, FileFilter fileFilter) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		Date date = new Date(System.currentTimeMillis());
		int i = 0;
		
		File[] files = directory.listFiles(fileFilter);
		Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
		
		for (File file : files) {
			Date lastMod = new Date(file.lastModified());
			System.out.println("File: " + file.getName() + ", Date: " + lastMod +", Counter: "+i+"");
			
			if (i >= 5 ) {
				System.out.println(file.getName() +" deleted.");
				file.delete();
			}
			i++;
		}
		
		System.out.println("Monat 2-digits: "+ formatter.format(date) +"\nAnz.Files: "+ i);
	}
}
