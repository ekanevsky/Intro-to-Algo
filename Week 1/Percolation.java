import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

    private boolean[][] siteIsOpen; //tracks if site is open
    private int[][] root; //keeps a list of unique identifiers that can be referenced to siteIsOpen
    private int[] topRow; //top row of site identifiers
    private int[] bottomRow; //bottom row of site identifiers
    private int openSites; 
    private int gridMax = 0; //grid max size from arg n in Percolation
    WeightedQuickUnionUF unions;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {

        if (n <= 0) throw new IllegalArgumentException();
        siteIsOpen = new boolean[n][n];
        root = new int[n][n];
        topRow = new int[n];
        bottomRow = new int[n];
        int startingRoot = 0;
        gridMax = n-1;
        for (int i = 0; i <= n-1; i++) {
            for (int j = 0; j <=n-1; j++) {
                siteIsOpen[i][j] = false;
                root[i][j] = startingRoot;
                if (i == 0) {
                    topRow[j] = startingRoot;
                }
                if (i == n-1) {
                    bottomRow[j] = startingRoot;
                }
                startingRoot++;
            }
        }
        unions = new WeightedQuickUnionUF(startingRoot);
        openSites = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || row > gridMax || col < 0 || col > gridMax) throw new IllegalArgumentException();

        siteIsOpen[row][col] = true;
        openSites++;
        //check if neighboring sites are open and merges their sets if they are
        //edge cases are filtered out so as not to check out of bounds sites
        if (row != 0) {
            if (siteIsOpen[row-1][col])
                unions.union(root[row][col], root[row-1][col]);
        }

        if (row != gridMax) {
            if (siteIsOpen[row+1][col])
               unions.union(root[row][col], root[row+1][col]);
        }

        if (col != 0) {
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
        if (row < 0 || row > gridMax || col < 0 || col > gridMax) throw new IllegalArgumentException();
        return siteIsOpen[row][col];

    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row > gridMax || col < 0 || col > gridMax) throw new IllegalArgumentException();

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
        //check each of the top and bottom row items until a match is found indicating percolation
        for (int i = 0; i <= topRow.length-1; i++) {
            for (int j = 0; j <= bottomRow.length-1; j++) {
                if (unions.find(topRow[i]) == unions.find(bottomRow[j])) {
                    return true;
                }
            }
        }
        return false;
    }
}