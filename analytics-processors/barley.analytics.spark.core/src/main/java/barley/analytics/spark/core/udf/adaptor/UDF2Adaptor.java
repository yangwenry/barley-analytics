/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
 */

package barley.analytics.spark.core.udf.adaptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.sql.api.java.UDF2;

import barley.analytics.spark.core.exception.AnalyticsUDFException;

/**
 * This class represents custom UDF type 2 adaptor
 */
public class UDF2Adaptor implements UDF2 {

    private static final long serialVersionUID = 2529251043136863273L;
    private static Log log = LogFactory.getLog(UDF2Adaptor.class);
    private Class<Object> udfClass;
    private String udfMethodName;
    private Class[] parameterTypes;

    public UDF2Adaptor(Class<Object> udfClass, String udfMethodName, Class[] parameterTypes)
            throws AnalyticsUDFException {
        try {
            this.udfClass = udfClass;
            this.udfMethodName = udfMethodName;
            this.parameterTypes = parameterTypes;
        } catch (Exception e) {
            throw new  AnalyticsUDFException("Error while initializing UDF: " + e.getMessage(), e);
        }
    }

    @Override
    public Object call(Object o, Object o2) throws Exception {
        Method udfMethod = udfClass.getDeclaredMethod(udfMethodName, parameterTypes);
        try {
            if (Modifier.isStatic(udfMethod.getModifiers())) {
                return udfMethod.invoke(null, o, o2);
            } else {
                Object udfInstance = udfClass.newInstance();
                return udfMethod.invoke(udfInstance, o, o2);
            }
        } catch (InvocationTargetException e) {
            log.error("Error while invoking method: " + udfMethodName + ", " +e.getMessage(), e.getCause());
            throw new Exception("Error while invoking method: " + udfMethodName + ", " +e.getMessage(), e.getCause());
        }
    }
}
