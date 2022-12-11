package domain;

import domain.AdjGraph;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GraphCreate {

    public static AdjGraph createAdjGraph(String filePath){
        List<String> list = readTxtFile(filePath);
        String[][] edges = getEdges(list);
        String[] vertex = getVertex(edges);
        AdjGraph adj = new AdjGraph(vertex);
        adj.addEdg(edges);
        return adj;
    }


    public static AmwGraph createAmwGraph(String filePath){
        List<String> list = readTxtFile(filePath);
        String[][] edges = getEdges(list);
        String[] vertex = getVertex(edges);
        AmwGraph awm = new AmwGraph(vertex);
        awm.addEdg(edges);
        return awm;
    }

    public static AmwGraph createSamplingAmwGraph(String filePath){
        List<String> list = readTxtFile(filePath);
        String[][] edges = getEdges(list);
        String[] vertex = getVertex(edges);
        AmwGraph awm = new AmwGraph(vertex);
        awm.addSampleEdge(edges);
        return awm;
    }

    public static String[][] getOfflineGraph(String filePath) {
        List<String> list = readTxtFile(filePath);
        String[][] edges = getEdges(list);
        String[] vertex = getVertex(edges);
        String[][] offlineGraph = new String[vertex.length][vertex.length];
        for (int i = 0; i < edges.length; i ++) {
            int[] pos = getIndex(vertex, edges[i]);
            int start = pos[0];
            int end = pos[1];
            offlineGraph[start][end] = edges[i][2];
        }
        return offlineGraph;

    }



    public static int[] getIndex(String[] vertex, String[] edge){
        int[] pos = new int[2];
        for (int i = 0; i <vertex.length ; i++){
            if (vertex[i].equals(edge[0])){
                pos[0] = i;
            }
            if (vertex[i].equals(edge[1])){
                pos[1] = i;
            }
        }
        return pos;
    }

    public static ArrayList<String> readTxtFile(String filePath) {
        ArrayList<String> list = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    // 将多个空格转换为一个空格
                    lineTxt = lineTxt.replaceAll("[' ']+"," ");
                    list.add(lineTxt);
                }
                read.close();
            } else System.out.println("文件不存在");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String[][] getEdges(List<String> list) {
        String[][] edges = new String[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            edges[i] = new String[3];
            String linetxt = list.get(i);
            String[] edgArr = linetxt.split(" ");
            for (int j = 0; j < edgArr.length; j++) {
                edges[i][j] = edgArr[j];
            }
        }
        return edges;
    }

    public static String[] getVertex(String[][] edges) {
        Set<String> set = new TreeSet<String>();
        for (int i = 0; i < edges.length; i++){
            for (int j =0;j<2;j++)
                set.add(edges[i][j]);
        }
        String[] vertexs = new String[set.size()];
        String[] vertex = set.toArray(vertexs);
        return vertex;
    }
}
