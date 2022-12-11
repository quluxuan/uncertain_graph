package centralityCalculate;

import domain.AdjGraph;
import domain.AmwGraph;
import reachablilityQuery.Sampling;

import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.util.*;

public class CentralityProcess {

    // 将计算的中心性进行排序
    public static <K, V extends Comparable<? super V>> Map<K,V> sortedDescend(Map<K,V> map){
        List<Map.Entry<K,V>> list = new ArrayList<Map.Entry<K,V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });
        Map<K,V> returnMap = new LinkedHashMap<>();
        for (Map.Entry<K,V> entry : list){
            returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }

    // 出现次数最多的前k个节点
    public static List<Integer> topK(List<List<Integer>> lists, int k, int maxVex){
        Map<Integer, Integer> map = countTimes(lists, maxVex);
        // 按出现的前五次次数排序
        map = sortedDescend(map);
        // 取出前五个key值
        List<Integer> keyList = new ArrayList<>(map.keySet());
        List<Integer> topKNode = new ArrayList<>();
        for (int i = 0; i < k; i++)
            topKNode.add(keyList.get(i));

        return topKNode;
    }

    public static Map<Integer, Integer> countTimes(List<List<Integer>> lists, int maxVex) {
        Map<Integer, Integer> map = new HashMap<>();
        // 初始化所有节点出现的次数
        for (int i = 0; i < maxVex; i++)
            map.put(i, 0);
        for (int i = 0; i < lists.size(); i++){
            List<Integer> list = lists.get(i);
            for (int j = 0; j < list.size(); j++) {
                int index = list.get(j);
                int value = map.get(index);
                map.put(index, value + 1);
            }
        }
        return map;
    }

    public static List<List<Integer>> getShortestIndex(AmwGraph graph, List<List<String>> shortestPath) {
        List<List<Integer>> lists = new ArrayList<>();
        for (List<String> spath : shortestPath){
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < spath.size(); i++){
                list.add(graph.getIndex(spath.get(i)));
            }
            lists.add(list);
        }
        return lists;
    }

    public static List<AmwGraph> getSamples(AmwGraph amwGraph, int N){
        List<AmwGraph> graphs = new ArrayList<>();
        for (int j = 0; j < N; j++){
            AmwGraph graph = Sampling.samping(amwGraph);
            graphs.add(graph);
        }
        return graphs;
    }

    public static List<AdjGraph> getSamples(AdjGraph adjGraph, int N){
        List<AdjGraph> graphs = new ArrayList<>();
        for (int j = 0; j < N; j++){
            AdjGraph graph = Sampling.samping(adjGraph);
            graphs.add(graph);
        }
        return graphs;
    }

    public static Map<Integer, Double> getRank(Map<Integer, Double> scores){
        Map<Integer, Double> rank = new HashMap<>();
        List<Integer> key = new ArrayList<>(scores.keySet());
        int len = key.size();
        for (int i = 0; i < len; i++){
            double score = (double) (len - i) / len;
            rank.put(key.get(i), score);
        }
        return rank;
    }

    public static List<Integer> moreThan(Map<Integer, Double> scores) {
        double sum = 0;
        for (int i : scores.keySet()) {
            sum += scores.get(i);
        }
        double mean = sum / scores.size();

        List<Integer> res = new ArrayList<>();
        for (int i : scores.keySet()) {
            if (scores.get(i) > mean)
               res.add(i);
        }
        return res;
    }

    public static List<Integer> moreThan(Map<Integer,Double> scores, double threshold){
        List<Integer> res = new ArrayList<>();
        for (int i : scores.keySet()) {
            if (scores.get(i) > threshold)
                res.add(i);
        }
        return res;
    }

    public static Set<Integer> getSame(List<Integer> degree, List<Integer> closeness, List<Integer> betweenness) {
        Set<Integer> temp1 = new HashSet<>(degree);
        Set<Integer> temp2 = new HashSet<>();
        Set<Integer> same = new HashSet<>();
        for (int i : closeness) {
            if (temp1.contains(i))
                temp2.add(i);
        }
        for (int i : betweenness) {
            if (temp2.contains(i))
                same.add(i);
        }
        return same;
    }

    public static List<Integer> getCenterNode(AdjGraph adjGraph, AmwGraph amwGraph, int N) {
        
        List<Integer> betweeness = CentralityProcess.moreThan(BetweennessCentrality.getCentrality(amwGraph, N));
//        System.out.println(betweeness.size());
        
        List<Integer> degree = CentralityProcess.moreThan(DegreeCentrality.getDegreeCentrality(adjGraph, N) );
//        System.out.println(degree.size());

        List<Integer> closeness = CentralityProcess.moreThan(ClosenessCentrality.getClosenessCentrality(amwGraph, N));
//        System.out.println(closeness.size());

        Set<Integer> centerNode = getSame(betweeness, degree, closeness);
        System.out.println(centerNode.size());
        
        return new ArrayList(centerNode);
    }

    public static List<Integer> getCenterList(AmwGraph amwGraph, List<Integer> centerNode) {
        List<Integer> centerList = new ArrayList<>();
        centerList.add(centerNode.get(0));
        for (int index = 1; index < centerNode.size(); index++) {
            int node = centerNode.get(index);
            if (!isReachable(amwGraph, node, centerList)){
                centerList.add(node);
                System.out.println(centerList);
            }
                
        }

        return centerList;
    }
    public static boolean isReachable(AmwGraph amwGraph, int node, List<Integer> centerList) {
        for (int i : centerList) {
            if (amwGraph.reach(node, i) && amwGraph.reach(i, node))
                return true;
        }
        return false;
    }

    // 取前10个大于平均值的节点
//    public static List<Integer> getTopk (AdjGraph adjGraph, AmwGraph amwGraph, int N, int k) {
//        List<Integer> res = new ArrayList<>();
//        Map<Integer, Double> degreeScores = DegreeCentrality.getDegreeCentrality(adjGraph, N);
//        Map<Integer, Double> closenessScores = ClosenessCentrality.getClosenessCentrality(amwGraph, N);
//        Map<Integer, Double> betweennessScores = BetweennessCentrality.getCentrality(amwGraph, N);
//
//        Map<Integer, Integer> degreeRank = rank(degreeScores, adjGraph.getMAXVEX());
//        Map<Integer, Integer> closenessRank = rank(closenessScores, adjGraph.getMAXVEX());
//        Map<Integer, Integer> betweennessRank = rank(betweennessScores, adjGraph.getMAXVEX());
//
//        Set<Integer> nodes = getSame(moreThan(degreeScores),
//                moreThan(closenessScores),
//                moreThan(betweennessScores));
//
//        Map<Integer, Double> scores = new HashMap<>();
//        for (int i : nodes) {
//            double totalScores = degreeRank.get(i) + closenessRank.get(i) + betweennessRank.get(i);
//            scores.put(i, totalScores);
//        }
////        System.out.println(scores);
//        scores = sortedDescend(scores);
//
//        List<Integer> keyList = new ArrayList<>(scores.keySet());
//        for (int i = 0; i < k; i++)
//            res.add(keyList.get(i));
//
//        return res;
//    }
//
//    public static Map<Integer, Integer> rank(Map<Integer, Double> scores, int num) {
//        Map<Integer, Integer> ranks = new HashMap<>();
//        scores = sortedDescend(scores);
//        List<Integer> keyList = new ArrayList<>(scores.keySet());
//        for (int i = 0; i < num; i++){
//            ranks.put(keyList.get(i), num-i);
//        }
//        return ranks;
//    }
}
