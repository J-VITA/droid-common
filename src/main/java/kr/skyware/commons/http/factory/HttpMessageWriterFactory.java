package kr.skyware.commons.http.factory;

import kr.skyware.commons.http.HttpMessage;
import kr.skyware.commons.http.io.HttpMessageWriter;
import kr.skyware.commons.http.io.SessionOutputBuffer;

public interface HttpMessageWriterFactory<T extends HttpMessage> {

    HttpMessageWriter<T> create(SessionOutputBuffer buffer);

}
