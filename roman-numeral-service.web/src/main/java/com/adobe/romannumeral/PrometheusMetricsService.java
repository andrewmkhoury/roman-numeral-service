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

import java.util.List;

import javax.servlet.ServletException;

import org.apache.sling.commons.metrics.MetricsService;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.MetricsServlet;
import io.prometheus.client.hotspot.DefaultExports;

/**
 * A simple DS component which is executed every 10 seconds
 *
 * @see <a href=
 *      "https://sling.apache.org/documentation/bundles/scheduler-service-commons-scheduler.html">Scheduler
 *      Service</a>
 */
@Component
public class PrometheusMetricsService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String URI_ALIAS = "/metrics";
	
	@Reference
	private HttpService httpService;

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	private volatile List<MetricRegistry> metricRegistries;
	
	@Reference
	private MetricsService metricsService;

	public void run() {
		log.info("Running...");
	}

	@Activate
	protected void activate(BundleContext context) {
		registerAll(CollectorRegistry.defaultRegistry);
		DefaultExports.initialize();
		
		try {
			httpService.registerServlet(URI_ALIAS, new MetricsServlet(), null, httpService.createDefaultHttpContext());
		} catch (ServletException e) {
			log.error("Unable to register " + URI_ALIAS + " servlet for Prometheus monitoring", e);
		} catch (org.osgi.service.http.NamespaceException e) {
			log.error("Unable to register " + URI_ALIAS + " servlet for Prometheus monitoring", e);
		}
	}

	@Deactivate
	protected void deactivate(BundleContext context) {
		httpService.unregister(URI_ALIAS);
	}

	private void registerAll(CollectorRegistry collector) {
		for (MetricRegistry registryEntry : metricRegistries) {
			collector.register(new DropwizardExports(registryEntry));
		}
	}
}
