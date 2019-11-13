package kr.skyware.commons.http.client.protocol;

import java.io.IOException;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.client.auth.AUTH;
import kr.skyware.commons.http.client.auth.AuthState;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.util.Args;

@Immutable
public class RequestTargetAuthentication extends RequestAuthenticationBase {

    public RequestTargetAuthentication() {
        super();
    }

    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        Args.notNull(context, "HTTP context");

        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")) {
            return;
        }

        if (request.containsHeader(AUTH.WWW_AUTH_RESP)) {
            return;
        }

        // Obtain authentication state
        final AuthState authState = (AuthState) context.getAttribute(
                ClientContext.TARGET_AUTH_STATE);
        if (authState == null) {
            this.log.debug("Target auth state not set in the context");
            return;
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("Target auth state: " + authState.getState());
        }
        process(authState, request, context);
    }

}
