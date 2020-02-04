/* *****************************************************************************
 *  Name: Jingru
 *  Date: 2/4/2020
 *  Description: PercolationStats.java       Algorithm-Princeton: Assignment1
 *****************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double mean;
    private double stdDev;
    private double halfinterval;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        double numOfGrid = n * n;
        double[] samples = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            // untial percolated
            while (!percolation.percolates()) {
                int openSite = StdRandom.uniform(n * n) + 1;
                int loc[] = map1Dto2D(openSite, n);
                int row = loc[0];
                int col = loc[1];
                percolation.open(row, col);
            }
            samples[i] = percolation.numberOfOpenSites() / numOfGrid;
            // StdOut.printf("%f\n", samples[i]);
        }
        mean = StdStats.mean(samples);
        stdDev = StdStats.stddev(samples);
        halfinterval = 1.96 * stdDev / java.lang.Math.sqrt(trials);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdDev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - halfinterval;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + halfinterval;
    }

    // return multiple variables in same type
    private int[] map1Dto2D(int position, int n) {
        int loc[] = new int[2];

        if (position % n == 0) {
            loc[0] = position / n; //取整
            loc[1] = n;
        }
        else {
            loc[0] = position / n + 1;
            loc[1] = position % n; //取余数
        }
        return loc;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);

        int trials = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.printf("mean %s= %f\n", " ", ps.mean());
        StdOut.printf("Standard Deviation %s= %f\n", " ", ps.stddev());
        StdOut.printf("Confidence Interval %s= [%f, %f]", " ", ps.confidenceLo(),
                      ps.confidenceHi());
    }
}
