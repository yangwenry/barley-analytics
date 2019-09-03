package barley.analytics.spark.core.udf.defaults;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ua_parser.Parser;

import java.io.IOException;

public class UserAgentInitializer {
    private static final Log log = LogFactory.getLog(UserAgentInitializer.class);

    public static final UserAgentInitializer Instance = new UserAgentInitializer();
    private static Parser uaParser;

    public static UserAgentInitializer getInstance() {
        return Instance;
    }

    private UserAgentInitializer() {
        try {
            uaParser = new Parser();
        } catch (IOException e) {
            log.error("Unable to initialize the user agent parser", e);

        }
    }

    public Parser getUaParser() {
        return uaParser;
    }
}
