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
package org.apache.karaf.scr.examples.componentfactory;

import java.util.Properties;

import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

@Component(name = ExampleComponentFactoryManager.COMPONENT_NAME, enabled = true, immediate = true)
public class ExampleComponentFactoryManager {

    public static final String COMPONENT_NAME = "ExampleComponentFactoryManager";
    public static final String COMPONENT_LABEL = "Example Component Factory Manager";

    private LogService logService;
    private ComponentFactory factory;
    private ComponentInstance instance;
    private ExampleComponentFactoryService service;

    @Activate
    public synchronized void activate() {
        logService.log(LogService.LOG_INFO, "Activating the " + COMPONENT_LABEL);
        final Properties props = new Properties();

        props.setProperty("name", "Scott ES");
        props.setProperty("salutation", "Howdy");
        instance = factory.newInstance(props);
        service = (ExampleComponentFactoryService)instance.getInstance();
        service.start();
    }

    @Deactivate
    public synchronized void deactivate() {
        service.stop();
        instance.dispose();
    }

    @Reference(target = "(component.factory=example.factory.provider)")
    public void setFactory(final ComponentFactory factory) {
        this.factory = factory;
    }

    public void unsetFactory(final ComponentFactory factory) {
        this.factory = factory;
    }

    @Reference
    protected void setLogService(LogService logService) {
        this.logService = logService;
    }

    protected void unsetLogService(LogService logService) {
        this.logService = null;
    }
}
