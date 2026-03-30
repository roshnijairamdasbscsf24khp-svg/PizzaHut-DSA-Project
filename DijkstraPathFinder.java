package pizzahutproject;

import java.util.*;

public class DijkstraPathFinder {
    private double[][] adj;
    private int n;
    private List<String> cityNames;
    private List<int[]> coordinates;

    public DijkstraPathFinder() {
        this.cityNames = new ArrayList<>();
        this.coordinates = new ArrayList<>();
        this.n = 0;
    }

    public void addLocation(String name, int x, int y) {
        cityNames.add(name);
        coordinates.add(new int[]{x, y});
        n++;
    }

    public void autoGenerateGraph() {
        adj = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    adj[i][j] = 0;
                } else {
                    int[] p1 = coordinates.get(i);
                    int[] p2 = coordinates.get(j);
                    double dist = Math.sqrt(Math.pow(p2[0] - p1[0], 2) + Math.pow(p2[1] - p1[1], 2));
                    adj[i][j] = Math.round(dist * 100.0) / 100.0;
                }
            }
        }
    }

    public List<String> getCityNames() { return cityNames; }

    public double getDistanceToCity(String destinationCity) {
        int destIndex = cityNames.indexOf(destinationCity);
        if (destIndex == -1) return Double.POSITIVE_INFINITY;
        return shortestDistanceTo(destIndex);
    }

    private double shortestDistanceTo(int dest) {
        if (n == 0) return 0.0;
        double[] dist = new double[n];
        boolean[] used = new boolean[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[0] = 0; // Source is index 0

        for (int i = 0; i < n; i++) {
            int u = -1;
            for (int j = 0; j < n; j++) if (!used[j] && (u == -1 || dist[j] < dist[u])) u = j;
            if (u == -1 || Double.isInfinite(dist[u])) break;
            used[u] = true;
            for (int v = 0; v < n; v++) {
                if (adj[u][v] > 0 && dist[v] > dist[u] + adj[u][v])
                    dist[v] = dist[u] + adj[u][v];
            }
        }
        return dist[dest];
    }
}