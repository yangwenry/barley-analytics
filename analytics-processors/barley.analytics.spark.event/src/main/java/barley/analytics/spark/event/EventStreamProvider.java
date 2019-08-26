/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package barley.analytics.spark.event;

import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.sources.RelationProvider;

import barley.analytics.datasource.commons.exception.AnalyticsException;
import barley.analytics.spark.core.util.AnalyticsConstants;
import barley.core.MultitenantConstants;
import scala.Function0;
import scala.Function1;
import scala.Option;
import scala.collection.immutable.Map;
import scala.runtime.AbstractFunction0;
import scala.runtime.AbstractFunction1;

/**
 * Implements <code>org.apache.spark.sql.sources.RelationProvider</code>
 * This is called whenever a CREATE_TABLE or INSERT_OVERWRITE Spark query is executed.
 * At the event of Spark's CREATE_TABLE query, this provider defines an event stream according  to the parameters
 * provided in the options sections of the query.
 *
 * At the event of Spark's INSERT_OVERWRITE query, this provider creates events from input table and publishes them
 * into the stream that has been created previously.
 */
public class EventStreamProvider implements RelationProvider {

    private static final String DEFAULT_VERSION = "1.0.0";

    private int tenantId;
    
    private String streamName;
    
    private String version;
    
    private String payload;

    private boolean globalTenantAccess;

    public EventStreamProvider() { 
        try {
            EventStreamDataStore.initStore();
        } catch (AnalyticsException e) {
            throw new RuntimeException("Error in creating event stream provider: " + e.getMessage(), e);
        }
    }

    @Override
    public StreamRelation createRelation(SQLContext sqlContext, Map<String, String> parameters) {
        this.setParameters(parameters);
        return new StreamRelation(this.tenantId, sqlContext, this.getStreamId(this.streamName, this.version), this.payload, this.globalTenantAccess);
    }

    private void setParameters(Map<String, String> parameters) {
        this.tenantId = Integer.parseInt(extractValuesFromMap(EventingConstants.TENANT_ID, parameters, 
                "" + MultitenantConstants.SUPER_TENANT_ID));
        this.streamName = extractValuesFromMap(EventingConstants.STREAM_NAME, parameters, "");
        this.version = extractValuesFromMap(EventingConstants.VERSION, parameters, DEFAULT_VERSION);
        this.payload = extractValuesFromMap(EventingConstants.PAYLOAD, parameters, "");
        this.globalTenantAccess = this.tenantId == MultitenantConstants.SUPER_TENANT_ID &&
                Boolean.parseBoolean(extractValuesFromMap(AnalyticsConstants.GLOBAL_TENANT_ACCESS, parameters, String.valueOf(false)));
    }

    private String getStreamId(String streamName, String version) {
        return streamName + ":" + version;
    }

    private String extractValuesFromMap(String key, Map<String, String> map,
                                        final String defaultVal) {
    	
    	/* (임시주석) - 컴파일 오류 발생 중이며 스칼라 문법을 이해하지 못해 주석. 
    	return map.getOrElse(key, new AbstractFunction0<String>() {
            public String apply() {
                return defaultVal;
            }
        });
        */
        
    	// 제네릭을 강제로 제거하여 코드로 작성함. 
        Map data = map;
        return (String) data.getOrElse(key, new AbstractFunction0<String>() {
            public String apply() {
                return defaultVal;
            }
        });
    }

}
