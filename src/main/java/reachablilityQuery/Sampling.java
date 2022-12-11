package reachablilityQuery;

import domain.*;

import java.util.*;


public class Sampling {

    public static String reachability(AdjGraph adj, String st, int N) {
        double count = 0;
        String[] vertex = st.split(",");
        for (int i = 0; i < N; i++) {
            AdjGraph graph = samping(adj);
            if (graph.reach(vertex[0],vertex[1]) == 1)
                count++;
        }
//        System.out.println(count);
        double result = count / N;
        double MSE = result * (1-result) / N;
//        System.out.println("MSE: " + String.format("%.6f",MSE) );
        String RPr = String.format("%.6f", result);
        return RPr;

    }

    public static double getReachability(AmwGraph AmwGraph, int start, int end, int N) {
        double count = 0.0;
        for (int i = 0; i < N; i++) {
            AmwGraph graph = samping(AmwGraph);
            if (graph.reach(start, end)){
                count++;
//                System.out.println(count);
            }
                
        }
        double result = count / N;
        return result;
    }

    public static AmwGraph samping(AmwGraph amw){
        String[] vertex = new String[amw.getMAXVEX()];
        for (int i = 0; i < amw.getMAXVEX(); i ++)
            vertex[i] = amw.getVerrArr().get(i);
        AmwGraph graph = new AmwGraph(vertex);
        double[][] edges = new double[amw.getMAXVEX()][amw.getMAXVEX()];
        for (int i =  0; i < amw.getMAXVEX(); i++){
            for (int j = 0; j < amw.getMAXVEX(); j++){
                edges[i][j] = 0.0;
            }
        }
        for (int i =  0; i < amw.getMAXVEX(); i++){
            for (int j = 0; j < amw.getMAXVEX(); j++){
                if (amw.getEdges()[i][j] != 0){
                    if (amw.getEdges()[i][j] == 1)
                        edges[i][j] = 1.0;
                    else
                        if (getWeightRandom(amw.getEdges()[i][j]))
                        edges[i][j] = 1.0;
                }
            }
        }
        graph.setEdges(edges);
        return graph;
    }

    public static AdjGraph samping(AdjGraph adj) {
        // 生成一个只有顶点数组的图
        String[] vertex = new String[adj.getMAXVEX()];
        for (int i = 0; i < adj.getMAXVEX(); i++)
            vertex[i] = adj.getVerArr().get(i).getVertex();
        AdjGraph graph = new AdjGraph(vertex);
        ArrayList<String> list = new ArrayList<>();

        // 采样一个可能世界图
        for (int i = 0; i < adj.getMAXVEX(); i++) {
            AdjGraph.VertexType v = adj.getVerArr().get(i);
            AdjGraph.AdgvexType e = v.getEdg();
            while (e != null) {
                // 对e进行采样,若采到,则将e添加到新的确定图中
                if (getWeightRandom(e.getWeightNum())) {
                    String s = v.getVertex() + " " + e.getVerName() + " " + "1";
                    list.add(s);
                }
                e = e.getNext();
            }
        }
        String[][] edges = GraphCreate.getEdges(list);
        graph.addEdg(edges);
        return graph;
    }

    public static boolean getWeightRandom(double weight) {
        double random = new Random().nextDouble();
        if (( weight - random) >= 0)
            return true;
        return false;
    }

    public static void BFS(AdjGraph adj, boolean[] visited) {
        // 广度优先遍历
        Queue<AdjGraph.VertexType> queue = new LinkedList<AdjGraph.VertexType>();

        visited[0] = true;
        queue.offer(adj.getVerArr().get(0));

        AdjGraph.VertexType v;
        AdjGraph.AdgvexType e;
        while (!queue.isEmpty()) {
            v = queue.poll();

            e = v.getEdg();
            while (e != null) {
                if (!visited[e.getVerNum()]) {
                    AdjGraph.VertexType w = adj.getVerArr().get(e.getVerNum());
                    visited[e.getVerNum()] = true;
                    queue.offer(w);
                }
                e = e.getNext();
            }
        }
    }

}
