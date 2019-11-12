package kr.skyware.commons.http.json;

public interface JsonValueInterface {
    /**
     * Returns the escaped, ready-to-be used value of this encapsulated object.
     *
     * @return byte array holding the data to be used (as-is) in a JSON object
     */
    byte[] getEscapedJsonValue();
}
