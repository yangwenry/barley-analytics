/*
 *  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package barley.analytics.datasource.rdbms.mysql;

import java.io.IOException;

import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import barley.analytics.dataservice.core.AnalyticsServiceHolder;
import barley.analytics.datasource.commons.exception.AnalyticsException;
import barley.analytics.datasource.core.util.GenericUtils;
import barley.analytics.datasource.rdbms.AnalyticsDataServiceTest;

/**
 * MySQL test implementation of {@link AnalyticsDataServiceTest}.
 */
public class MySQLAnalyticsDataServiceTest extends AnalyticsDataServiceTest {
    
    @BeforeClass
    public void setup() throws NamingException, AnalyticsException, IOException {
        GenericUtils.clearGlobalCustomDataSourceRepo();
        System.setProperty(GenericUtils.WSO2_ANALYTICS_CONF_DIRECTORY_SYS_PROP, "src/test/resources/conf3");
        AnalyticsServiceHolder.setHazelcastInstance(null);
        AnalyticsServiceHolder.setAnalyticsClusterManager(null);
        System.setProperty(AnalyticsServiceHolder.FORCE_INDEXING_ENV_PROP, Boolean.TRUE.toString());
        this.init(AnalyticsServiceHolder.getAnalyticsDataService());
    }
    
    @AfterClass
    public void done() throws NamingException, AnalyticsException, IOException {
        this.service.destroy();
        System.clearProperty(AnalyticsServiceHolder.FORCE_INDEXING_ENV_PROP);
        AnalyticsServiceHolder.setAnalyticsDataService(null);
    }
    
}
