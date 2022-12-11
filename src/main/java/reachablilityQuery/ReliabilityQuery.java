package reachablilityQuery;

import domain.AdjGraph;
import domain.AmwGraph;
import domain.GraphCreate;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

public class ReliabilityQuery {
    AmwGraph amwGraph;
    Set<Integer> centerNodes;
    int N;
    public ReliabilityQuery(AmwGraph amwGraph, Set<Integer> centerNodes, int N){
        this.amwGraph = amwGraph;
        this.centerNodes = centerNodes;
        this.N = N;
    }
    // 计算总概率
//    public double getReliability(int start, int end, double p, int level) {
//        BigDecimal result = new BigDecimal(0);
//        List<Set<Integer>> subGraphs = partitionSubGraph();
//        Map<Integer, Node> sToCenters = getFirstCenter(start,amwGraph.getEdges(), subGraphs,p,level);
//        Map<Integer, Node> centersTot = getEndCenter(end,subGraphs,p,level);
//        int size = subGraphs.size();
//        for (int i = 0; i < size; i++) {
//            Node center1 = sToCenters.get(i);
//            System.out.println("Node1 = "+center1);
//            Node center2 = centersTot.get(i);
//            System.out.println("Node2 = "+center2);
//            BigDecimal p1 = new BigDecimal(center1.getProbility()+"");
//            BigDecimal p2 = new BigDecimal(center2.getProbility()+"");
//            BigDecimal p3 = center1.getNum() == center2.getNum() ? new BigDecimal(1) :
//                    new BigDecimal(getCenterRelated(center1.getNum(), center2.getNum())+"");
//            System.out.println("中心节点间的概率：" + p3);
//            p1 = p1.multiply(p2).multiply(p3);
//            System.out.println(i + " 路径上的结果："+p1);
//            result = result.add(p1);
//        }
//
//        return result.doubleValue();
//    }

    public double getReliability(int start, int end, double p, int level) {
        List<Set<Integer>> subGraphs = partitionSubGraph();
        Map<Integer, Node> sToCenters = getFirstCenter(start,amwGraph.getEdges(), subGraphs,p,level);
        Map<Integer, Node> centersTot = getEndCenter(end,subGraphs,p,level);
        int size = subGraphs.size();
        AmwGraph graph = amwGraph.clone();
        double[][] edges = new double[graph.getMAXVEX()][graph.getMAXVEX()];
        for (int i = 0; i < size; i++) {
            Node center1 = sToCenters.get(i);
            System.out.println("Node1 = "+center1);
            Node center2 = centersTot.get(i);
            System.out.println("Node2 = "+center2);
            double p1 = center1.getProbility();
            double p2 = center2.getProbility();
            double p3 = center1.getNum() == center2.getNum() ? 0 :
                    getCenterRelated(center1.getNum(), center2.getNum());
            edges[start][center1.getNum()] = p1;
            edges[center1.getNum()][center2.getNum()] = p2;
            edges[center2.getNum()][end] = p3;

        }
        graph.setEdges(edges);
        double result = ImprovedSampling.BSSII(amwGraph,N,10,start,end);

        return result;
    }

    // s -> C1
    public Map<Integer, Node> getFirstCenter(int start, double[][] edges, List<Set<Integer>>
            subGraphs, double p, int level) {
        Map<Integer,Node> res = new HashMap<>();
        for (int i = 0; i < subGraphs.size(); i++) {
            Node node = new Node(0,0,0);
            res.put(i,node);
        }
        int len = amwGraph.getMAXVEX();
        boolean[] visited = new boolean[len];
        Queue<Node> queue = new LinkedList<>();
        Node startNode = new Node(start, 0,0);
        queue.offer(startNode);
        visited[start] = true;
        int count = 0;
        while (!queue.isEmpty()) {
            Node currNode = queue.poll();
            count++;
            for (int i = 0; i < len; i ++) {
                if (edges[currNode.getNum()][i] != 0 && !visited[i]) {
                    Node node = new Node(i,currNode.getLevel()+1, 0);
                    if (node.getLevel() > level)
                        continue;
                    visited[i] = true;
                    if (centerNodes.contains(i)){
                        double probability = ImprovedSampling.BSSII(amwGraph,N,count,start,i);
                        node.setProbility(probability);
                        if (probability > p) {
                            for (int m = 0; m < subGraphs.size(); m ++) {
                                if (subGraphs.get(m).contains(i)) {
                                    if (probability > res.get(m).getProbility()) {
                                        res.put(m,node);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    queue.offer(node);
                }
            }
        }
        return res;
    }



//     c2 -> t
    public Map<Integer, Node> getEndCenter(int end, List<Set<Integer>> subGraphs, double p, int level) {
        double[][] reverseEdges = getReverse(amwGraph.getEdges());
        return getFirstCenter(end, reverseEdges, subGraphs, p, level);
    }

    public double[][] getReverse(double[][] edges) {
        int len = amwGraph.getMAXVEX();
        double[][] reverseEdges = new double[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                reverseEdges[i][j] = edges[j][i];
            }
        }
        return reverseEdges;
    }

    // c1 -> c2
    public double getCenterRelated(int center1, int center2) {
        List<String> text = GraphCreate.readTxtFile("G:\\workspace\\uncertain_graph\\src\\main\\java\\data\\output\\testoffline.txt");
        String s = amwGraph.getName(center1) + " " + amwGraph.getName(center2);
        double result = 0;
        for (String str : text) {
            if (str.contains(s)) {
                String[] arr = str.split(" ");
                int count = getOne(arr[2]);
                result = (double) count/arr[2].length();
            }
        }
        return result;
    }

    public int getOne(String s) {
        int count = 0;
        char[] arr = s.toCharArray();
        for (char c : arr) {
            if (c == '1')
                count++;
        }
        return count;
    }

    public List<Set<Integer>> partitionSubGraph() {
        List<Integer> centers = new ArrayList<>(centerNodes);
        List<Set<Integer>> res = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        set.add(centers.get(0));
        res.add(set);
        for (int i = 1; i < centers.size(); i++) {
            int flag = 0;
            int node = centers.get(i);
            for (Set subGraph : res) {
                if (isCountains(subGraph,node)){
                    flag = 1;
                    subGraph.add(node);
                    break;
                }
            }
            if (flag == 0) {
                Set<Integer> temp = new HashSet<>();
                temp.add(node);
                res.add(temp);
            }
        }
        return res;
    }

    private boolean isCountains(Set<Integer> subGraph, int node) {
        for (int centerNode : subGraph) {
            if (amwGraph.reach(node,centerNode) || amwGraph.reach(centerNode,node))
                return true;
        }
        return false;
    }
}
