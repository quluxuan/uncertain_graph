package reachablilityQuery;

public class Node {
    private int num;
    private int level;
    private double probility;
    Node(int num, int level, double probility) {
        this.num = num;
        this.level = level;
        this.probility = probility;
    }

    public double getProbility() {
        return probility;
    }

    public void setProbility(double probility) {
        this.probility = probility;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Node{" +
                "num=" + num +
                ", probility=" + probility +
                '}';
    }
}
