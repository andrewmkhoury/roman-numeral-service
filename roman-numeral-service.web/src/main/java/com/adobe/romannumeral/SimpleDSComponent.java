/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.adobe.romannumeral;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.sling.commons.metrics.MetricsService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;

/**
 * A simple DS component which is executed every 10 seconds
 *
 * @see <a href=
 *      "https://sling.apache.org/documentation/bundles/scheduler-service-commons-scheduler.html">Scheduler
 *      Service</a>
 */
@Component(property = { "scheduler.period:Long=10" })
public class SimpleDSComponent implements Runnable {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private BundleContext bundleContext;

	@Reference
	private MetricsService metricsService;

	public void run() {
		log.info("Running...");
	}

	protected void activate(ComponentContext ctx) {
		this.bundleContext = ctx.getBundleContext();

		// Inside an initialisation function.
		CollectorRegistry.defaultRegistry.register(new DropwizardExports(getConsolidatedRegistry()));
	}

	protected void deactivate(ComponentContext ctx) {
		this.bundleContext = null;
	}

	public static final String METRIC_REGISTRY_NAME = "name";
	private ConcurrentMap<ServiceReference, MetricRegistry> registries = new ConcurrentHashMap<>();

	MetricRegistry getConsolidatedRegistry() {
		MetricRegistry registry = new MetricRegistry();
		for (Map.Entry<ServiceReference, MetricRegistry> registryEntry : registries.entrySet()) {
			String metricRegistryName = (String) registryEntry.getKey().getProperty(METRIC_REGISTRY_NAME);
			for (Map.Entry<String, Metric> metricEntry : registryEntry.getValue().getMetrics().entrySet()) {
				String metricName = metricEntry.getKey();
				try {
					if (metricRegistryName != null) {
						metricName = metricRegistryName + ":" + metricName;
					}
					registry.register(metricName, metricEntry.getValue());
				} catch (IllegalArgumentException ex) {
					log.warn("Duplicate Metric name found {}", metricName, ex);
				}
			}
		}
		return registry;
	}
}
