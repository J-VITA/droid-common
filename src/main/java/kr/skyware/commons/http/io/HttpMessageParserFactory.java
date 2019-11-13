package kr.skyware.commons.http.io;

import kr.skyware.commons.http.HttpMessage;
import kr.skyware.commons.http.util.args.MessageConstraints;

public interface HttpMessageParserFactory<T extends HttpMessage> {

    HttpMessageParser<T> create(SessionInputBuffer buffer, MessageConstraints constraints);

}
