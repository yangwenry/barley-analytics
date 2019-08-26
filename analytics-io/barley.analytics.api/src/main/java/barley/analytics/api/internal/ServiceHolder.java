/*
*  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package barley.analytics.api.internal;

import barley.analytics.dataservice.core.AnalyticsDataService;
import barley.analytics.dataservice.core.SecureAnalyticsDataService;

/**
 * This class holds the OSGI services which was required for the analytics api operation.
 */
public class ServiceHolder {
    private static AnalyticsDataService analyticsDataService;
    private static SecureAnalyticsDataService secureAnalyticsDataService;

    private ServiceHolder() {
        //Avoid Instantiation
    }

    public static AnalyticsDataService getAnalyticsDataService() {
        return analyticsDataService;
    }

    public static void setAnalyticsDataService(AnalyticsDataService analyticsDataService) {
        ServiceHolder.analyticsDataService = analyticsDataService;
    }

    public static SecureAnalyticsDataService getSecureAnalyticsDataService() {
        return secureAnalyticsDataService;
    }

    public static void setSecureAnalyticsDataService(SecureAnalyticsDataService secureAnalyticsDataService) {
        ServiceHolder.secureAnalyticsDataService = secureAnalyticsDataService;
    }
}
