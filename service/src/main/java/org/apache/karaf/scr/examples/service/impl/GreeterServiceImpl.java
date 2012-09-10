/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.karaf.scr.examples.service.impl;

import org.apache.karaf.scr.examples.service.GreeterService;
import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

/**
 * <p>
 * The service implementation for for our {@link GreeterService}.
 * </p>
 * <p>
 * With the {@link Component} annotation, any declared inteface on this class will
 * automatically be exported as a service. The DS annotations are build time
 * only though and do not inherit.
 * </p>
 * 
 * @author sully6768
 */
@Component
public class GreeterServiceImpl implements GreeterService {

    private LogService logService;

    private String name = System.getProperty("user.name", "Scott ES");

    private String salutation = "Hello";

    public void printGreetings() {
        logService.log(LogService.LOG_INFO, salutation + " " + name);
    }

    @Reference
    protected void setLogService(LogService logService) {
        this.logService = logService;
    }

    protected void unsetLogService(LogService logService) {
        this.logService = logService;
    }

}
