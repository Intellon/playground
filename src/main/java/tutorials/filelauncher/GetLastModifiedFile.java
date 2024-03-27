package tutorials.filelauncher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

public class GetLastModifiedFile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		System.out.println(formatter.format(date));
		
		// specify your directory
		Path dir = Paths.get("resources\\tutorials.filelauncher.logs");
		
		// here we get the stream with full directory listing
		Optional<Path> lastFilePath = Files.list(dir)    
			.filter(f -> !Files.isDirectory(f)) // exclude subdirectories from listing
		    .max(Comparator.comparingLong(f -> f.toFile().lastModified())); // finally get the last file using simple comparator by lastModified field  
		
		// your folder may be empty
		if ( lastFilePath.isPresent() ) 
		{
		    //lastFilePath contains all you need
		    System.out.println("geiler scheiss..\n" + lastFilePath.get().getFileName());
		} 
	}
}
