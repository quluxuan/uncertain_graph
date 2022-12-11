package indexTree;
//
//import domain.AmwGraph;
//import domain.GraphCreate;
//
//import java.util.*;
public class PathCount {
//    //有向带权图。-1表示无路可通。自己到自己也是-1。其它表示权值。
//    private static String[][] graph;
//
//    //true-表示该结点已访问过。false-表示还没有访问过。
//
//    private static boolean[] hasFlag;
//    public Map<String,String> res=new HashMap<>();
//
//    public PathCount(String[][] graph){
//        this.graph = graph;
//        hasFlag = new boolean[graph.length];
//    }
//    //最后的所有的路径的结果。每一条路径的格式是如：0->2->1->3:7
//
//    //求在图graph上源点s到目标点d之间所有的简单路径，并求出路径的和。
//    public void getPaths(int start,int end, String path, String sum) {
//        hasFlag[start] = true;//源点已访问过.
//        for (int i = 0; i < graph.length; i++) {
//            if (graph[start][i] == null || hasFlag[i]) {
//                continue;
//            }
//            //若无路可通或已访问过，则找下一个结点。
//            if (i == end)//若已找到一条路径
//            {
//                res.put(path + "->" + end, and(sum, graph[start][i]));//加入结果。
//                continue;
//            }
//
//            getPaths(i, end, path + "->" + i, and(sum, graph[start][i]));//继续找
//            hasFlag[i] = false;
//        }
//    }
//
//    public String and(String a, String b) {
//        char[] c1 = a.toCharArray();
//        char[] c2 = b.toCharArray();
//        StringBuilder builder = new StringBuilder();
//        if (c1.length != c2.length){
//            System.out.println("Invalid Operation");
//            return null;
//        }
//        for (int i = 0; i < c1.length; i++) {
//            int andNum = (c1[i]-48) & (c2[i]-48);
//            builder.append(andNum);
//        }
//        return builder.toString();
//    }

}