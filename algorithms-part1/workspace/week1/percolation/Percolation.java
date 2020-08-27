import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // constants
    private static final int CLOSED = 0;
    private static final int OPEN = 1;
    // variables
    private final int size;
    private int grid[][];
    private final WeightedQuickUnionUF perc;
    private final WeightedQuickUnionUF full;
    private int numberOfOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        perc = new WeightedQuickUnionUF(n * n + 2); // index 0 state for connected to top row
        full = new WeightedQuickUnionUF(n * n + 1); // index 0 state for connected to top row

        grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = CLOSED;
            }
        }

        size = n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > size || row < 1 || col > size || col < 1) {
            throw new IllegalArgumentException();
        }

        if (isOpen(row, col)) {
        }
        else {
            grid[(row - 1)][(col - 1)] = OPEN;

            // join left
            if (col != 1)
                con(row, col, row, (col - 1));

            // join right
            if (col != size)
                con(row, col, row, (col + 1));

            // join above
            if (row != 1) {
                con(row, col, (row - 1), col);
            }
            else {
                perc.union(g2p(row, col), 0);
                full.union(g2p(row, col), 0);
            }

            // join below
            if (row != size)
                con(row, col, (row + 1), col);
            else
                perc.union(g2p(row, col), (size * size + 1));

            // increment no. of open sites
            numberOfOpenSites++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > size || row < 1 || col > size || col < 1) {
            throw new IllegalArgumentException();
        }

        if (grid[(row - 1)][(col - 1)] == OPEN)
            return true;
        else
            return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > size || row < 1 || col > size || col < 1) {
            throw new IllegalArgumentException();
        }

        // check if cell is opened and connected to top row.
        return (grid[(row - 1)][(col - 1)] == OPEN) && (full.find(0) == full.find(g2p(row, col)));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return (perc.find(0) == perc.find(size * size + 1));
    }

    private int g2p(int row, int col) {
        return (row - 1) * size + (col - 1) + 1;
    }

    private void con(int pi, int pj, int qi, int qj) {
        // ensure both sites are open
        if (isOpen(pi, pj) && isOpen(qi, qj)) {
            perc.union(g2p(pi, pj), g2p(qi, qj));
            full.union(g2p(pi, pj), g2p(qi, qj));
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(4);
        System.out.println(percolation.percolates());
        percolation.open(1, 1);
        percolation.open(2, 1);
        System.out.println(percolation.percolates());
        percolation.open(3, 1);
        percolation.open(3, 2);
        System.out.println(percolation.percolates());
        System.out.println(percolation.isFull(3, 2));
        System.out.println(percolation.isFull(3, 3));
        percolation.open(4, 2);
        System.out.println(percolation.percolates());
    } // test client (optional)
}
