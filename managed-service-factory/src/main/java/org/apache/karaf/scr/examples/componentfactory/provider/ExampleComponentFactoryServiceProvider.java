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
package org.apache.karaf.scr.examples.componentfactory.provider;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.karaf.scr.examples.component.service.SimpleService;
import org.apache.karaf.scr.examples.componentfactory.ExampleComponentFactoryService;
import org.osgi.service.log.LogService;

/**
 * TODO Add Class documentation for ExampleComponentFactoryServiceProvider
 * 
 * @author sully6768
 */

@Component(name = ExampleComponentFactoryServiceProvider.COMPONENT_NAME, factory = "example.factory.provider")
public class ExampleComponentFactoryServiceProvider implements ExampleComponentFactoryService {

    public static final String COMPONENT_NAME = "ExampleComponentFactoryServiceProvider";

    public static final String COMPONENT_LABEL = "Example Component Factory Service Provider";

    private LogService logService;

    private SimpleService simpleService;

    private ExecutorService executor = Executors.newFixedThreadPool(2);

    private boolean started = false;

    /**
     * Called when all of the SCR Components required dependencies have been
     * satisfied.
     */
    @Activate
    public void activate(Map<String, Object> properties) {
        logService.log(LogService.LOG_INFO, "Activating the " + COMPONENT_LABEL);
        simpleService.setName((String)properties.get("name"));
        simpleService.setSalutation((String)properties.get("salutation"));
    }

    /**
     * Called when any of the SCR Components required dependencies become
     * unsatisfied.
     */
    @Deactivate
    public void deactivate() {
        logService.log(LogService.LOG_INFO, "Deactivating the " + COMPONENT_LABEL);
    }

    public void start() {
        started = true;
        executor.execute(new Runnable() {
            public void run() {
                try {
                    while (started) {
                        simpleService.printGreetings();
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    logService.log(LogService.LOG_ERROR, "Error running the SimpleService", e);
                }
            }
        });

    }

    public void stop() {
        started = false;
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }

    @Reference
    public void setExampleService(final SimpleService simpleService) {
        this.simpleService = simpleService;
    }

    public void unsetExampleService(final SimpleService simpleService) {
        this.simpleService = null;
    }

    @Reference
    protected void setLogService(LogService logService) {
        this.logService = logService;
    }

    protected void unsetLogService(LogService logService) {
        this.logService = null;
    }
}
