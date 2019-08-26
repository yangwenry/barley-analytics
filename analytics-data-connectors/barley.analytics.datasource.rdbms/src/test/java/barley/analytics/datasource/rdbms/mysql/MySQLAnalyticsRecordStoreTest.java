/*
 *  Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import barley.analytics.datasource.commons.exception.AnalyticsException;
import barley.analytics.datasource.core.rs.AnalyticsRecordStore;
import barley.analytics.datasource.core.util.GenericUtils;
import barley.analytics.datasource.rdbms.AnalyticsRecordStoreTest;
import barley.analytics.datasource.rdbms.RDBMSAnalyticsRecordStore;

/**
 * MySQL implementation of analytics record store tests.
 */
public class MySQLAnalyticsRecordStoreTest extends AnalyticsRecordStoreTest {
        
    @BeforeClass
    public void setup() throws NamingException, AnalyticsException {
        GenericUtils.clearGlobalCustomDataSourceRepo();
        System.setProperty(GenericUtils.WSO2_ANALYTICS_CONF_DIRECTORY_SYS_PROP, "src/test/resources/conf3");
        AnalyticsRecordStore ars = new RDBMSAnalyticsRecordStore();
        Map<String, String> props = new HashMap<String, String>();
        props.put("datasource", "WSO2_ANALYTICS_RS_DB");
        ars.init(props);
        this.init("MySQLDBAnalyticsDataSource", ars);
    }
    
    @AfterClass
    public void destroy() throws AnalyticsException {
        this.cleanup();
    }
    
}
