package kr.skyware.commons.http.client.execchain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import kr.skyware.commons.http.HttpEntity;
import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.config.HttpEntityWrapper;
import kr.skyware.commons.http.connect.EofSensorInputStream;
import kr.skyware.commons.http.connect.EofSensorWatcher;

@NotThreadSafe
class ResponseEntityProxy extends HttpEntityWrapper implements EofSensorWatcher {

    private final ConnectionHolder connHolder;

    public static void enchance(final HttpResponse response, final ConnectionHolder connHolder) {
        final HttpEntity entity = response.getEntity();
        if (entity != null && entity.isStreaming() && connHolder != null) {
            response.setEntity(new ResponseEntityProxy(entity, connHolder));
        }
    }

    ResponseEntityProxy(final HttpEntity entity, final ConnectionHolder connHolder) {
        super(entity);
        this.connHolder = connHolder;
    }

    private void cleanup() {
        if (this.connHolder != null) {
            this.connHolder.abortConnection();
        }
    }

    public void releaseConnection() throws IOException {
        if (this.connHolder != null) {
            try {
                if (this.connHolder.isReusable()) {
                    this.connHolder.releaseConnection();
                }
            } finally {
                cleanup();
            }
        }
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public InputStream getContent() throws IOException {
        return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
    }

    @Deprecated
    @Override
    public void consumeContent() throws IOException {
        releaseConnection();
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        try {
            this.wrappedEntity.writeTo(outstream);
            releaseConnection();
        } finally {
            cleanup();
        }
    }

    public boolean eofDetected(final InputStream wrapped) throws IOException {
        try {
            // there may be some cleanup required, such as
            // reading trailers after the response body:
            wrapped.close();
            releaseConnection();
        } finally {
            cleanup();
        }
        return false;
    }

    public boolean streamClosed(final InputStream wrapped) throws IOException {
        try {
            final boolean open = connHolder != null && !connHolder.isReleased();
            // this assumes that closing the stream will
            // consume the remainder of the response body:
            try {
                wrapped.close();
                releaseConnection();
            } catch (final SocketException ex) {
                if (open) {
                    throw ex;
                }
            }
        } finally {
            cleanup();
        }
        return false;
    }

    public boolean streamAbort(final InputStream wrapped) throws IOException {
        cleanup();
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResponseEntityProxy{");
        sb.append(wrappedEntity);
        sb.append('}');
        return sb.toString();
    }

}
