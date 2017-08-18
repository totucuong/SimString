package simstring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * SimString is a class that implements a simple and fast algorithm for approximate dictionary matching. This algorithm
 * was published in "Simple and Efficient Algorithm for Approximate Dictionary Matching", Naoki Okazaki, Junichi Tusujii
 * , Coling 2010.
 * Created by totucuong-standard on 4/5/17.
 *
 */
public class SimString implements StringMatcher {

    private SimStringIndex index;

    private final static Logger LOG = Logger.getLogger(SimString.class.getName());

    public SimString() {
        this.index = new SimStringIndex();
    }

    /**
     * Retrieve a set of string that is similar to the query string.
     * @param query a query string
     * @param distance a type of computeSimilarity (e.g., Cosine).
     * @param alpha similarity threshold
     * @return set of approximately matched strings
     */
    public Collection<String> retrieve(String query, Distance distance, double alpha) {
        Collection<Integer> results = new ArrayList<>();

        // get query features (ngrams)
        Collection<String> queryFeatures = index.getNgramGenerator().getCharacterNgrams(query);


        int querySize = queryFeatures.size();
        int minCandidateSize = Math.max(distance.minSize(querySize,alpha), 1);
        int maxCandidateSize = Math.min(distance.maxSize(querySize, alpha), index.size());


        for (int l = minCandidateSize; l <= maxCandidateSize; l++) {
            int minOverlap = Math.max(distance.minOverlap(querySize,l, alpha), 1);
//            LOG.log(Level.INFO, "Candidate size: " +l);
            Collection<Integer> res = overlapJoin(queryFeatures, minOverlap, l);
            if (res != null)
                results.addAll(res);
        }

        // convert sid to original string
        return results.stream().map(sid -> index.getString(sid)).collect(Collectors.toList());
    }

    private boolean isInvertedListEmpty(ArrayList<ArrayList<Integer>> post) {
        boolean empty = true;
        for (int i = 0; i < post.size(); i++)
            if (post.get(i) != null) {
                empty = false;
                break;
            }
        return empty;
    }

    private Collection<Integer> overlapJoin(Collection<String> ngrams, int minOverlap, int l) {

        ArrayList<ArrayList<Integer>> post = index.getAndSort(ngrams, l);
        if (isInvertedListEmpty(post)) // no candidate
                return null;

        // sid -> count (its overlap with the query string)
        HashMap<Integer, Integer> countMap = new HashMap<>();

        // compared with the first (|X| - minOverlap + 1) features (ngrams)
        int signatureSize = ngrams.size() - minOverlap + 1;
        int k = 0;
        for (; k < signatureSize; k++) {
            if (post.get(k) != null)
                for (Integer sid : post.get(k)) {
                    // update count of a candidate string
                    countMap.putIfAbsent(sid, 0);
                    countMap.put(sid, countMap.get(sid) + 1);
                }
        }

        // check each candidate sid in countMap to see if they also have features in the remaining feature of |X|
        Collection<Integer> results = new ArrayList<>();
        if (k == ngrams.size()) {
            for (Integer sid : countMap.keySet()) {
                if (countMap.get(sid) >= minOverlap)
                    results.add(sid);
            }
        }

        Collection<Integer> pruneSet = new ArrayList<>();
        for (; k < ngrams.size(); k++) {
            for (Integer sid : countMap.keySet()) {
                if (Collections.binarySearch(post.get(k),sid) >= 0) { // found
                    countMap.put(sid, countMap.get(sid) + 1);
                }

                if (minOverlap <= countMap.get(sid)) {
                    results.add(sid);
                    pruneSet.add(sid);
                } else if (countMap.get(sid) + (ngrams.size() - k - 1) < minOverlap)
                    pruneSet.add(sid);
            }

            // pruning
            for (Integer sid : pruneSet) {
                countMap.remove(sid);
            }
        }

        return results;

    }

    public static void main(String[] args) {

//
    }

    @Override
    public void addAll(Collection<String> strings) {
        strings.stream().forEach(s -> this.index.insert(s));
    }

    @Override
    public Collection<String> search(String query, double alpha) {
        return retrieve(query, Cosine.getInstance(), alpha);
    }

    @Override
    public String searchForOne(String query, double alpha) {
        Collection<String> candidates = search(query, alpha);
        if (candidates.iterator().hasNext())
                return candidates.iterator().next();
        return null;
    }
}
