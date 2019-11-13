package kr.skyware.commons.http.client.auth;

import kr.skyware.commons.http.exception.AuthenticationException;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpRequest;

public interface ContextAwareAuthScheme extends AuthScheme {

    /**
     * Produces an authorization string for the given set of
     * {@link Credentials}.
     *
     * @param credentials The set of credentials to be used for athentication
     * @param request The request being authenticated
     * @param context HTTP context
     * @throws AuthenticationException if authorization string cannot
     *   be generated due to an authentication failure
     *
     * @return the authorization string
     */
    Header authenticate(
            Credentials credentials,
            HttpRequest request,
            HttpContext context) throws AuthenticationException;

}
