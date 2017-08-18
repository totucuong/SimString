package simstring;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by totucuong-standard on 4/5/17.
 */
public class Cosine implements Distance {
    private static Cosine ourInstance = new Cosine();

    public static Cosine getInstance() {
        return ourInstance;
    }

    private Cosine() {
    }

    @Override
    public int minSize(int querySize, double alpha) {
        return (int) Math.ceil(alpha*alpha*querySize);
    }

    @Override
    public int maxSize(int querySize, double alpha) {
        return (int) Math.floor(querySize/(alpha*alpha));
    }

    @Override
    public int minOverlap(int querySize, int candidateSize, double alpha) {
        return (int) Math.ceil(alpha * Math.sqrt(querySize * candidateSize));
    }

    @Override
    public double computeSimilarity(Collection<String> ngram1, Collection<String> ngram2) {
        Set<String> common = ngram1.stream().filter(n -> ngram2.contains(n)).collect(Collectors.toSet());
        double nominator = common.size();
        double denominator = Math.sqrt(ngram1.size() * ngram2.size());
        return nominator/denominator;
    }
}
