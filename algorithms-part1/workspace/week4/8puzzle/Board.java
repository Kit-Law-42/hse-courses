import java.util.ArrayList;
import java.util.List;

public class Board {

    private final char[] board;
    private int n;
    private int indexRow = -1;
    private int indexColumn = -1;
    private List<Board> neighbour = new ArrayList<>();

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles[0].length;
        board = new char[n * n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[n * i + j] = (char) tiles[i][j];
                if (board[n * i + j] == 0) {
                    indexRow = i;
                    indexColumn = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        String str;
        str = n + "\n";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                str += ((int) board[n * i + j]) + " ";
            }
            str += "\n";
        }
        return str;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[n * i + j] != (char) 0)
                    if (board[n * i + j] != (char) n * i + j + 1) {
                        count++;
                    }
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0, dest, destRow, destColumn;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[n * i + j] == (char) n * i + j + 1 || board[n * i + j] == (char) 0)
                    continue;
                dest = (int) board[n * i + j];
                destRow = (dest - 1) / n;
                destColumn = (dest - 1) % n;
                sum += Math.abs((destRow - i)) + Math.abs((destColumn - j));
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (that.n == this.n) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (this.board[n * i + j] != that.board[n * i + j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        else return false;
    }

    private boolean isIndex(int row, int column) {
        return row < n && row >= 0 && column >= 0 && column < n;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        if (neighbour.isEmpty()) {
            if (isIndex(indexRow + 1, indexColumn))
                neighbour.add(exch(board, indexRow, indexColumn, indexRow + 1, indexColumn));
            if (isIndex(indexRow, indexColumn + 1))
                neighbour.add(exch(board, indexRow, indexColumn + 1, indexRow, indexColumn));
            if (isIndex(indexRow, indexColumn - 1))
                neighbour.add(exch(board, indexRow, indexColumn - 1, indexRow, indexColumn));
            if (isIndex(indexRow - 1, indexColumn))
                neighbour.add(exch(board, indexRow - 1, indexColumn, indexRow, indexColumn));
        }
        return neighbour;
    }

    private Board exch(char[] xcboard, int xcIndexRow1, int xcIndexColumn1, int xcIndexRow2,
                       int xcIndexColumn2) {
        int n1 = n;
        int[][] tempboard = new int[n1][n1];
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < n; k++) {
                tempboard[j][k] = (int) xcboard[n * j + k];
            }
        }
        int temp = tempboard[xcIndexRow1][xcIndexColumn1];
        tempboard[xcIndexRow1][xcIndexColumn1] = tempboard[xcIndexRow2][xcIndexColumn2];
        tempboard[xcIndexRow2][xcIndexColumn2] = temp;
        Board result = new Board(tempboard);
        return result;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (board[0] != (char) 0 && board[n * n - 1] != (char) 0) {
            return exch(board, 0, 0, n - 1, n - 1);
        }
        else if (board[0] == (char) 0) {
            return exch(board, 0, 1, n - 1, n - 1);
        }
        else if (board[n * n - 1] == (char) 0) return exch(board, 0, 0, n - 2, n - 1);
        return null;
    }

    public static void main(String[] args) {

    }
}
