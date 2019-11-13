package kr.skyware.commons.http.connect.ssl;


import kr.skyware.commons.http.annotation.Immutable;

@Immutable
public class AllowAllHostnameVerifier extends AbstractVerifier {

    public final void verify(
            final String host,
            final String[] cns,
            final String[] subjectAlts) {
        // Allow everything - so never blowup.
    }

    @Override
    public final String toString() {
        return "ALLOW_ALL";
    }

}