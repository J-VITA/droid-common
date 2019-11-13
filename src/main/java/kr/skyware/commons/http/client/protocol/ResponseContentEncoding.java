package kr.skyware.commons.http.client.protocol;

import java.io.IOException;
import java.util.Locale;

import kr.skyware.commons.http.HttpEntity;
import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.entity.DeflateDecompressingEntity;
import kr.skyware.commons.http.entity.GzipDecompressingEntity;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HeaderElement;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.interceptor.HttpResponseInterceptor;

@Immutable
public class ResponseContentEncoding implements HttpResponseInterceptor {

    public static final String UNCOMPRESSED = "http.client.response.uncompressed";

    /**
     * Handles the following {@code Content-Encoding}s by
     * using the appropriate decompressor to wrap the response Entity:
     * <ul>
     * <li>gzip - see {@link GzipDecompressingEntity}</li>
     * <li>deflate - see {@link DeflateDecompressingEntity}</li>
     * <li>identity - no action needed</li>
     * </ul>
     *
     * @param response the response which contains the entity
     * @param  context not currently used
     *
     * @throws HttpException if the {@code Content-Encoding} is none of the above
     */
    public void process(
            final HttpResponse response,
            final HttpContext context) throws HttpException, IOException {
        final HttpEntity entity = response.getEntity();

        // entity can be null in case of 304 Not Modified, 204 No Content or similar
        // check for zero length entity.
        if (entity != null && entity.getContentLength() != 0) {
            final Header ceheader = entity.getContentEncoding();
            if (ceheader != null) {
                final HeaderElement[] codecs = ceheader.getElements();
                boolean uncompressed = false;
                for (final HeaderElement codec : codecs) {
                    final String codecname = codec.getName().toLowerCase(Locale.ENGLISH);
                    if ("gzip".equals(codecname) || "x-gzip".equals(codecname)) {
                        response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                        uncompressed = true;
                        break;
                    } else if ("deflate".equals(codecname)) {
                        response.setEntity(new DeflateDecompressingEntity(response.getEntity()));
                        uncompressed = true;
                        break;
                    } else if ("identity".equals(codecname)) {

                        /* Don't need to transform the content - no-op */
                        return;
                    } else {
                        throw new HttpException("Unsupported Content-Coding: " + codec.getName());
                    }
                }
                if (uncompressed) {
                    response.removeHeaders("Content-Length");
                    response.removeHeaders("Content-Encoding");
                    response.removeHeaders("Content-MD5");
                }
            }
        }
    }

}
