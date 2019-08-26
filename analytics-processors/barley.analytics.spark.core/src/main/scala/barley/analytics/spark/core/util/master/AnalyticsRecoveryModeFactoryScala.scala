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

package barley.analytics.spark.core.util.master

import org.apache.spark.SparkConf
import org.apache.spark.deploy.master._
import barley.analytics.spark.core.deploy.{AnalyticsPersistenceEngine, AnalyticsLeaderElectionAgent}
import org.apache.spark.serializer.Serializer

/**
 * Scala version of Recovery mode factory
 */
class AnalyticsRecoveryModeFactoryScala(conf: SparkConf, serializer: Serializer)
  extends StandaloneRecoveryModeFactory(conf, serializer) {

  AnalyticsRecoveryModeFactoryScala.instantiationAttempts += 1

  override def createPersistenceEngine(): PersistenceEngine = new
      AnalyticsPersistenceEngine(serializer)

  override def createLeaderElectionAgent(master: LeaderElectable): LeaderElectionAgent = new
      AnalyticsLeaderElectionAgent(master)
}

object AnalyticsRecoveryModeFactoryScala {
  @volatile var instantiationAttempts = 0
}