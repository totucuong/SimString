package simstring;

import java.util.*;

/**
 * This file implement an in-memory index for simstring algorithm. It consists of two vectors.
 *
 * 1. First vector conntains (trigrams -> string pointers) hash tables.
 * -----------------------------------------------------------------
 * |        |       | hash_table_i  | ...   |       |       |
 * -----------------------------------------------------------------
 *
 * Each hash table is an inverted list of trigram -> string pointer (sid)
 *
 * Trigrams at hash_table_i comes from string of size (i+1).
 *
 * Note that, we define the size of the string as the cardinality of its set of ngrams. For example if we use trigram
 * and our string is "vietnam" then its trigrams are {vie,iet,etn,tna,nam}. Hence its size is 5.
 *
 * 2. Second vector contains the dictionary, i.e. vocabulary.
 *    0          1         2
 * -----------------------------------------------------------------
 * |        |       | totucuong  | ...   |       |       |
 * -----------------------------------------------------------------
 * so the string "totucuong" will have the index sid = 2.
 *
 * Created by totucuong-standard on 4/6/17.
 */
public class SimStringIndex {
    private Vector<InvertedList> index;

    private ArrayList<String> vocabulary;


    private NgramGenerator ngramGenerator;


    public SimStringIndex() {
        init();
        ngramGenerator = NgramGenerator.getInstance();
    }

    private void init() {
        index = new Vector<>(100);
        vocabulary = new ArrayList<>();
    }

    public void setNgramGenerator(NgramGenerator n) {
        this.ngramGenerator = n;
    }

    public void insert(String str) {
        int sid = addToVocabulary(str);
        Collection<String> ngrams = ngramGenerator.getCharacterNgrams(str);
        addToIndex(ngrams,sid);
    }


    private int addToVocabulary(String str) {
        int sid = vocabulary.size();
        vocabulary.add(str);
        return sid;
    }

    private void addToIndex(Collection<String> ngrams, int sid) {
        int idx = ngrams.size() - 1;

        // adjust index size to store
        if (idx > index.size() - 1) {
            index.setSize(idx + 1);
        }

        // allocate if no inverted list found
        if (index.get(idx) == null) {
            index.set(idx, new InvertedList());
        }

        // index new ngrams into its place
        for (String ngram : ngrams) {
            index.get(idx).put(ngram,sid);
        }
    }

    public NgramGenerator getNgramGenerator() {
        return this.ngramGenerator;
    }

    public final Collection<String> getVocabulary() {
        return vocabulary;
    }

    public String getString(int sid) {
        return vocabulary.get(sid);
    }

    /**
     *
     * @param ngrams some ngrams
     * @param l the size of the string
     * @return an array of inverted list of each ngram. This list is also sorted according to the size of each ngram's
     * inverted list. Note that we look at only string of size l.
     */
    public ArrayList<ArrayList<Integer>> getAndSort(Collection<String> ngrams, int l) {
        // store result here
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        // get the inverted list that has strings whose size is l
        if (l > index.size()) // no string of this size in the index
            return result;
        InvertedList source = index.get(l-1);

        // collect posting list for each ngram
        if (source != null) {
            for (String ngram : ngrams) {
//                if (source.get(ngram) != null)
                    result.add(source.get(ngram));
            }
        }

        // sort wrt to the size of the list
        Collections.sort(result, new Comparator<Collection<Integer>>() {
            @Override
            public int compare(Collection<Integer> o1, Collection<Integer> o2) {
                return o1 == null
                        ? (o2 == null ? 0 : -1)
                        : (o2 == null ? 1 : o1.size() - o2.size());
            }

        });

        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Vocabulary:\t");
        vocabulary.stream().forEach(w -> builder.append(w).append(", "));
        builder.append("\n");
        builder.append("Inverted list:\n");
        for (int i = 0; i < index.size(); i++) {
            if (index.get(i) != null) {
                builder.append("Word size " + (i+1) + "\n");
                for (String key : index.get(i).keySet()) {
                    builder.append(key + " --> " + index.get(i).get(key).toString() + "\n");
                }
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public int size() {
        return index.size();
    }

    public void clear() {
        init();
    }

    public static void main(String[] args) {
        SimStringIndex index = new SimStringIndex();
        index.insert("totucuong");
        index.insert("alice");
        index.insert("hanh");
        index.insert("to");
        index.insert("blablabla");
        index.insert("totucuongblabla");
        System.out.println(index);
    }
}
