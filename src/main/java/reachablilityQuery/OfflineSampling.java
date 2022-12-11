package reachablilityQuery;

import domain.AdjGraph;
import domain.AmwGraph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class OfflineSampling {

    public static void getCloseness(AdjGraph adj, int N, String outFilePath) throws IOException{
        List<AdjGraph> graphs = new ArrayList<>();
        for (int i = 0; i < N; i++){
            graphs.add(Sampling.samping(adj));
        }
        ArrayList<AdjGraph.VertexType> verArr = adj.getVerArr();
        for (AdjGraph graph : graphs){
            for (AdjGraph.VertexType vertex : verArr) {
                int verNum = vertex.getVerNum();
                AdjGraph.AdgvexType edge = vertex.getEdg();
                while (edge != null) {
                    if (isExist(graph, verNum, edge.getVerNum())) {
                        edge.sampleEdge.add(1);
                    } else
                        edge.sampleEdge.add(0);
                    edge = edge.getNext();
                }
            }
        }
        writeGraph(adj,outFilePath);
    }

    public static boolean isExist(AdjGraph graph, int verNum, int edgeNum) {
        AdjGraph.VertexType vertex = graph.getVerArr().get(verNum);
        AdjGraph.AdgvexType edge = vertex.getEdg();
        while(edge != null) {
            if (edge.getVerNum() == edgeNum)
                return true;
            edge = edge.getNext();
        }
        return false;
    }

    private static void writeGraph(AdjGraph adj, String outFilePath) throws IOException {
        FileWriter fw = new FileWriter(outFilePath);
        ArrayList<AdjGraph.VertexType> verArr = adj.getVerArr();
        for (AdjGraph.VertexType vertex : verArr) {
            AdjGraph.AdgvexType edge = vertex.getEdg();
            while(edge != null) {
                String str = vertex.getVertex() + " " + edge.getVerName() + " ";
                StringBuffer temp = new StringBuffer();
                for (int i : edge.sampleEdge)
                    temp.append(i);
                str = str + temp.toString();
                fw.write(str+"\n");
                edge = edge.getNext();
            }
        }
        fw.close();
    }

    public static void getCenterRelated(AmwGraph amwGraph, String outFilePath, int N, Set<Integer> centerNodes) throws IOException {
        List<AmwGraph> amwGraphs = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            amwGraphs.add(Sampling.samping(amwGraph));
        }
        Map<Integer, Map<Integer, StringBuffer>> res = new HashMap<>();
        for (int i = 0; i < N; i ++) {
            AmwGraph graph = Sampling.samping(amwGraph);
            for (int centerNode : centerNodes) {
                if (res.get(centerNode) == null) {
                    res.put(centerNode, new HashMap<>());
                }
                for (int neighborNode : centerNodes) {
                    if (centerNode != neighborNode) {
                        if (res.get(centerNode).get(neighborNode)==null){
                            res.get(centerNode).put(neighborNode,new StringBuffer());
                        }
                        if (graph.reach(centerNode,neighborNode))
                            res.get(centerNode).get(neighborNode).append(1);
                        else
                            res.get(centerNode).get(neighborNode).append(0);
                    }


                }
            }
        }
        FileWriter fw = new FileWriter(outFilePath);
        for (Map.Entry entry : res.entrySet()) {
            int index = (int)entry.getKey();
            Map<Integer, StringBuffer> map = (Map<Integer, StringBuffer>) entry.getValue();
            for (Map.Entry entry1 :map.entrySet()) {
                String s = amwGraph.getName(index) + " " +
                        amwGraph.getName((int)entry1.getKey()) + " " + entry1.getValue();
                fw.write(s + "\n");
            }
        }
        fw.close();
    }
}
