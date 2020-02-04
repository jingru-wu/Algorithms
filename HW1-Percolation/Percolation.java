/* *****************************************************************************
 *  Name: Jingru
 *  Date: 2/4/2020
 *  Description: Percolation.java       Algorithm-Princeton: Assignment1
 *               version-2: add uftop to avoid backwash
 *****************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private final int number;
    private int openSites;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF uftop;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        number = n;
        openSites = 0;
        grid = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
        }
        uf = new WeightedQuickUnionUF(n * n + 2);
        uftop = new WeightedQuickUnionUF(n * n + 1);
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        isValidIndices(row, col);

        if (!grid[row - 1][col - 1])    // not open
        {
            grid[row - 1][col - 1] = true;  // set to open
            openSites++;
            int position = map2Dto1D(row, col);

            // connect with start or end component
            if (row == 1) {
                uf.union(position, 0);
                uftop.union(position, 0);
            }
            if (row == number) uf.union(position, number * number + 1);

            // connect with upper site
            if (row > 1 && grid[row - 2][col - 1]) {
                uf.union(position, position - number);
                uftop.union(position, position - number);
            }

            // lower
            if (row < number && grid[row][col - 1]) {
                uf.union(position, position + number);
                uftop.union(position, position + number);
            }
            // left
            if (col > 1 && grid[row - 1][col - 2]) {
                uf.union(position, position - 1);
                uftop.union(position, position - 1);
            }

            // right
            if (col < number && grid[row - 1][col]) {
                uf.union(position, position + 1);
                uftop.union(position, position + 1);
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        isValidIndices(row, col);
        return grid[row - 1][col - 1];
    }

    // does the system percolate?
    public boolean percolates() {
        int component1 = 0;
        int component2 = number * number + 1;
        return uf.connected(component1, component2);
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        isValidIndices(row, col);
        int pos = map2Dto1D(row, col);
        return uftop.connected(pos, 0);
    }

    private void isValidIndices(int row, int col) {
        if (row < 1 || row > number || col < 1 || col > number)
            throw new IllegalArgumentException();
    }

    private int map2Dto1D(int row, int col) {
        return (row - 1) * number + col;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(4);
        System.out.println(percolation.percolates());
        percolation.open(1, 1);
        percolation.open(2, 1);
        System.out.println(percolation.percolates());
        percolation.open(3, 1);
        percolation.open(3, 2);
        System.out.println(percolation.isFull(3, 2));
        percolation.open(4, 2);
        System.out.println(percolation.percolates());
    }
}
