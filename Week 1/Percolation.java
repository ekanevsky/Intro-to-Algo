import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int[][] root; // keeps a list of unique identifiers that can be referenced to siteIsOpen
    private final int gridMax; // grid max size from arg n in Percolation
    private final WeightedQuickUnionUF unions;
    private int openSites; 
    private boolean[][] siteIsOpen; // tracks if site is open

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {

        if (n <= 0) throw new IllegalArgumentException();
        siteIsOpen = new boolean[n+2][n+1];
        root = new int[n+2][n+1];
        int startingRoot = 0;
        openSites = 0;
        gridMax = n;
        int gridTotal = (n+2) * (n+1);
        unions = new WeightedQuickUnionUF(gridTotal);
        for (int i = 0; i <= n+1; i++) {
            for (int j = 0; j <= n; j++) {
                siteIsOpen[i][j] = false;
                root[i][j] = startingRoot;
                if (i == 0) {
                    unions.union(root[i][j], 0); // creating virtual top and bottom sites 
                    siteIsOpen[i][j] = true;
                }
                if (i == n+1) {
                    unions.union(root[i][j], root[1][0]); // and attaching top and bottom rows to them
                    siteIsOpen[i][j] = true;
                }
                startingRoot++;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validation(row, col);
        if (isOpen(row, col)) return;
        siteIsOpen[row][col] = true;
        openSites++;
        // check if neighboring sites are open and merges their sets if they are
        // edge cases are filtered out to avoid checking out of bounds sites
        if (siteIsOpen[row-1][col]) unions.union(root[row][col], root[row-1][col]);
        if (siteIsOpen[row+1][col]) unions.union(root[row][col], root[row+1][col]);
        if (col > 1 && siteIsOpen[row][col-1]) unions.union(root[row][col], root[row][col-1]);
        if (col != gridMax && siteIsOpen[row][col+1]) unions.union(root[row][col], root[row][col+1]);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validation(row, col);
        return siteIsOpen[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validation(row, col);
        return (unions.find(root[row][col]) == unions.find(0));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return (unions.find(0) == unions.find(root[1][0]));
    }

    private void validation(int row, int col) {
        if (row < 1 || row > gridMax || col < 1 || col > gridMax) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {

        Percolation perc = new Percolation(3);

        perc.open(1,1);
        perc.open(3,1);
        perc.open(2,1);
        perc.open(3,3);
        System.out.println(perc.isFull(3, 3));
        System.out.println(perc.percolates());

    }
}