package dmitriy.dolbik.oidc.client.testing.client;

import io.quarkus.oidc.token.propagation.common.AccessToken;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

//@RegisterRestClient(configKey="rest-client-with-token-propagation-filter")
@RegisterRestClient
@AccessToken
@Path("/")
public interface RestClientWithTokenPropagationFilter {

    @GET
    @Produces("text/plain")
    @Path("userName")
    Uni<String> getUserName();

    @GET
    @Produces("text/plain")
    @Path("adminName")
    Uni<String> getAdminName();
}
