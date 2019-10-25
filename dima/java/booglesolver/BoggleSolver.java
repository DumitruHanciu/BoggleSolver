package dima.java.booglesolver;

/* *****************************************************************************
 *  Name: Dumitru Hanciu
 *  Date: 24.01.2019
 *  Description: dima.java.booglesolver.BoggleSolver
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashSet;

public class BoggleSolver {
    private Node root;
    private HashSet<String> words;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary)
            insert(word);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        words = new HashSet<>();
        for (int row = 0; row < board.rows(); row++)
            for (int col = 0; col < board.cols(); col++)
                getWords(board, root, "", row, col, 0, new boolean[board.rows()][board.cols()]);
        return words;
    }

    private void getWords(BoggleBoard b, Node n, String w, int r, int c, int i, boolean[][] m) {
        if (validate(b, r, c, m)) {
            w += (getLetter(b, r, c));
            Node x = get(n, w, i++);
            if (getLetter(b, r, c).equals("QU")) i++;
            if (x != null) {
                if (x.word && w.length() > 2) words.add(w);
                boolean[][] visited = clone(m, r, c);
                getWords(b, x.mid, w, r, c - 1, i, visited);
                getWords(b, x.mid, w, r, c + 1, i, visited);
                getWords(b, x.mid, w, r - 1, c, i, visited);
                getWords(b, x.mid, w, r + 1, c, i, visited);
                getWords(b, x.mid, w, r + 1, c + 1, i, visited);
                getWords(b, x.mid, w, r - 1, c - 1, i, visited);
                getWords(b, x.mid, w, r + 1, c - 1, i, visited);
                getWords(b, x.mid, w, r - 1, c + 1, i, visited);
            }
        }
    }

    private String getLetter(BoggleBoard b, int r, int c) {
        String s = "";
        char letter = b.getLetter(r, c);
        if (letter == 'Q') s += "QU";
        else s += letter;
        return s;
    }

    private boolean validate(BoggleBoard b, int r, int c, boolean[][] m) {
        return c < b.cols() && c >= 0 && r < b.rows() && r >= 0 && !m[r][c];
    }

    private boolean[][] clone(boolean[][] m, int r, int c) {
        boolean[][] newM = new boolean[m.length][m[0].length];
        for (int row = 0; row < m.length; row++)
            for (int col = 0; col < m[0].length; col++)
                newM[row][col] = m[row][col];
        newM[r][c] = true;
        return newM;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (contains(word))
            if (word.length() < 3) return 0;
            else if (word.length() < 5) return 1;
            else if (word.length() < 6) return 2;
            else if (word.length() < 7) return 3;
            else if (word.length() < 8) return 5;
            else return 11;
        else return 0;
    }

    private class Node {
        private boolean word;
        private char c;
        private Node left, mid, right;

        Node(char c) {
            this.c = c;
        }
    }

    private void insert(String key) {
        root = insert(root, key, 0);
    }

    private Node insert(Node x, String key, int d) {
        char c = key.charAt(d);
        if (x == null) x = new Node(c);
        if (c < x.c) x.left = insert(x.left, key, d);
        else if (c > x.c) x.right = insert(x.right, key, d);
        else if (d < key.length() - 1) x.mid = insert(x.mid, key, d + 1);
        else x.word = true;
        return x;
    }

    private boolean contains(String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        else return x.word;
    }

    private Node get(Node x, String key, int d) {
        char c = key.charAt(d);
        if (x == null) return null;
        else if (c < x.c)
            return get(x.left, key, d);
        else if (c > x.c)
            return get(x.right, key, d);
        else if (d < key.length() - 1)
            return get(x.mid, key, d + 1);
        else
            return x;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        // In in = new In("dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        // BoggleBoard board = new BoggleBoard(args[1]);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int count = 0;
        Stopwatch s = new Stopwatch();
        for (String word : solver.getAllValidWords(board)) {
            count++;
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        StdOut.println("Count = " + count + " time: " + s.elapsedTime());
    }
}
