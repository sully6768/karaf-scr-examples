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
package org.apache.karaf.scr.examples.managed.service.component;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import org.apache.karaf.scr.examples.managed.service.ManagedGreeterService;
import org.osgi.service.log.LogService;

@Component(name = ManagedGreeterServiceComponent.COMPONENT_NAME)
public class ManagedGreeterServiceComponent {

    public static final String COMPONENT_NAME = "ManagedGreeterServiceComponent";

    public static final String COMPONENT_LABEL = "Managed GreeterService Component";

    private LogService logService;

    private ManagedGreeterService greeterService;

    /**
     * Called when all of the SCR Components required dependencies have been
     * satisfied.
     */
    @Activate
    public void activate() {
        logService.log(LogService.LOG_INFO, "Activating the " + COMPONENT_LABEL);
        greeterService.printGreetings();
    }

    /**
     * Called when any of the SCR Components required dependencies become
     * unsatisfied.
     */
    @Deactivate
    public void deactivate() {
        logService.log(LogService.LOG_INFO, "Deactivating the " + COMPONENT_LABEL);
    }

    @Reference(target="(greeter.service=managed)")
    public void setExampleService(final ManagedGreeterService greeterService) {
        this.greeterService = greeterService;
    }

    public void unsetGreeterService(final ManagedGreeterService greeterService) {
        this.greeterService = null;
    }

    @Reference
    protected void setLogService(LogService logService) {
        this.logService = logService;
    }

    protected void unsetLogService(LogService logService) {
        this.logService = null;
    }

}
