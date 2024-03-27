package tutorials.filelauncher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class FileReader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePath = "resources\\tutorials.filelauncher.logs\\StartupLog.txt";
        System.out.println( readLineByLine(filePath) + "\nich habe die Zeilen gelesen :-)" );

		Path logPath = Paths.get("resources\\tutorials.filelauncher.logs\\StartupLog.txt");
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            // Creates FileChannel and open the file channel for read access.
            FileChannel channel = FileChannel.open(logPath, StandardOpenOption.READ);

            // Read a sequence of bytes from the channel into the buffer starting
            // at given file position, which is the channel size - 1000. Because
            // we are going to read the last 1000 characters from the file.
            channel.read(buffer, channel.size() - 100);
            System.out.println("Characters = " + new String(buffer.array()));
            
            if (new String(buffer.array()).contains("scheissdreck...")) {
            	System.out.println("\n\nole ole ole oleeeee");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
                
	}
	
	private static String readLineByLine(String filePath) 
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append( "\n"));   
        }
        catch (IOException e) { e.printStackTrace(); }
 
        return contentBuilder.toString();
    }

}
