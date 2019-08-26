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
package barley.analytics.spark.core.jdbc;

/**
 * This class represents the type mappings required by Spark when using JDBC connections to a database.
 */
public class SparkJDBCTypeMapping {

    // -- Type mapping -- //
    private String binaryType;

    private String booleanType;

    private String byteType;

    private String dateType;

    private String doubleType;

    private String floatType;

    private String integerType;

    private String longType;

    private String nullType;

    private String shortType;

    private String stringType;

    private String timestampType;


    public String getBinaryType() {
        return binaryType;
    }

    public void setBinaryType(String binaryType) {
        this.binaryType = binaryType;
    }

    public String getBooleanType() {
        return booleanType;
    }

    public void setBooleanType(String booleanType) {
        this.booleanType = booleanType;
    }

    public String getByteType() {
        return byteType;
    }

    public void setByteType(String byteType) {
        this.byteType = byteType;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getDoubleType() {
        return doubleType;
    }

    public void setDoubleType(String doubleType) {
        this.doubleType = doubleType;
    }

    public String getFloatType() {
        return floatType;
    }

    public void setFloatType(String floatType) {
        this.floatType = floatType;
    }

    public String getIntegerType() {
        return integerType;
    }

    public void setIntegerType(String integerType) {
        this.integerType = integerType;
    }

    public String getLongType() {
        return longType;
    }

    public void setLongType(String longType) {
        this.longType = longType;
    }

    public String getNullType() {
        return nullType;
    }

    public void setNullType(String nullType) {
        this.nullType = nullType;
    }

    public String getShortType() {
        return shortType;
    }

    public void setShortType(String shortType) {
        this.shortType = shortType;
    }

    public String getStringType() {
        return stringType;
    }

    public void setStringType(String stringType) {
        this.stringType = stringType;
    }

    public String getTimestampType() {
        return timestampType;
    }

    public void setTimestampType(String timestampType) {
        this.timestampType = timestampType;
    }

}
