import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] siteIsOpen; //tracks if site is open
    private int[][] root; //keeps a list of unique identifiers that can be referenced to siteIsOpen
    private int openSites; 
    private int gridMax = 0; //grid max size from arg n in Percolation
    WeightedQuickUnionUF unions;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {

        if (n <= 0) throw new IllegalArgumentException();
        siteIsOpen = new boolean[n+1][n+1];
        root = new int[n+1][n+1];
        int startingRoot = 0;
        openSites = 0;
        gridMax = n;
        int gridTotal = (n+1) * (n+1);
        unions = new WeightedQuickUnionUF(gridTotal);
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                siteIsOpen[i][j] = false;
                root[i][j] = startingRoot;
                if (i == 1) unions.union(root[i][j], 0); //creating virtual top and bottom sites 
                if (i == n) unions.union(root[i][j], 1); //and attaching top and bottom rows to them
                startingRoot++;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > gridMax || col < 1 || col > gridMax) throw new IllegalArgumentException();
        siteIsOpen[row][col] = true;
        openSites++;
        //check if neighboring sites are open and merges their sets if they are
        //edge cases are filtered out to avoid checking out of bounds sites
        if (row > 1) {
            if (siteIsOpen[row-1][col])
                unions.union(root[row][col], root[row-1][col]);
        }
        if (row != gridMax) {
            if (siteIsOpen[row+1][col])
               unions.union(root[row][col], root[row+1][col]);
        }
        if (col > 1) {
            if (siteIsOpen[row][col-1])
              unions.union(root[row][col], root[row][col-1]);
        }
        if (col != gridMax) {
            if (siteIsOpen[row][col+1])
                unions.union(root[row][col], root[row][col+1]);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > gridMax || col < 1 || col > gridMax) throw new IllegalArgumentException();
        return siteIsOpen[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > gridMax || col < 1 || col > gridMax) throw new IllegalArgumentException();
        if (!siteIsOpen[row][col]) {
            return true;
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (unions.find(0) == unions.find(1)) {
            return true;
        }
        return false;
    }
}