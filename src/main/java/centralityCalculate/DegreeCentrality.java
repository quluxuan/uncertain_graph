package centralityCalculate;

import SSSPD.Graph;
import domain.AdjGraph;
import domain.GraphProcess;
import javafx.scene.control.DatePicker;
import reachablilityQuery.Sampling;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class DegreeCentrality {

    /**
     *
     * @param adj：使用邻接表计算度中心性
     * @param k：取前k个节点
     * @param N：采样次数
     * @return 前k个度中心节点索引
     */
    public static List<Integer> getTokDC(AdjGraph adj, int k, int N){
        List<List<Integer>> lists = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            // 采样N个可能世界
            List<AdjGraph> graphs = CentralityProcess.getSamples(adj,N);

            // 计算N个可能世界的平均度中心性
            Map<Integer, Double> centrality = CentralityProcess.sortedDescend(
                    DegreeCentralityCalculate(graphs, adj.getMAXVEX()));
            List<Integer> key = new ArrayList<>(centrality.keySet());
            lists.add(key);

        }
        return CentralityProcess.topK(lists, k, adj.getMAXVEX());
    }



    public static Map<Integer, Double> getDegreeCentrality(AdjGraph adjGraph, int N) {
        List<AdjGraph> graphs = CentralityProcess.getSamples(adjGraph,N);

        // 计算N个可能世界的平均度中心性
        Map<Integer, Double> centrality = CentralityProcess.sortedDescend(
                DegreeCentralityCalculate(graphs, adjGraph.getMAXVEX()));
        return centrality;
    }


    public static Map<Integer, Double> DegreeCentralityCalculate(List<AdjGraph> graphs, int maxVex){
        Map<Integer, Double> res = new HashMap<>();
        int size = graphs.size();
        // 计算N个可能世界的各节点度数
        List<List<Integer>> degrees = new ArrayList<>();
        for (int i = 0; i < size; i++){
            degrees.add(new ArrayList<>(getDegree(graphs.get(i), maxVex)));
        }
        // 计算各节点度数的平均值
        List<Double> mean = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.0000");
        for (int i = 0; i < maxVex; i++){
            int sum = 0;
            for (int j = 0; j < degrees.size(); j ++){
                sum = sum + degrees.get(j).get(i);
            }
            double avg = (double)sum / size;
            mean.add(Double.parseDouble(df.format(avg)));
        }

        // 根据N个可能世界计算各节点的度中心性
        for (int i = 0; i < maxVex; i++){
            double centrality = mean.get(i) / (maxVex - 1);
            res.put(i, centrality);
        }
        return res;
    }
    public static List<Integer> getDegree(AdjGraph graph, int maxVex){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < maxVex; i++){
            int degree = GraphProcess.getInDegree(graph, i) + GraphProcess.getOutDegree(graph, i);
            list.add(degree);
        }
        return list;
    }

}
