package kr.skyware.commons.http.client.execchain;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.HttpException;

@Immutable
public class TunnelRefusedException extends HttpException {

    private static final long serialVersionUID = -8646722842745617323L;

    private final HttpResponse response;

    public TunnelRefusedException(final String message, final HttpResponse response) {
        super(message);
        this.response = response;
    }

    public HttpResponse getResponse() {
        return this.response;
    }

}
