package UI;

import org.slf4j.LoggerFactory;

public class Logger {
    static public boolean enabled = true;
    static private org.slf4j.Logger logger = LoggerFactory.getLogger("game");
    static public void log(Object string) {
        if(enabled) logger.debug(string.toString());
    }
}
