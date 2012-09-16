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
import aQute.bnd.annotation.component.Modified;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.karaf.scr.examples.managed.service.ManagedGreeterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of the ManagedGreeterService interface. Component
 * configuration includes setting the name attribute and setting the
 * configuration policy to required. The default is optional and when the
 * component attempts to activate it will throw a RuntimeException.
 * 
 * @author sully6768
 */
// The ConfigAdmin PID of our component
@Component(name = ManagedGreeterServiceImpl.COMPONENT_NAME,
// The policy that makes our configuration a required dependency
configurationPolicy = ConfigurationPolicy.require)
public class ManagedGreeterServiceImpl implements ManagedGreeterService {

    public static final String COMPONENT_NAME = "ManagedGreeterService";
    public static final String COMPONENT_LABEL = "Managed Greeeter Service";

    private static final Logger LOG = LoggerFactory.getLogger(ManagedGreeterServiceImpl.class);

    private ExecutorService executor = Executors.newCachedThreadPool();
    private Worker worker = new Worker();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Called when all of the SCR Components required dependencies have been
     * satisfied.
     */
    @Activate
    public void activate(final Map<String, ?> properties) {
        LOG.info("Activating the " + COMPONENT_LABEL);

        // Just because our component has a policy of required doesn't help to
        // ensure our properties are set.
        //
        // First check that salutation is set
        if (properties.containsKey("salutation")) {
            try {
                lock.writeLock().lock();
                worker.setSalutation((String)properties.get("salutation"));
            } finally {
                lock.writeLock().unlock();
            }
        } else {
            throw new IllegalArgumentException("The salutation property may not be null or empty: " + properties.get("salutation"));
        }

        // Now verify that name is set
        if (properties.containsKey("name")) {
            try {
                lock.writeLock().lock();
                worker.setName((String)properties.get("name"));
            } finally {
                lock.writeLock().unlock();
            }
        } else {
            throw new IllegalArgumentException("The name property may not be null or empty: " + properties.get("name"));
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

//    /**
//     * Called when the configuration associated with this component has been
//     * updated.
//     */
//    @Modified
//    public void modified(final Map<String, ?> properties) {
//        LOG.info("Modifying the " + COMPONENT_LABEL);
//
//        // This time we really only need to make sure if it changed it isn't
//        // empty
//        if (properties.containsKey("salutation") && !properties.get("salutation").equals("")) {
//            try {
//                lock.writeLock().lock();
//                worker.setSalutation((String)properties.get("salutation"));
//            } finally {
//                lock.writeLock().unlock();
//            }
//        }
//
//        // Same for name
//        if (properties.containsKey("name") && !properties.get("name").equals("")) {
//            try {
//                lock.writeLock().lock();
//                worker.setName((String)properties.get("name"));
//            } finally {
//                lock.writeLock().unlock();
//            }
//        }
//    }

    public void startGreeter() {
        try {
            lock.writeLock().lock();
            executor.execute(worker);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void stopGreeter() {
        try {
            lock.writeLock().lock();
            if (!executor.isTerminated()) {
                executor.shutdownNow();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Thread worker that continuously prints a message.
     */
    private class Worker implements Runnable {

        private String name;
        private String salutation;

        public void run() {
            boolean running = true;
            int messageCount = 0;
            while (running) {
                try {
                    LOG.info("Message " + (++messageCount) + ": " + salutation + " " + name);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    running = false;
                    LOG.info("Thread shutting down");
                }
            }
        }

        public void setName(String userName) {
            this.name = userName;
        }

        public void setSalutation(String salutation) {
            this.salutation = salutation;
        }
    }
}
