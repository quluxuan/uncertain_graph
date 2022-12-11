package reachablilityQuery;

import domain.AdjGraph;
import domain.GraphCreate;
import java.util.ArrayList;

public class Prunning {
    private AdjGraph adj;
    private int NUM;
    private int[] dfn;
    private int[] low;
    private int[] parent;
    private boolean[] vis;
    private static int count;

    public
    Prunning(AdjGraph adj) {
        this.adj = adj;
        this.NUM = adj.getMAXVEX();
        this.dfn = new int[NUM];
        this.low = new int[NUM];
        this.parent = new int[NUM];
        for (int i =0;i<NUM;i++)
            parent[i] = -1;
        this.vis = new boolean[NUM];
        this.count = 0;
    }


    public void getCut(int u) {
        // 子树数量
        int chirldren = 0;
        this.dfn[u] = low[u] = ++count;
        vis[u] = true;
        AdjGraph.AdgvexType e = adj.getVerArr().get(u).getEdg();

        // 遍历与u相邻的所有顶点
        while (e != null){
            // {u,e}为树边
            if (!vis[e.getVerNum()]){
                chirldren++;
                parent[e.getVerNum()] = u;
                getCut(e.getVerNum());
                low[u] = Math.min(low[u],low[e.getVerNum()]);

                // 如果是根节点且有两棵以上的子树则是割点
                if (parent[u] == -1 && chirldren >= 2)
                    System.out.println("Articulation point: "+ adj.getVerArr().get(u).getVertex());
                // 如果不是根节点且low[e] >= dfn[u]，则是割点
                else if (parent[u] != -1 && low[e.getVerNum()] >= dfn[u])
                    System.out.println("Articulation point: "+ adj.getVerArr().get(u).getVertex());
            }
            else if (e.getVerNum()!=parent[u])
                low[u] = Math.min(low[u],dfn[e.getVerNum()]);
            e =e.getNext();
        }
    }

    // 有向图转换为无向图
    public AdjGraph dirToUndir(String filepath) {
        String[] vertex = new String[adj.getMAXVEX()];
        for (int i = 0; i < adj.getMAXVEX(); i++)
            vertex[i] = adj.getVerArr().get(i).getVertex();
        AdjGraph undirGraph = new AdjGraph(vertex);
        ArrayList<String> list = GraphCreate.readTxtFile(filepath);
        for (int i = 0; i < adj.getMAXVEX(); i++) {
            AdjGraph.VertexType v = adj.getVerArr().get(i);
            AdjGraph.AdgvexType e = v.getEdg();
            while (e != null) {
                String s = e.getVerName() + " " + v.getVertex() + " " + e.getWeightNum();
                list.add(s);
                e = e.getNext();
            }
        }
        String[][] edges = GraphCreate.getEdges(list);
        undirGraph.addEdg(edges);
        return undirGraph;
    }

    public void DFS(int verNum, ArrayList<Integer> clique) {
        if (verNum > adj.getMAXVEX() || verNum < 0) throw new IndexOutOfBoundsException("角标越界");
        adj.getVisited()[verNum] = true;
        clique.add(verNum);
        AdjGraph.AdgvexType temp = adj.getVerArr().get(verNum).getEdg();
        while (temp != null) {
            if (!adj.getVisited()[temp.getVerNum()])
                DFS(temp.getVerNum(), clique);
            temp = temp.getNext();
        }

    }
}
