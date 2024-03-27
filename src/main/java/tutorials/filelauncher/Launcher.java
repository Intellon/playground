package tutorials.filelauncher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author uex13061
 *
 */
public class Launcher {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec("cmd.exe /c resources\\tutorials.filelauncher.logs\\batch\\Starter.bat");
		//Process proc = runtime.exec("cmd.exe /c PowerShell.exe -File D:\\Test.ps1");
		//Process proc = runtime.exec("cmd.exe /c PowerShell.exe D:\\Test.ps1 >> D:\\StartupLog.txt 2>&1");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		System.out.println(dtf.format(now));  
		
		InputStream is = proc.getInputStream();		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		String line;

		while ((line = reader.readLine()) != null)
		{
			System.out.println(line);
			//System.out.println(reader.readLine());
		}
		
		reader.close();
		proc.getOutputStream().close();
    }
	
}
