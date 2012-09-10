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
package org.apache.karaf.scr.examples.managed.service.impl;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;
import aQute.bnd.annotation.component.Deactivate;

import java.util.Map;

import org.apache.karaf.scr.examples.managed.service.ManagedGreeterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(name=ManagedGreeterServiceImpl.COMPONENT_NAME, 
           configurationPolicy=ConfigurationPolicy.require,
           properties={"greeter.service=managed"})
public class ManagedGreeterServiceImpl implements ManagedGreeterService {

    public static final String COMPONENT_NAME = "ManagedGreeterService";

    public static final String COMPONENT_LABEL = "Managed Greeeter Service";
    
    private static final Logger LOG = LoggerFactory.getLogger(ManagedGreeterServiceImpl.class);

    private String name;

    private String salutation;

    /**
     * Called when all of the SCR Components required dependencies have been
     * satisfied.
     */
    @Activate
    public void activate(final Map<String, ?> properties) {
        LOG.info("Activating the " + COMPONENT_LABEL);
        if (properties.containsKey("salutation")) {
            salutation = (String)properties.get("salutation");
        } else {
            throw new IllegalArgumentException("The salutation property may not be null or empty: " + salutation);
        }
        if (properties.containsKey("name")) {
            name = (String)properties.get("name");
        } else {
            throw new IllegalArgumentException("The salutation property may not be null or empty: " + salutation);
        }
    }

    /**
     * Called when any of the SCR Components required dependencies become
     * unsatisfied.
     */
    @Deactivate
    public void deactivate() {
        LOG.info("Deactivating the " + COMPONENT_LABEL);
    }
    
//    @Modified
//    public void modified(final Map<String, ?> properties) {
//        LOG.info("Modifying the " + COMPONENT_LABEL);
//        if (properties.containsKey("salutation")) {
//            salutation = (String)properties.get("salutation");
//        } else {
//            throw new IllegalArgumentException("The salutation property may not be null or empty: " + salutation);
//        }
//        if (properties.containsKey("name")) {
//            name = (String)properties.get("name");
//        } else {
//            throw new IllegalArgumentException("The salutation property may not be null or empty: " + salutation);
//        }
//    }

    public void printGreetings() {
        LOG.info(salutation + " " + name);
    }
}
