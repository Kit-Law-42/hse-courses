import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;


public class PercolationStats {

    // variables
    private static final double CONFIDENCE_95 = 1.96;
    private double thresh[];
    private double avg, sd;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException();
        }
        thresh = new double[trials];
        for (int i = 0; i < trials; i++) {
            thresh[i] = perc(n);
        }
    }

    private double perc(int n) {
        Percolation p = new Percolation(n);
        int i, j;
        int count = 0;
        while (!p.percolates()) {
            i = StdRandom.uniform(n) + 1;
            j = StdRandom.uniform(n) + 1;
            if (!p.isOpen(i, j)) {
                p.open(i, j);
                count++;
            }
        }
        return (double) count / (double) (n * n);
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresh);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresh);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(thresh.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(thresh.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        final int N = Integer.parseInt(args[0]);
        final int T = Integer.parseInt(args[1]);
        final Stopwatch clock = new Stopwatch();
        final PercolationStats ps = new PercolationStats(N, T);
        final double time = clock.elapsedTime();

        System.out.println("mean() = " + ps.mean());
        System.out.println("stddev() = " + ps.stddev());
        System.out.println(
                "95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
        System.out.println("elapsed time:\t\t\t" + time + "s");
    }

}
