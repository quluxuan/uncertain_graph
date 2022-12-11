package SSSPD;

import domain.AmwGraph;
import domain.GraphCreate;

import java.util.List;

public class ShortestPath {

    public static List<List<String>> shortestPath(AmwGraph amwGraph, int r) {
        //构建图
        Graph graph=new Graph();
//        String filePath = "G:\\workspace\\uncertain_graph\\src\\main\\java\\graph\\small.txt";
//        List<String> list = GraphCreate.readTxtFile(filePath);
//        String[][] edges = GraphCreate.getEdges(list);
//        for (int i = 0; i < edges.length; i++){
//            graph.addEdge(edges[i][0],edges[i][1]);
//        }
//
//        String[] vertex = GraphCreate.getVertex(edges);
        String[] vertex = new String [amwGraph.getMAXVEX()];
        for (int i = 0; i < amwGraph.getMAXVEX(); i++){
            vertex[i] = amwGraph.getVerrArr().get(i);
        }
        for (int i = 0; i < amwGraph.getMAXVEX(); i++){
            for (int j = 0; j < amwGraph.getMAXVEX(); j++){
                if (amwGraph.getEdges()[i][j] != 0){
                    graph.addEdge(amwGraph.getVerrArr().get(i), amwGraph.getVerrArr().get(j));
                }
            }
        }
        //以v3为起点计算无权图的最短路径
        graph.unweighted(vertex[r]);
        //打印从v3到各个顶点的最短路径
        for (int i = 0; i < vertex.length; i++)
            graph.printPath(vertex[i]);
//        System.out.println(graph.shortesPah);
        return graph.shortesPah;
    }
    private int getIndex(String[] vertex, String str){
        for (int i = 0; i < vertex.length; i++){
            if (vertex[i] == str)
                return i;
        }
        return -1;
    }
}
