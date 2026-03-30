package pizzahutproject;

public class Pizza {
    public String name;
    public int cost_L, cost_M, cost_S;
    public int time_reqd;

    public Pizza(String n, int cl, int cm, int cs, int t) {
        name = n; cost_L = cl; cost_M = cm; cost_S = cs; time_reqd = t;
    }

    public String detailedString() {
        return name + " | Small: " + cost_S + " Medium: " + cost_M + " Large: " + cost_L + " | BakeTime: " + time_reqd + "m";
    }
}