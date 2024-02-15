package com.marcnuri.demo.quarkus;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.api.model.Route;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/kubernetes")
public class KubernetesResource {

  private final KubernetesClient kubernetesClient;

  @Inject
  public KubernetesResource(KubernetesClient kubernetesClient) {
    this.kubernetesClient = kubernetesClient;
  }

  @GET
  @Path("/version")
  public String get() {
    return kubernetesClient.getKubernetesVersion().getMajor() + "." + kubernetesClient.getKubernetesVersion().getMinor();
  }

  @GET
  @Path("/routes")
  public List<String> routes() {
    if (kubernetesClient.supports(Route.class)) {
      return kubernetesClient.resources(Route.class).list().getItems().stream()
        .map(Route::getMetadata)
        .map(ObjectMeta::getName)
        .toList();
    }
    throw new NotFoundException();
  }
}
