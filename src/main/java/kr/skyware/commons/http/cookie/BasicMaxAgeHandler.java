package kr.skyware.commons.http.cookie;

import java.util.Date;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.MalformedCookieException;
import kr.skyware.commons.http.util.Args;

@Immutable
public class BasicMaxAgeHandler extends AbstractCookieAttributeHandler {

    public BasicMaxAgeHandler() {
        super();
    }

    public void parse(final SetCookie cookie, final String value)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (value == null) {
            throw new MalformedCookieException("Missing value for max-age attribute");
        }
        final int age;
        try {
            age = Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            throw new MalformedCookieException ("Invalid max-age attribute: "
                    + value);
        }
        if (age < 0) {
            throw new MalformedCookieException ("Negative max-age attribute: "
                    + value);
        }
        cookie.setExpiryDate(new Date(System.currentTimeMillis() + age * 1000L));
    }

}
