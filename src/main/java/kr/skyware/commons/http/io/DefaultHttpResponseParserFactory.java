package kr.skyware.commons.http.io;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.client.DefaultHttpResponseFactory;
import kr.skyware.commons.http.factory.HttpResponseFactory;
import kr.skyware.commons.http.message.BasicLineParser;
import kr.skyware.commons.http.message.LineParser;
import kr.skyware.commons.http.util.args.MessageConstraints;

@Immutable
public class DefaultHttpResponseParserFactory implements HttpMessageParserFactory<HttpResponse> {

    public static final DefaultHttpResponseParserFactory INSTANCE = new DefaultHttpResponseParserFactory();

    private final LineParser lineParser;
    private final HttpResponseFactory responseFactory;

    public DefaultHttpResponseParserFactory(final LineParser lineParser,
                                            final HttpResponseFactory responseFactory) {
        super();
        this.lineParser = lineParser != null ? lineParser : BasicLineParser.INSTANCE;
        this.responseFactory = responseFactory != null ? responseFactory
                : DefaultHttpResponseFactory.INSTANCE;
    }

    public DefaultHttpResponseParserFactory() {
        this(null, null);
    }

    public HttpMessageParser<HttpResponse> create(final SessionInputBuffer buffer,
                                                  final MessageConstraints constraints) {
        return new DefaultHttpResponseParser(buffer, lineParser, responseFactory, constraints);
    }

}
