# Boggle Solver
Boggle solver for grid sizes of 3x3 up to a 6x6. To determine whether a letter combination is a word, the combinations were crossed referenced against the [10, 000 most popular words](https://github.com/first20hours/google-10000-english) determined by Google's Trillion Word Corpus.

**Speeds**
*~1s margin of error*
| Grid | 3   | 4    | 5    | 6    |
|------|-----|------|------|------|
| 3    | 1s  | 6s   | 15s  | 28s  |
| 4    | 6s  | 25s  | 67s  | 114s |
| 5    | 15s | 67s  | 161s | 258s |
| 6    | 28s | 114s | 258s | 414s |
