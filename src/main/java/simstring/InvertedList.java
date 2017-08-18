package simstring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * This class implement an inverted list (trigram -> sids)
 * Note that a (trigram -> null) means there is no string associated with this trigram.
 * This could happen when we perform a approximate dictionary matching.
 * Created by totucuong-standard on 4/6/17.
 */
public class InvertedList {
    private HashMap<String, ArrayList<Integer>> map;

    public InvertedList() {
        this.map = new HashMap<>();
    }

    /**
     *
     * @param key ngram
     * @param value its document id
     */
    public void put(String key, int value) {
        map.putIfAbsent(key, new ArrayList<>());
        map.get(key).add(new Integer(value));
    }

    public void put(String key, Collection<Integer> sids) {
        map.putIfAbsent(key, new ArrayList<>());
        map.get(key).addAll(sids);
    }

    public ArrayList<Integer> get(String key) {
        return map.get(key);
    }

    public Set<String> keySet() {
        return map.keySet();
    }
}
