package kr.skyware.commons.http.message;

import java.io.Serializable;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.ParseException;
import kr.skyware.commons.http.header.BasicHeaderValueParser;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HeaderElement;
import kr.skyware.commons.http.util.Args;

@Immutable
public class BasicHeader implements Header, Cloneable, Serializable {

    private static final long serialVersionUID = -5427236326487562174L;

    private final String name;
    private final String value;

    /**
     * Constructor with name and value
     *
     * @param name the header name
     * @param value the header value
     */
    public BasicHeader(final String name, final String value) {
        super();
        this.name = Args.notNull(name, "Name");
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        // no need for non-default formatting in toString()
        return BasicLineFormatter.INSTANCE.formatHeader(null, this).toString();
    }

    public HeaderElement[] getElements() throws ParseException {
        if (this.value != null) {
            // result intentionally not cached, it's probably not used again
            return BasicHeaderValueParser.parseElements(this.value, null);
        } else {
            return new HeaderElement[] {};
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
