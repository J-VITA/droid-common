package kr.skyware.commons.http.message;

import java.util.NoSuchElementException;

import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.client.HeaderElementIterator;
import kr.skyware.commons.http.header.BasicHeaderValueParser;
import kr.skyware.commons.http.header.FormattedHeader;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HeaderElement;
import kr.skyware.commons.http.header.HeaderIterator;
import kr.skyware.commons.http.header.HeaderValueParser;
import kr.skyware.commons.http.header.ParserCursor;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.CharArrayBuffer;

@NotThreadSafe
public class BasicHeaderElementIterator implements HeaderElementIterator {

    private final HeaderIterator headerIt;
    private final HeaderValueParser parser;

    private HeaderElement currentElement = null;
    private CharArrayBuffer buffer = null;
    private ParserCursor cursor = null;

    /**
     * Creates a new instance of BasicHeaderElementIterator
     */
    public BasicHeaderElementIterator(
            final HeaderIterator headerIterator,
            final HeaderValueParser parser) {
        this.headerIt = Args.notNull(headerIterator, "Header iterator");
        this.parser = Args.notNull(parser, "Parser");
    }


    public BasicHeaderElementIterator(final HeaderIterator headerIterator) {
        this(headerIterator, BasicHeaderValueParser.INSTANCE);
    }


    private void bufferHeaderValue() {
        this.cursor = null;
        this.buffer = null;
        while (this.headerIt.hasNext()) {
            final Header h = this.headerIt.nextHeader();
            if (h instanceof FormattedHeader) {
                this.buffer = ((FormattedHeader) h).getBuffer();
                this.cursor = new ParserCursor(0, this.buffer.length());
                this.cursor.updatePos(((FormattedHeader) h).getValuePos());
                break;
            } else {
                final String value = h.getValue();
                if (value != null) {
                    this.buffer = new CharArrayBuffer(value.length());
                    this.buffer.append(value);
                    this.cursor = new ParserCursor(0, this.buffer.length());
                    break;
                }
            }
        }
    }

    private void parseNextElement() {
        // loop while there are headers left to parse
        while (this.headerIt.hasNext() || this.cursor != null) {
            if (this.cursor == null || this.cursor.atEnd()) {
                // get next header value
                bufferHeaderValue();
            }
            // Anything buffered?
            if (this.cursor != null) {
                // loop while there is data in the buffer
                while (!this.cursor.atEnd()) {
                    final HeaderElement e = this.parser.parseHeaderElement(this.buffer, this.cursor);
                    if (!(e.getName().length() == 0 && e.getValue() == null)) {
                        // Found something
                        this.currentElement = e;
                        return;
                    }
                }
                // if at the end of the buffer
                if (this.cursor.atEnd()) {
                    // discard it
                    this.cursor = null;
                    this.buffer = null;
                }
            }
        }
    }

    public boolean hasNext() {
        if (this.currentElement == null) {
            parseNextElement();
        }
        return this.currentElement != null;
    }

    public HeaderElement nextElement() throws NoSuchElementException {
        if (this.currentElement == null) {
            parseNextElement();
        }

        if (this.currentElement == null) {
            throw new NoSuchElementException("No more header elements available");
        }

        final HeaderElement element = this.currentElement;
        this.currentElement = null;
        return element;
    }

    public final Object next() throws NoSuchElementException {
        return nextElement();
    }

    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Remove not supported");
    }

}
