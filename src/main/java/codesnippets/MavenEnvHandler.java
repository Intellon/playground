package codesnippets;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MavenEnvHandler {

    public static void main(String[] args) {

        System.out.println("Test Maven Runner with Plain Java Methods\nHello Eliseo check that out:\n*****************\nMAVEN ROCKS\n**************");
        if(args.length > 0){
            System.out.println(args[0] +" von Eliseo");
        }

        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("D:\\temp\\LogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages
            logger.info("My log");
            logger.info(System.getProperty("KeyVonEliseo"));

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("End Log");
    }
}
