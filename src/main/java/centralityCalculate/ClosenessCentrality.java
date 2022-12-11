package centralityCalculate;

import SSSPD.ShortestPath;
import domain.AmwGraph;
import reachablilityQuery.Sampling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClosenessCentrality {

    public static List<Integer> getTopCC(AmwGraph amwGraph, int k, int N){
        List<List<Integer>> lists = new ArrayList<>();
        for (int i = 0; i < 1; i++){
            List<AmwGraph> graphs = CentralityProcess.getSamples(amwGraph, N);

            Map<Integer, Double> centrality = CentralityProcess.sortedDescend(
                    closenessCentralityCalculate(graphs, amwGraph.getMAXVEX()));
            List<Integer> key = new ArrayList<>(centrality.keySet());
            lists.add(key);
        }
        return CentralityProcess.topK(lists, k, amwGraph.getMAXVEX());
    }


    public static Map<Integer, Double> getClosenessCentrality(AmwGraph amwGraph, int N) {
        List<AmwGraph> graphs = CentralityProcess.getSamples(amwGraph, N);
        Map<Integer, Double> centrality = closenessCentralityCalculate(graphs, amwGraph.getMAXVEX());
        return centrality;
    }

    public static Map<Integer, Double> closenessCentralityCalculate(List<AmwGraph> graphs, int maxVex) {
        Map<Integer, Double> res = new HashMap<>();
        for (int i = 0; i < maxVex; i++)
            res.put(i, 0.0);
        for (AmwGraph graph : graphs) {
//            Map<Integer, Double> scores = new HashMap<>();
            for (int i = 0; i < maxVex; i++) {
                double score = res.get(i);
                res.put(i, singleCC(graph, i)+score);
            }
//            scores = CentralityProcess.sortedDescend(scores);
//            Map<Integer, Double> rank = CentralityProcess.getRank(scores);
//            for (Integer key : rank.keySet()){
//                res.put(key, res.get(key) + rank.get(key));
//            }
        }
        for (int i = 0; i < maxVex; i++){
            double score = res.get(i) / maxVex;;
            res.put(i, score);
        }
        return res;
    }

    public static Double singleCC(AmwGraph graph, int node) {
        final int MAXVALUE = 100;
        double score = 0;
        List<List<String>> shortestPath = ShortestPath.shortestPath(graph, node);
        int distance = 0;
        for (int i = 1; i< shortestPath.size(); i ++){
            distance += shortestPath.get(i).size();
        }
        // 再加上除去k个可达节点的n-k个最大值
        distance += MAXVALUE * (graph.getMAXVEX() - (shortestPath.size()-1));

        score = (double) (graph.getMAXVEX() - 1) / distance;

        return score;

    }
}
