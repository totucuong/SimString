package simstring;

import edu.stanford.nlp.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class implements an ngram generator (trigram is the default)
 * Created by totucuong-standard on 4/5/17.
 */
public class NgramGenerator {
    private int ngramSize;

    private static final char mark = 0x01; // start of heading mark (SOH)

    private static NgramGenerator ourInstance = new NgramGenerator(3);

    public static NgramGenerator getInstance() {
        return ourInstance;
    }

    private NgramGenerator(int size) {
        this.ngramSize = size;
    }

    public void setSize(int size) {
        this.ngramSize = size;
    }

    Collection<String> getCharacterNgrams(String str) {
        if (str.length() < this.ngramSize)  // pad string if its length is shorter than ngram size
            str = StringUtils.padLeft(str, ngramSize , mark);
        HashMap<String, Integer> ngramStats = new HashMap<>();

        // collect ngram statistics
        for (String ngram : StringUtils.getCharacterNgrams(str, this.ngramSize, this.ngramSize)) {
            ngramStats.putIfAbsent(ngram, 0);
            ngramStats.put(ngram, ngramStats.get(ngram) + 1);
        }

        // convert them into set by appending number to repetitive ngram
        Collection<String> ngrams = new ArrayList<>();
        ngramStats.keySet().forEach(k -> {
            ngrams.add(k);
            for (int i = 1; i < ngramStats.get(k); i++)
                ngrams.add(k + i);
        });
        return ngrams;
    }

    public static void main(String[] args) {
        System.out.println(NgramGenerator.getInstance().getCharacterNgrams("totucuong"));
        System.out.println(NgramGenerator.getInstance().getCharacterNgrams("to"));
        System.out.println("Begin (end) of string mark" + mark);
    }
}
