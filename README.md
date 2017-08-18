This is the Java implementation of simstring algorithm

http://www.chokkan.org/software/simstring/

The SimString algorithm is an approximate string matching one. Given a
query string it looks for similar string in a dictionary. The dictionary
size could have millions of unique words.

Supported similarity functions include:

    - Jaccard
    - Cosine
    
 One application of simstring is to be part of a data integration process.
 For example, if one wants to look for matched candidates for a table
 column name.
    