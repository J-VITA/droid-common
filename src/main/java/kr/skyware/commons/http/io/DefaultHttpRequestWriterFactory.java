package kr.skyware.commons.http.io;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.factory.HttpMessageWriterFactory;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.message.BasicLineFormatter;
import kr.skyware.commons.http.message.LineFormatter;

@Immutable
public class DefaultHttpRequestWriterFactory implements HttpMessageWriterFactory<HttpRequest> {

    public static final DefaultHttpRequestWriterFactory INSTANCE = new DefaultHttpRequestWriterFactory();

    private final LineFormatter lineFormatter;

    public DefaultHttpRequestWriterFactory(final LineFormatter lineFormatter) {
        super();
        this.lineFormatter = lineFormatter != null ? lineFormatter : BasicLineFormatter.INSTANCE;
    }

    public DefaultHttpRequestWriterFactory() {
        this(null);
    }

    public HttpMessageWriter<HttpRequest> create(final SessionOutputBuffer buffer) {
        return new DefaultHttpRequestWriter(buffer, lineFormatter);
    }

}
