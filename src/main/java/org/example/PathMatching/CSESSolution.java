package org.example.PathMatching;

// Fasted accepted Java solution on CSES Grid Paths
// credits: https://cses.fi/problemset/hack/1625/entry/166997/
public class CSESSolution implements PathMatcher{
    static boolean[][] used = new boolean[10][10];
    static {
        for (int i = 0; i <= 9; i += 9)
            for (int j = 0; j <= 9; j++)
                used[i][j] = true;
        for (int j = 0; j <= 9; j += 9)
            for (int i = 0; i <= 9; i++)
                used[i][j] = true;
    }
    char[] path;
    int cnt;

    static boolean only(int i, int j) {
        if (i == 8 && j == 1)
            return false;
        int k = 0;
        if (!used[i - 1][j])
            k++;
        if (!used[i + 1][j])
            k++;
        if (!used[i][j - 1])
            k++;
        if (!used[i][j + 1])
            k++;
        return k == 1;
    }
    void solve(int i, int j, int h) {
        if (h == 63 || i == 8 && j == 1) {
            if (h == 63)
                cnt++;
            return;
        }
        boolean u = !used[i - 1][j];
        boolean d = !used[i + 1][j];
        boolean l = !used[i][j - 1];
        boolean r = !used[i][j + 1];
        if (u && d && !l && !r || !u && !d && l && r)
            return;
        used[i][j] = true;
        char p = path[h++];
        if (u && only(i - 1, j)) {
            if (p == 'U' || p == '*')
                solve(i - 1, j, h);
            used[i][j] = false;
            return;
        }
        if (d && only(i + 1, j)) {
            if (p == 'D' || p == '*')
                solve(i + 1, j, h);
            used[i][j] = false;
            return;
        }
        if (l && only(i, j - 1)) {
            if (p == 'L' || p == '*')
                solve(i, j - 1, h);
            used[i][j] = false;
            return;
        }
        if (r && only(i, j + 1)) {
            if (p == 'R' || p == '*')
                solve(i, j + 1, h);
            used[i][j] = false;
            return;
        }
        if (u && (p == 'U' || p == '*'))
            solve(i - 1, j, h);
        if (d && (p == 'D' || p == '*'))
            solve(i + 1, j, h);
        if (l && (p == 'L' || p == '*'))
            solve(i, j - 1, h);
        if (r && (p == 'R' || p == '*'))
            solve(i, j + 1, h);
        used[i][j] = false;
    }

    @Override
    public int countMatches(String path) {
        cnt = 0;
        this.path = path.toCharArray();
        long t1 = System.currentTimeMillis();
        solve(1, 1, 0);
        long t2 = System.currentTimeMillis();

        System.out.println("Searching time (ms): " + (t2 - t1));
        System.out.println("Total paths: " + cnt);
        return cnt;
    }
}
