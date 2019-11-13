package kr.skyware.commons.http.config;

/**
 * Generic lookup by low-case string ID.
 *
 * @since 4.3
 */
public interface Lookup<I> {

    I lookup(String name);

}
