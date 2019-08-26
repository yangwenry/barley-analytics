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
package barley.analytics.eventsink.internal;

import java.io.File;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.axis2.deployment.DeploymentException;
import org.apache.axis2.deployment.repository.util.DeploymentFileData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;

import barley.analytics.api.AnalyticsDataAPI;
import barley.analytics.eventsink.AnalyticsEventSinkServiceImpl;
import barley.analytics.eventsink.AnalyticsEventStoreDeployer;
import barley.analytics.eventsink.internal.jmx.EventReceiverCounter;
import barley.analytics.eventsink.internal.jmx.QueueEventBufferSizeCalculator;
import barley.analytics.eventsink.internal.util.AnalyticsEventSinkConstants;
import barley.analytics.eventsink.internal.util.ServiceHolder;
import barley.analytics.eventsink.subscriber.AnalyticsEventStreamListener;
import barley.core.utils.BarleyUtils;
import barley.databridge.core.definitionstore.AbstractStreamDefinitionStore;
import barley.event.processor.manager.core.EventManagementService;
import barley.event.stream.core.EventStreamService;

/**
 * This is the declarative service component which registers the required OSGi
 * services for this component's operation.
 * 
 * @scr.component name="analytics.eventsink.comp" immediate="true"
 * @scr.reference name="registry.streamdefn.comp"
 * interface="org.wso2.carbon.databridge.core.definitionstore.AbstractStreamDefinitionStore"
 * cardinality="1..1" policy="dynamic" bind="setStreamDefinitionStoreService" unbind="unsetStreamDefinitionStoreService"
 * @scr.reference name="event.stream.service" interface="org.wso2.carbon.event.stream.core.EventStreamService"
 * cardinality="1..1" policy="dynamic" bind="setEventStreamService" unbind="unsetEventStreamService"
 * @scr.reference name="analytics.component" interface="org.wso2.carbon.analytics.api.AnalyticsDataAPI"
 * cardinality="1..1" policy="dynamic" bind="setAnalyticsDataAPI" unbind="unsetAnalyticsDataAPI"
 * @scr.reference name="eventManagement.service"
 * interface="org.wso2.carbon.event.processor.manager.core.EventManagementService" cardinality="1..1"
 * policy="dynamic" bind="setEventManagementService" unbind="unsetEventManagementService"
 */
public class AnalyticsEventSinkComponent {
    private static Log log = LogFactory.getLog(AnalyticsEventSinkComponent.class);

    public void activate() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Started the Analytics Event Sink component");
            }
            ServiceHolder.setAnalyticsEventSinkService(new AnalyticsEventSinkServiceImpl());
            ServiceHolder.setEventPublisherManagementService(new CarbonEventSinkManagementService());
            /* (임시주석) 
            componentContext.getBundleContext().registerService(EventStreamListener.class.getName(),
                    ServiceHolder.getAnalyticsEventStreamListener(), null);
            componentContext.getBundleContext().registerService(AnalyticsEventSinkService.class.getName(),
                    ServiceHolder.getAnalyticsEventSinkService(), null);
            componentContext.getBundleContext().registerService(ServerStartupObserver.class.getName(),
                    AnalyticsEventSinkServerStartupObserver.getInstance(), null);
            componentContext.getBundleContext().registerService(
                    AppDeploymentHandler.class.getName(), new AnalyticsEventStoreCAppDeployer(), null);
                    */
            ServiceHolder.getEventManagementService().subscribe(ServiceHolder.getEventPublisherManagementService());
            this.loadAnalyticsEventSinkConfiguration();
            ServiceHolder.setAnalyticsDSConnector(new AnalyticsDSConnector());
            
            // (추가) 2019.08.05 - 수동으로 xml 디플로이 수행 
            deployDefaultEventSinkConfiguration();
            
        } catch (Exception e) {
            log.error("Error while activating the AnalyticsEventSinkComponent.", e);
        }
        try {
            MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
            String eventCounterObject = "org.wso2.carbon:00=analytics,01=EVENT_PERSISTENCE_COUNTER";
            ObjectName eventCounterMbean = new ObjectName(eventCounterObject);
            if (!platformMBeanServer.isRegistered(eventCounterMbean)) {
                EventReceiverCounter counter = new EventReceiverCounter();
                platformMBeanServer.registerMBean(counter, eventCounterMbean);
            }
            String queueBufferSize = "org.wso2.carbon:00=analytics,01=RECEIVER_REMAINING_PERSISTENCE_QUEUE_BUFFER_SIZE_IN_BYTES";
            ObjectName queueBufferMbean = new ObjectName(queueBufferSize);
            if (!platformMBeanServer.isRegistered(queueBufferMbean)) {
                QueueEventBufferSizeCalculator counter = new QueueEventBufferSizeCalculator();
                platformMBeanServer.registerMBean(counter, queueBufferMbean);
            }
        } catch (Exception e) {
            log.error("Unable to create EventCounter stat MBean: " + e.getMessage(), e);
        }
    }

    private void loadAnalyticsEventSinkConfiguration() {
        File analyticsConfFile = new File(BarleyUtils.getCarbonConfigDirPath() + File.separator +
                AnalyticsEventSinkConstants.ANALYTICS_CONF_DIR + File.separator +
                AnalyticsEventSinkConstants.EVENT_SINK_CONFIGURATION_FILE_NAME);
        if (analyticsConfFile.exists()) {
            try {
                JAXBContext context = JAXBContext.newInstance(AnalyticsEventSinkConfiguration.class);
                Unmarshaller un = context.createUnmarshaller();
                ServiceHolder.setAnalyticsEventSinkConfiguration((AnalyticsEventSinkConfiguration)
                        un.unmarshal(analyticsConfFile));
            } catch (JAXBException e) {
                log.error("Error while unmarshalling the file : " + analyticsConfFile.getName() + ". Therefore getting the " +
                        "default configuration.", e);
                ServiceHolder.setAnalyticsEventSinkConfiguration(new AnalyticsEventSinkConfiguration());
            }
        } else {
            ServiceHolder.setAnalyticsEventSinkConfiguration(new AnalyticsEventSinkConfiguration());
        }
    }
    
    // (추가)  
    private void deployDefaultEventSinkConfiguration() {
    	String mappingResourcePath = BarleyUtils.getCarbonConfigDirPath() + File.separator + AnalyticsEventSinkConstants.DEPLOYMENT_DIR_NAME;
    	AnalyticsEventStoreDeployer deployer = new AnalyticsEventStoreDeployer();
    	try {
    		File mappingResourceDir = new File(mappingResourcePath);
    		if(mappingResourceDir.exists()) {
    			for(File configFile : mappingResourceDir.listFiles()) {
    				deployer.deploy(new DeploymentFileData(configFile));
    			}
    		}
        } catch (DeploymentException e) {
            log.error(e.getMessage(), e);
        }
	}
    
    public void deactivate(ComponentContext componentContext) {
        if (log.isDebugEnabled()) {
            log.debug("Stopped AnalyticsEventSink component");
        }
    }

    public void setStreamDefinitionStoreService(AbstractStreamDefinitionStore abstractStreamDefinitionStore) {
        ServiceHolder.setStreamDefinitionStoreService(abstractStreamDefinitionStore);
    }

    public void unsetStreamDefinitionStoreService(AbstractStreamDefinitionStore abstractStreamDefinitionStore) {
        ServiceHolder.setStreamDefinitionStoreService(null);
    }

    public void setEventStreamService(EventStreamService eventStreamService) {
        ServiceHolder.setAnalyticsEventStreamListener(new AnalyticsEventStreamListener());
        ServiceHolder.setEventStreamService(eventStreamService);
    }

    public void unsetEventStreamService(EventStreamService eventStreamService) {
        ServiceHolder.setEventStreamService(null);
    }

    public void setAnalyticsDataAPI(AnalyticsDataAPI analyticsDataAPI) {
        ServiceHolder.setAnalyticsDataAPI(analyticsDataAPI);
    }

    public void unsetAnalyticsDataAPI(AnalyticsDataAPI analyticsDataAPI) {
        ServiceHolder.setAnalyticsDataAPI(null);
    }

    public void setEventManagementService(EventManagementService eventManagementService) {
        ServiceHolder.setEventManagementService(eventManagementService);
    }

    public void unsetEventManagementService(EventManagementService eventManagementService) {
        ServiceHolder.setEventManagementService(null);
    }
}
