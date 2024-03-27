package tutorials.loadgeneratoripconfigurator;

import java.io.Console;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class JavaConsole {

    public final String PARAM_ONLYDEFAULT = "ONLYDEFAULT";
    public final String PARAM_HELP1 = "HELP";
    public final String PARAM_HELP2 = "?";
    public final String PARAM_DHCP = "DHCP";
    public String keyAuswahl = "";
    public Console console = System.console();

    private boolean needHelp = false;
    private boolean resetToDefault = false;
    private boolean resetToDHCP = false;
    private String hostname;


    public static void main(String[] args) throws UnknownHostException {

        JavaConsole ipChanger = new JavaConsole();
        ipChanger.ReadArgs(args);
        ipChanger.Start();

    }

    private void ReadArgs(String[] args) throws UnknownHostException {
        for (int i = 0; i < args.length; i++)
        {
            if (isParameter(args[i], PARAM_ONLYDEFAULT))
            {
                resetToDefault = true;
            }
            if (isParameter(args[i], PARAM_HELP1) || isParameter(args[i], PARAM_HELP2))
            {
                needHelp = true;
            }
            if (isParameter(args[i], PARAM_DHCP))
            {
                resetToDHCP = true;
            }
        }
        this.hostname = InetAddress.getLocalHost().getHostName();
    }

    private boolean isParameter(String arg, String param)
    {
        arg = arg.toUpperCase().trim();
        param = param.toLowerCase();
        if (arg.startsWith("/"))
        {
            arg = arg.substring(1).trim();
        }
        return arg.equals(param);
    }

    private void Start()
    {
        if (needHelp)
        {
            ShowHelp();
            return;
        }

        System.out.println("Herzlich willkommen in der wunderbaren Welt der IP-Konfiguration für Lastgeneratoren");
        System.out.println("Dieses Tool wird sie mit ausserordentlicher Herzlichkeit durch diesen Prozess begleiten");
        System.out.println("Was möchten Sie jetzt tun?");
        System.out.println("1 - Den Lastgenerator auf DHCP zurücksetzen");
        System.out.println("2 - Den Lastgenerator statisch auf seine Default-IP setzen");
        System.out.println("3 - Dem Lastgenerator viele viele IP Adressen zuweisen");

        System.out.println("Bitte wählen Sie: ");
        keyAuswahl = console.readLine();

        if (keyAuswahl.equals("1"))
        {
            this.resetToDHCP = true;
            this.resetToDefault = false;
            System.out.println("Sie haben haben " + keyAuswahl +" gewählt");
        }
        else if (keyAuswahl.equals("2"))
        {
            this.resetToDHCP = false;
            this.resetToDefault = true;
            System.out.println("Sie haben haben " + keyAuswahl +" gewählt");
        }
        else if (keyAuswahl.equals("3"))
        {
            this.resetToDHCP = false;
            this.resetToDefault = false;
            System.out.println("Sie haben haben " + keyAuswahl +" gewählt");
        }
        else
        {
            System.out.println("Ungültige Auswahl. Programm wird beendet.");
            return;
        }
    }

    private void ShowHelp()
    {
        System.out.println("Dieses Tool dient dazu, die dynamischen IP-Adressen auf Lastgeneratoren zu konfigurieren");
        System.out.println("Die Konfiguration liegt in der Mada Datenbank mssql-pfc12-mada-p:1433");
        System.out.println("in der Datenbank 'ip_loadgenerators'. Nähere Informationen dazu hat der IT-AV Performance Center");
        System.out.println("");
        System.out.println("Parameter:");
        System.out.println(PARAM_ONLYDEFAULT + " ... Setzt die IP des Lastgenerators auf den Defaultwert (nur 1 IP)");
        System.out.println(PARAM_HELP1 + " ... Anzeigen der Hilfe");
        System.out.println(PARAM_HELP2 + " ... Anzeigen der Hilfe");
        System.out.println(PARAM_DHCP + " ... Netzwerkkarte auf DHCP zurücksetzen");
    }

}
