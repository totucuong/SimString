package simstring;

import java.util.Collection;

/**
 * Created by totucuong-standard on 4/7/17.
 */
public interface StringMatcher {
    /**
     * add all of the strings into a StringMatcher
     * @param strings a collection of strings
     */
    public void addAll(Collection<String> strings);

    /**
     *
     * @param query a text query
     * @param alpha is between [0,1]: 0 is for everything, 1 is for exact match
     * @return a collection of approximately matched strings
     */
    public Collection<String> search(String query, double alpha);

    // return only 1 string
    public String searchForOne(String query, double alpha);
}
