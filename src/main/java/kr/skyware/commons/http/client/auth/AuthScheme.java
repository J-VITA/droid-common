package kr.skyware.commons.http.client.auth;

import kr.skyware.commons.http.exception.AuthenticationException;
import kr.skyware.commons.http.exception.MalformedChallengeException;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HttpRequest;

public interface AuthScheme {

    /**
     * Processes the given challenge token. Some authentication schemes
     * may involve multiple challenge-response exchanges. Such schemes must be able
     * to maintain the state information when dealing with sequential challenges
     *
     * @param header the challenge header
     */
    void processChallenge(final Header header) throws MalformedChallengeException;

    /**
     * Returns textual designation of the given authentication scheme.
     *
     * @return the name of the given authentication scheme
     */
    String getSchemeName();

    /**
     * Returns authentication parameter with the given name, if available.
     *
     * @param name The name of the parameter to be returned
     *
     * @return the parameter with the given name
     */
    String getParameter(final String name);

    /**
     * Returns authentication realm. If the concept of an authentication
     * realm is not applicable to the given authentication scheme, returns
     * <code>null</code>.
     *
     * @return the authentication realm
     */
    String getRealm();

    /**
     * Tests if the authentication scheme is provides authorization on a per
     * connection basis instead of usual per request basis
     *
     * @return <tt>true</tt> if the scheme is connection based, <tt>false</tt>
     * if the scheme is request based.
     */
    boolean isConnectionBased();

    /**
     * Authentication process may involve a series of challenge-response exchanges.
     * This method tests if the authorization process has been completed, either
     * successfully or unsuccessfully, that is, all the required authorization
     * challenges have been processed in their entirety.
     *
     * @return <tt>true</tt> if the authentication process has been completed,
     * <tt>false</tt> otherwise.
     */
    boolean isComplete();

    /**
     * Produces an authorization string for the given set of {@link Credentials}.
     *
     * @param credentials The set of credentials to be used for athentication
     * @param request The request being authenticated
     * @throws AuthenticationException if authorization string cannot
     *   be generated due to an authentication failure
     *
     * @return the authorization string
     *
     * @deprecated (4.1)  Use {@link ContextAwareAuthScheme#authenticate(Credentials, HttpRequest, HttpContext)}
     */
    @Deprecated
    Header authenticate(Credentials credentials, HttpRequest request)
            throws AuthenticationException;

}
