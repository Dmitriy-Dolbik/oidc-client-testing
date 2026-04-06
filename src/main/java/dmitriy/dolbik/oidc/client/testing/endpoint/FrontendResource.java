package dmitriy.dolbik.oidc.client.testing.endpoint;

import dmitriy.dolbik.oidc.client.testing.OidcClientCreator;
import dmitriy.dolbik.oidc.client.testing.client.RestClientWithOidcClientFilter;
import dmitriy.dolbik.oidc.client.testing.client.RestClientWithTokenHeaderParam;
import dmitriy.dolbik.oidc.client.testing.client.RestClientWithTokenPropagationFilter;
import io.quarkus.oidc.client.Tokens;
import io.quarkus.oidc.client.runtime.TokensHelper;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/frontend")
public class FrontendResource {
    public static final String BEARER = "Bearer ";
    @Inject
    @RestClient
    RestClientWithOidcClientFilter restClientWithOidcClientFilter;

    @Inject
    @RestClient
    RestClientWithTokenPropagationFilter restClientWithTokenPropagationFilter;

    @Inject
    OidcClientCreator oidcClientCreator;
    TokensHelper tokenHelper = new TokensHelper();

    @Inject
    @RestClient
    RestClientWithTokenHeaderParam restClientWithTokenHeaderParam;

    @GET
    @Path("user-name-with-oidc-client-token")
    @Produces("text/plain")
    public Uni<String> getUserNameWithOidcClientToken() {
        return restClientWithOidcClientFilter.getUserName();
    }

    @GET
    @Path("admin-name-with-oidc-client-token")
    @Produces("text/plain")
    public Uni<String> getAdminNameWithOidcClientToken() {
        return restClientWithOidcClientFilter.getAdminName();
    }

    @GET
    @Path("user-name-with-propagated-token")
    @Produces("text/plain")
    public Uni<String> getUserNameWithPropagatedToken() {
        return restClientWithTokenPropagationFilter.getUserName();
    }

    @GET
    @Path("admin-name-with-propagated-token")
    @Produces("text/plain")
    public Uni<String> getAdminNameWithPropagatedToken() {
        return restClientWithTokenPropagationFilter.getAdminName();
    }

    @GET
    @Path("user-name-with-oidc-client-token-header-param")
    @Produces("text/plain")
    public Uni<String> getUserNameWithOidcClientTokenHeaderParam() {
        return tokenHelper.getTokens(oidcClientCreator.getOidcClient()).onItem()
                .transformToUni(tokens -> restClientWithTokenHeaderParam.getUserName(BEARER + tokens.getAccessToken()));
    }

    @GET
    @Path("admin-name-with-oidc-client-token-header-param")
    @Produces("text/plain")
    public Uni<String> getAdminNameWithOidcClientTokenHeaderParam() {
        return tokenHelper.getTokens(oidcClientCreator.getOidcClient()).onItem()
                .transformToUni(tokens -> restClientWithTokenHeaderParam.getAdminName(BEARER + tokens.getAccessToken()));
    }

    @GET
    @Path("user-name-with-oidc-client-token-header-param-blocking")
    @Produces("text/plain")
    public String getUserNameWithOidcClientTokenHeaderParamBlocking() {
        Tokens tokens = tokenHelper.getTokens(oidcClientCreator.getOidcClient()).await().indefinitely();
        return restClientWithTokenHeaderParam.getUserName(BEARER + tokens.getAccessToken()).await().indefinitely();
    }

    @GET
    @Path("admin-name-with-oidc-client-token-header-param-blocking")
    @Produces("text/plain")
    public String getAdminNameWithOidcClientTokenHeaderParamBlocking() {
        Tokens tokens = tokenHelper.getTokens(oidcClientCreator.getOidcClient()).await().indefinitely();
        return restClientWithTokenHeaderParam.getAdminName(BEARER + tokens.getAccessToken()).await().indefinitely();
    }

}
