/*
 * Copyright 2017 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.kubernetes;

import com.netflix.spinnaker.halyard.config.model.v1.node.DeploymentConfiguration;
import com.netflix.spinnaker.halyard.core.job.v1.JobExecutor;
import com.netflix.spinnaker.halyard.deploy.services.v1.ArtifactService;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.OrcaBootstrapService;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.OrcaService;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.ServiceInterfaceFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Component
@Data
public class KubernetesOrcaBootstrapService extends OrcaBootstrapService implements KubernetesDeployableService<OrcaService.Orca> {
  @Autowired
  private String dockerRegistry;

  @Autowired
  KubernetesMonitoringDaemonService monitoringDaemonService;

  @Autowired
  ArtifactService artifactService;

  @Autowired
  ServiceInterfaceFactory serviceInterfaceFactory;

  @Autowired
  JobExecutor jobExecutor;

  @Override
  public Settings buildServiceSettings(DeploymentConfiguration deploymentConfiguration) {
    List<String> profiles = new ArrayList<>();
    profiles.add("bootstrap");
    Settings settings = new Settings(profiles);
    settings.setAddress(buildAddress())
        .setArtifactId(getArtifactId(deploymentConfiguration.getName()))
        .setEnabled(true);
    return settings;
  }

  public String getArtifactId(String deploymentName) {
    return KubernetesDeployableService.super.getArtifactId(deploymentName);
  }

  final DeployPriority deployPriority = new DeployPriority(10);
  final boolean requiredToBootstrap = true;
}
