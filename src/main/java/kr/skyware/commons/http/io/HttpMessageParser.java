package kr.skyware.commons.http.io;

import java.io.IOException;

import kr.skyware.commons.http.HttpMessage;
import kr.skyware.commons.http.exception.HttpException;

public interface HttpMessageParser<T extends HttpMessage> {

    /**
     * Generates an instance of {@link HttpMessage} from the underlying data
     * source.
     *
     * @return HTTP message
     * @throws IOException in case of an I/O error
     * @throws HttpException in case of HTTP protocol violation
     */
    T parse()
            throws IOException, HttpException;

}
