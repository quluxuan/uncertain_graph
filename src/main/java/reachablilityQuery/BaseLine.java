package reachablilityQuery;

import domain.AdjGraph;
import domain.AmwGraph;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BaseLine extends ReliabilityQuery {

    AmwGraph amwGraph;
    Set<Integer> centerNode;
    int N;

    public BaseLine(AmwGraph amwGraph, Set<Integer> centerNode, int N) {
        super(amwGraph,centerNode,N);
        this.amwGraph = amwGraph;
        this.centerNode = centerNode;
        this.N = N;
    }

    // 计算总的概率
    public double getReliability(int start, int end) {

        List<Node> sToCenters = getFirstCenter(start,amwGraph.getEdges());
        System.out.println(sToCenters);
        List<Node> centersTot = getEndCenter(end);
        System.out.println(centersTot);

        // 定义一个图
        AmwGraph graph = amwGraph.clone();
        double[][] edges = new double[graph.getMAXVEX()][graph.getMAXVEX()];
        for (Node sToCenter : sToCenters) {
            double p1 = sToCenter.getProbility();
            edges[start][sToCenter.getNum()] = p1;
            for (Node centerTot : centersTot) {
                if (sToCenter.getNum() != centerTot.getNum()) {
                    double p2 = getCenterRelated(sToCenter.getNum(), centerTot.getNum());
                    edges[sToCenter.getNum()][centerTot.getNum()] = p2;
                }
            }
        }
        for (Node centerTot : centersTot) {
            double p3 = centerTot.getProbility();
            edges[centerTot.getNum()][end] = p3;
        }

        graph.setEdges(edges);
        double result = Sampling.getReachability(amwGraph,start,end,N);
        return result;
    }


    // s->center 的所有中心节点
    public List<Node> getFirstCenter(int start, double[][] edges) {
        List<Node> result = new ArrayList<>();

        int len = amwGraph.getMAXVEX();
        boolean[] visited = new boolean[len];
        Queue<Node> queue = new LinkedList<>();
        int count = 0;

        Node startNode = new Node(start, 0,0);
        queue.offer(startNode);
        visited[start] = true;
        while (!queue.isEmpty()) {
            Node currNode = queue.poll();
            count++;
            for (int i = 0; i < len; i++) {
                if (edges[currNode.getNum()][i] != 0 && !visited[i]) {
                    Node node = new Node(i,currNode.getLevel()+1, 0);
                    visited[i] = true;
                    if (centerNode.contains(i)) {
                        double probability = ImprovedSampling.BSSII(amwGraph,N,count,start,i);
                        node.setProbility(probability);
                        result.add(node);
                    }
                    queue.offer(node);
                }
            }
        }

        return result;
    }

    // centers -> t 的所有节点
    public List<Node> getEndCenter(int end) {
        double[][] reverseEdges = getReverse(amwGraph.getEdges());
        return getFirstCenter(end, reverseEdges);
    }


}
