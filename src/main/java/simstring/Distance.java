package simstring;

import java.util.Collection;

/**
 * Created by totucuong-standard on 4/5/17.
 */
public interface Distance  {

    /**
     *
     * @param querySize the size of the query
     * @param alpha the matching threshold
     * @return minimum size for a candidate
     */
    public int minSize(int querySize, double alpha);

    /**
     *
     * @param querySize the size of the query
     * @param alpha the matching threshold
     * @return maximum size of for a candidate
     */
    public int maxSize(int querySize, double alpha);


    /**
     *
     * @param querySize the size of the query
     * @param candidateSize the size of the candidate
     * @param alpha the matching threshold
     * @return minimum overlap so that a candidate can satisfy the threshold
     */
    public int minOverlap(int querySize, int candidateSize, double alpha);


    public double computeSimilarity(Collection<String> ngram1, Collection<String> ngram2);
}
