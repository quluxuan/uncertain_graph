package centralityCalculate;

import SSSPD.ShortestPath;
import domain.AdjGraph;
import domain.AmwGraph;

import java.util.*;

/**
 * 提取N个可能世界，计算每个可能世界每个节点上的可能世界，并进行排序。根据排名得到前k个中心节点。
 *
 * 采用邻接矩阵的形式便于实现边逆转。
 */
public class BetweennessCentrality {

    static int COUNT = 1;

    public static List<Integer> getTopBC(AmwGraph amwGraph, int k, int N){
        List<List<Integer>> lists = new ArrayList<>();
        for (int i = 0; i < k; i++){
            List<AmwGraph> graphs = CentralityProcess.getSamples(amwGraph, N);

            Map<Integer, Double> centrality = CentralityProcess.sortedDescend(
                    BetweennessCentralityCalculte(graphs, amwGraph.getMAXVEX()));
            List<Integer> key = new ArrayList<>(centrality.keySet());
            lists.add(key);
        }
        return CentralityProcess.topK(lists, k, amwGraph.getMAXVEX());
    }


    public static Map<Integer, Double> getCentrality(AmwGraph amwGraph, int N) {
        List<AmwGraph> graphs = CentralityProcess.getSamples(amwGraph, N);
        Map<Integer, Double> centrality = BetweennessCentralityCalculte(graphs, amwGraph.getMAXVEX());
        return centrality;
    }

    public static Map<Integer, Double> BetweennessCentralityCalculte(List<AmwGraph> graphs, int maxVex){
        Map<Integer, Double> res = new HashMap<>();
        for (int i = 0; i < maxVex; i++)
            res.put(i, 0.0);
        for (AmwGraph graph : graphs){
//            Map<Integer, Double> scores = new HashMap<>();
            for (int i = 0; i <maxVex; i++){
                double score = res.get(i);
                // 计算各个节点的BC值
                res.put(i, singleBC(graph,i) + score);
            }
            COUNT++;
            // 按照BC值进行排序。
//            scores = CentralityProcess.sortedDescend(scores);
//            // 将排序的索引值取出，计算BC排位值。
//            Map<Integer, Double> BCR = CentralityProcess.getRank(scores);
//            // 将排位值添加到结果中。
//            for (Integer key : BCR.keySet()){
//                res.put(key, res.get(key)+BCR.get(key));
//            }

        }
        for (int i = 0; i < maxVex; i++){
            double score = res.get(i);
            res.put(i, score/maxVex);
        }
        return res;
    }



    /**
     * 计算一个点的BC值
     */

    public static double singleBC(AmwGraph graph, int node){
        double scores = 0;

        // 判断节点node的出度是否为0
        if (getOutdegree(graph, node) == 0){
            System.out.println("calculate " + node +" centrality:" + 0 + " in "+ COUNT +"th");
            return 0;
        }
//        // 计算所有到node的节点 RF(node)
        AmwGraph rgraph = reverseGraph(graph);
        List<Integer> reachable = getReachable(rgraph, node);

        for (int i = 0; i < reachable.size(); i ++){
            // 生成以v为根的单源最短路径。
            List<List<Integer>> SPD = CentralityProcess.getShortestIndex(graph, ShortestPath.shortestPath(graph, reachable.get(i)));
            // 计算依赖函数
            double dependency = dependencyScores(node, SPD);
            scores += dependency;
        }
        System.out.println("calculate " + node +" centrality:" + scores + " in "+ COUNT +"th");
        return scores;
    }

    /**
     * 计算以v为起始点r的依赖函数
     */
    public static double dependencyScores(int r, List<List<Integer>> shortedtPath){
        double dependency = 0;
        int spLen = shortedtPath.size();
        int count = 0;
        for (List<Integer> list : shortedtPath){
            if (list.contains(r) && list.indexOf(r) != list.size()-1){
                count = 1;
                dependency = dependency + (double) count / spLen;
            }
        }
        return dependency;
    }

    /**
     * 将最短路径节点名转为索引值。
     */


    /**
     * 计算node在graph上的出度
     */
    public static int getOutdegree (AmwGraph graph, int node){
        int count = 0;
        for (int i = 0; i < graph.getMAXVEX(); i++){
            if (graph.getEdges()[node][i] > 0){
                count ++;
            }
        }
        return count;
    }

    /**
     * 将图的边指向进行反转
     *
     * 将矩阵转置
     */
    public static AmwGraph reverseGraph(AmwGraph graph){
        AmwGraph cgraph = graph.clone();
        double[][] edges = cgraph.getEdges();
        // 将矩阵转置
        int len = graph.getMAXVEX();
        for (int i = 0; i < len; i++){
            for (int j = i; j < len; j++){
                double temp = edges[i][j];
                cgraph.getEdges()[i][j] = edges[j][i];
                cgraph.getEdges()[j][i] = temp;
            }
        }
        return cgraph;

    }

    /**
     * 在R(G)上计算节点node的可达节点。
     */

    public static List<Integer> getReachable(AmwGraph graph, int node){
        boolean[] visited = new boolean[graph.getMAXVEX()];
        Set<Integer> reachable = new HashSet<>();
        double[][] edges = graph.getEdges();
        int len = graph.getMAXVEX();
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(node);
        visited[node] = true;
        while (!queue.isEmpty()){
            int currNode = queue.poll();
            for (int j = 0; j < len; j++){
                if (edges[currNode][j] != 0 && !visited[j]){
                    reachable.add(j);
                    queue.offer(j);
                    visited[j] = true;
                }
            }
        }
        return new ArrayList<>(reachable);
    }
}
