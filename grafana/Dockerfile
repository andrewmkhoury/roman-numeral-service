FROM grafana/grafana
ENV GF_INSTALL_PLUGINS grafana-clock-panel,grafana-simple-json-datasource
ENV DS_PROM Prometheus
COPY provisioning /etc/grafana/provisioning