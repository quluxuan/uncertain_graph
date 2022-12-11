package reachablilityQuery;

import domain.AmwGraph;
import indexTree.PathCount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class ImprovedSampling {


    public BigDecimal RSSII(AmwGraph amwGraph, int N, int threshold, int remainder,int r, int start, int end){
        BigDecimal estimater = new BigDecimal(0);
        if (N < threshold || remainder < r) {
            for (int j = 0; j < N; j++) {
                AmwGraph sample = Sampling.samping(amwGraph);
                int query = sample.reach(start, end)?1:0;
                estimater = estimater.add(new BigDecimal(query));
            }
            return estimater.divide(new BigDecimal(N),8,RoundingMode.HALF_UP);
        }
        else {
            int[][] selectedEdges = edge_selection(amwGraph, r, start);
            for (int i = 0; i <= r; i++) {
                AmwGraph simpleGraph = getSimpleGraph(amwGraph,i,selectedEdges);
                BigDecimal simpleProbability = calculatePi(i, selectedEdges, amwGraph);
                BigDecimal b1 = new BigDecimal(N);
                int Ni = b1.multiply(simpleProbability).intValue();
                if (Ni == 0){
                    Ni = 1;
                }
                BigDecimal ui = RSSII(simpleGraph, Ni, threshold,remainder-r, r,start,end);
                estimater = estimater.add(simpleProbability.multiply(ui));
            }

        }
        return estimater;
    }

    public static double BSSII(AmwGraph amwGraph, int N, int r, int start, int end) {
        BigDecimal estimator = new BigDecimal(0);
        int[][] selectedEdges = edge_selection(amwGraph, r, start);
        for (int i = 0; i <= r; i++) {
            AmwGraph simpleGraph = getSimpleGraph(amwGraph,i,selectedEdges);
            BigDecimal pi = calculatePi(i,selectedEdges,amwGraph);
            BigDecimal b1 = new BigDecimal(N);
            int Ni = b1.multiply(pi).intValue();
            if (Ni == 0)
                Ni = 1;
            double t = 0;
            for (int j = 0; j < Ni; j ++) {
                // 在simpleGraph上采样可能世界
                AmwGraph sample = Sampling.samping(simpleGraph);
                // 在sample上计算是否可达
                int query = sample.reach(start,end)?1:0;
                t += query;
            }
            BigDecimal temp = new BigDecimal(t).divide(new BigDecimal(Ni), 8, RoundingMode.HALF_UP);
            estimator = estimator.add(pi.multiply(temp));

        }
        return estimator.doubleValue();
    }

    /**
     *
     * @param amwGraph:概率图
     * @param N：采样数量
     * @param threshold：采样阈值
     * @param remainder：剩余边
     * @param r：选取边数
     * @param start：起始节点
     * @param end：终止节点
     * @return RSSI估值
     */
    public BigDecimal RSSI(AmwGraph amwGraph, int N, int threshold, int remainder,int r, int start, int end) {
        BigDecimal estimater = new BigDecimal(0);
        if (N < threshold || remainder < r) {
            for (int j = 0; j < N; j++) {
                AmwGraph sample = Sampling.samping(amwGraph);
                int query = sample.reach(start, end)?1:0;
                estimater = estimater.add(new BigDecimal(query));
            }
            return estimater.divide(new BigDecimal(N),8,RoundingMode.HALF_UP);
        }
        else {
            int[][] selectedEdges = edge_selection(amwGraph, r, start);
            double times = Math.pow(2,r);
            for (int i = 0; i < times; i++) {
                String statusVector = getStauts(i, r);
                AmwGraph simpleGraph = getSimpleGraph(amwGraph,statusVector,selectedEdges);
                BigDecimal simpleProbability = calculatePi(statusVector, selectedEdges, amwGraph);
                BigDecimal b1 = new BigDecimal(N);
                int Ni = b1.multiply(simpleProbability).intValue();
                if (Ni == 0){
                    Ni = 1;
                }
                BigDecimal ui = RSSI(simpleGraph, Ni, threshold,remainder-r, r,start,end);
                estimater = estimater.add(simpleProbability.multiply(ui));
            }

        }
        return estimater;
    }

    /**
     *
     * @param amwGraph:概率图
     * @param N：采样数
     * @param r：选取边个数
     * @param start：起始节点
     * @param end：终止节点
     * @return BSSI 估值
     */
    public double BSSI(AmwGraph amwGraph, int N, int r, int start, int end){
        BigDecimal estimator = new BigDecimal(0);
        int[][] selectedEdges = edge_selection(amwGraph, r, start);
        System.out.println("select r edges : " + selectedEdges);
        double times = Math.pow(2,r);
        for (int i = 0; i < times; i++){
            // 得到每层i的用0/1表示状态向量
            String statusVector = getStauts(i, r);
            // 根据statusVector生成简单图
            AmwGraph simpleGraph = getSimpleGraph(amwGraph, statusVector, selectedEdges);
            BigDecimal simpleProbability = calculatePi(statusVector, selectedEdges, amwGraph);
            BigDecimal b1 = new BigDecimal(N);
            int Ni = b1.multiply(simpleProbability).intValue();
            if (Ni == 0)
                Ni = 1;
            double t = 0;
            for (int j = 0; j < Ni; j ++) {
                // 在simpleGraph上采样可能世界
                AmwGraph sample = Sampling.samping(simpleGraph);
                // 在sample上计算是否可达
                int query = sample.reach(start,end)?1:0;
                t += query;
            }
            BigDecimal temp = new BigDecimal(t).divide(new BigDecimal(Ni), 8, RoundingMode.HALF_UP);
            estimator = estimator.add(simpleProbability.multiply(temp));

        }

        return estimator.doubleValue();
    }

    public BigDecimal calculatePi(String statusVector, int[][] selectedEdges, AmwGraph amwGraph) {
        BigDecimal pi = new BigDecimal(1);
        char[] status = statusVector.toCharArray();
        double[][] edges = amwGraph.getEdges();
        for (int i = 0; i < selectedEdges.length; i++) {
            int start = selectedEdges[i][0];
            int end = selectedEdges[i][1];
            if (status[i] == '1'){
                BigDecimal d1 = new BigDecimal(Double.toString(edges[start][end]));
                pi = pi.multiply(d1);
            }
            else if (status[i] == '0') {
                BigDecimal d2  = new BigDecimal(1).subtract(
                        new BigDecimal(Double.toString(edges[start][end])));
                pi = pi.multiply(d2);
            }
        }
        return pi;
    }

    public  static  BigDecimal calculatePi(int count, int[][] selectedEdges, AmwGraph amwGraph) {
        BigDecimal pi = new BigDecimal(1);
        double[][] edges = amwGraph.getEdges();
        if (count == 0) {
            for (int i = 0; i < selectedEdges.length; i++) {
                int start = selectedEdges[i][0];
                int end = selectedEdges[i][1];
                BigDecimal d = new BigDecimal(1).subtract(
                        new BigDecimal(Double.toString(edges[start][end])));
                pi = pi.multiply(d);
            }
        } else {
            for (int i = 0; i < count-1; i++) {
                int start = selectedEdges[i][0];
                int end = selectedEdges[i][1];
                BigDecimal d = new BigDecimal(1).subtract(
                        new BigDecimal(Double.toString(edges[start][end])));
                pi = pi.multiply(d);
            }
            BigDecimal d = new BigDecimal(
                    Double.toString(edges[selectedEdges[count-1][0]][selectedEdges[count-1][1]]));
            pi = pi.multiply(d);
        }

        return pi;
    }

    public static AmwGraph getSimpleGraph(AmwGraph amwGraph, int count, int[][] selectedEdges) {
        AmwGraph simpleGraph = amwGraph.clone();
        double[][] edges = simpleGraph.getEdges();
        if (count == 0) {
            for (int i = 0; i < selectedEdges.length; i ++) {
                int start = selectedEdges[i][0];
                int end = selectedEdges[i][1];
                edges[start][end] = 0;
            }
        }
        else {
            for (int i = 0; i < count-1; i++) {
                int start = selectedEdges[i][0];
                int end = selectedEdges[i][1];
                edges[start][end] = 0;
            }
            edges[selectedEdges[count-1][0]][selectedEdges[count-1][1]] = 1;
        }
        return simpleGraph;
    }

    public AmwGraph getSimpleGraph(AmwGraph amwGraph, String statusVector, int[][] selectedEdges) {
        AmwGraph simpleGraph = amwGraph.clone();
        double[][] edges = simpleGraph.getEdges();
        char[] stauts = statusVector.toCharArray();
        for (int i = 0; i < selectedEdges.length; i ++) {
            int start = selectedEdges[i][0];
            int end = selectedEdges[i][1];
            if (stauts[i] == '1')
                edges[start][end] = 1;
            else
                edges[start][end] = 0;
        }
        return simpleGraph;

    }

    public String getStauts(int num, int r) {
        StringBuffer buffer = new StringBuffer(Integer.toBinaryString(num));
        buffer.reverse();
        int len = buffer.length();
        for (int i = len; i < r; i ++) {
            buffer.append(0);
        }
        return buffer.toString();
    }

    // 通过BFS算法选择从start开始的前r个边
    public static int[][] edge_selection(AmwGraph amwGraph, int r, int start) {
        int count = 0;
        int[][] selectedEdges = new int[r][2];
        double[][] edges = amwGraph.getEdges();
        boolean[] visited = new boolean[amwGraph.getMAXVEX()];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        visited[start] = true;
        sign:
        while (!queue.isEmpty()) {
            int currNode = queue.poll();
            for (int i = 0; i < amwGraph.getMAXVEX(); i++) {
                if (edges[currNode][i] > 0) {
                    queue.offer(i);
                    visited[i] = true;
                    selectedEdges[count][0] = currNode;
                    selectedEdges[count][1] = i;
                    count++;
                    if (count == r)
                        break sign;
                }
            }
        }
        return selectedEdges;
    }
}
