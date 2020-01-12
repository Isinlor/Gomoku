package UI;

import org.slf4j.LoggerFactory;

public class Logger {
    static private org.slf4j.Logger logger = LoggerFactory.getLogger("game");
    static public void log(Object string) {
        logger.debug(string.toString());
    }
}
