package kr.skyware.commons.http.entity;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.builder.URLEncodedUtils;
import kr.skyware.commons.http.header.ContentType;
import kr.skyware.commons.http.header.NameValuePair;
import kr.skyware.commons.http.util.HTTP;

@NotThreadSafe // AbstractHttpEntity is not thread-safe
public class UrlEncodedFormEntity extends StringEntity {

    /**
     * Constructs a new {@link UrlEncodedFormEntity} with the list
     * of parameters in the specified encoding.
     *
     * @param parameters list of name/value pairs
     * @param charset encoding the name/value pairs be encoded with
     * @throws UnsupportedEncodingException if the encoding isn't supported
     */
    public UrlEncodedFormEntity (
            final List<? extends NameValuePair> parameters,
            final String charset) throws UnsupportedEncodingException {
        super(URLEncodedUtils.format(parameters,
                charset != null ? charset : HTTP.DEF_CONTENT_CHARSET.name()),
                ContentType.create(URLEncodedUtils.CONTENT_TYPE, charset));
    }

    /**
     * Constructs a new {@link UrlEncodedFormEntity} with the list
     * of parameters in the specified encoding.
     *
     * @param parameters iterable collection of name/value pairs
     * @param charset encoding the name/value pairs be encoded with
     *
     * @since 4.2
     */
    public UrlEncodedFormEntity (
            final Iterable <? extends NameValuePair> parameters,
            final Charset charset) {
        super(URLEncodedUtils.format(parameters,
                charset != null ? charset : HTTP.DEF_CONTENT_CHARSET),
                ContentType.create(URLEncodedUtils.CONTENT_TYPE, charset));
    }

    /**
     * Constructs a new {@link UrlEncodedFormEntity} with the list
     * of parameters with the default encoding of {@link HTTP#DEFAULT_CONTENT_CHARSET}
     *
     * @param parameters list of name/value pairs
     * @throws UnsupportedEncodingException if the default encoding isn't supported
     */
    public UrlEncodedFormEntity (
            final List <? extends NameValuePair> parameters) throws UnsupportedEncodingException {
        this(parameters, (Charset) null);
    }

    /**
     * Constructs a new {@link UrlEncodedFormEntity} with the list
     * of parameters with the default encoding of {@link HTTP#DEFAULT_CONTENT_CHARSET}
     *
     * @param parameters iterable collection of name/value pairs
     *
     * @since 4.2
     */
    public UrlEncodedFormEntity (
            final Iterable <? extends NameValuePair> parameters) {
        this(parameters, null);
    }

}
