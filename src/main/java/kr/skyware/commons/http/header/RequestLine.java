package kr.skyware.commons.http.header;

import kr.skyware.commons.http.util.ProtocolVersion;

public interface RequestLine {
    String getMethod();

    ProtocolVersion getProtocolVersion();

    String getUri();
}
