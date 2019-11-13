package kr.skyware.commons.http.header;

import kr.skyware.commons.http.HttpMessage;

public interface HttpRequest extends HttpMessage {

    /**
     * Returns the request line of this request.
     * @return the request line.
     */
    RequestLine getRequestLine();

}