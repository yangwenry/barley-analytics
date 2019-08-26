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

package barley.analytics.spark.core.deploy;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import barley.analytics.spark.core.internal.ServiceHolder;

/**
 * Execution call for electing a leader
 */
public class ElectLeaderExecutionCall implements Callable<Integer>, Serializable{

    private static final long serialVersionUID = -155083315280606063L;

    private static Log log = LogFactory.getLog(ElectLeaderExecutionCall.class);

    @Override
    public Integer call() throws Exception {
        ServiceHolder.getAnalyticskExecutor().electAsLeader();
        log.info("Current node elected as Spark leader.");
        return 0;
    }
}
