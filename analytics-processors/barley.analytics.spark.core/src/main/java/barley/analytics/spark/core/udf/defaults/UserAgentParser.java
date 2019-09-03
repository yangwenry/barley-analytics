package barley.analytics.spark.core.udf.defaults;

import ua_parser.OS;
import ua_parser.Parser;
import ua_parser.UserAgent;

public class UserAgentParser {

	
	/**
     * This method would extract the Operating system form the given User-Agent String.
     *
     * @param userAgent The User-Agent string that is been sent.
     *                  Ex:- Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/48.0.2564.116 Chrome/48.0.2564.116 Safari/537.36
     * @return The name of the Operating system extracted from the User-Agent.
     */
    public String getOSFromUserAgent(String userAgent) {
        Parser parser = UserAgentInitializer.getInstance().getUaParser();
        if (parser != null) {
            OS operatingSystem = parser.parseOS(userAgent);
            if (operatingSystem != null) {
                return operatingSystem.family;
            }
        }
        return null;
    }

}
