package kr.skyware.commons.http.io;

import java.io.IOException;

import kr.skyware.commons.http.header.HttpParams;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.message.LineFormatter;

public class HttpRequestWriter extends AbstractMessageWriter<HttpRequest> {

    public HttpRequestWriter(final SessionOutputBuffer buffer,
                             final LineFormatter formatter,
                             final HttpParams params) {
        super(buffer, formatter, params);
    }

    @Override
    protected void writeHeadLine(final HttpRequest message) throws IOException {
        lineFormatter.formatRequestLine(this.lineBuf, message.getRequestLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }

}
