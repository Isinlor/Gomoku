package UI;

public class Logger {
    static public boolean enabled = false; // enable if you want to see logs
    static public void log(Object string) {
        if(enabled) {
            System.out.println(string);
        }
    }
}
