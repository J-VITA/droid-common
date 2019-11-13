package kr.skyware.commons.http.io;

import java.io.IOException;

import kr.skyware.commons.http.HttpMessage;
import kr.skyware.commons.http.exception.HttpException;

public interface HttpMessageWriter<T extends HttpMessage> {

    /**
     * Serializes an instance of {@link HttpMessage} to the underlying data
     * sink.
     *
     * @param message HTTP message
     * @throws IOException in case of an I/O error
     * @throws HttpException in case of HTTP protocol violation
     */
    void write(T message)
            throws IOException, HttpException;

}
