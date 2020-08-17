import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private double[] openSites;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        openSites = new double[trials];
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        for (int i = 0; i <= trials-1; i++) {
            Percolation testRun = new Percolation(n);
            //test if percolates is true then opens a site if it's not
            while (!testRun.percolates()) {
                //generate random row/col locations and attempts to open them
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);
                testRun.open(row, col);
            }
            openSites[i] = (double)testRun.numberOfOpenSites()/(n*n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(openSites);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(openSites);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(int trials) {
        return mean() - 1.96*(stddev() / Math.sqrt(trials));

    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(int trials) {
        return mean() + 1.96*(stddev() / Math.sqrt(trials));
    }

   // test client
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        int tries = Integer.parseInt(args[1]);
        PercolationStats perc = new PercolationStats(size, tries);
        System.out.println("mean                    = " + perc.mean());
        System.out.println("stddev                  = " + perc.stddev());
        System.out.println("95% confidence interval = [" + perc.confidenceLo(tries) + ", " + perc.confidenceHi(tries) + "]");
    }
}