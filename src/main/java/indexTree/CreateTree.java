package indexTree;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import domain.AmwGraph;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CreateTree {
    public static int COUNT = 0;

    AmwGraph graph;
    String[][] offlineGraph;
    private static final int N = 10;
    private static boolean[] hasFlag;
    public CreateTree(AmwGraph graph, String[][] offlineGraph) {
        this.graph = graph;
        this.offlineGraph = offlineGraph;
        hasFlag = new boolean[offlineGraph.length];
    }



    public List<String> paths = new ArrayList<>();


//    public Map<Integer, List<Map<Integer, String>>> getSubTrees(List<Integer> centerNode) {
//        Map<Integer, List<Map<Integer, String>>> subTrees = new HashMap<>();
//        for (int i : centerNode) {
//            List<List<Integer>> subleveas = getSubLeaves(i);
//            System.out.println(i+"'s subtrees are: " + subleveas);
//            List<Map<Integer, String>> subTree = getWeight(i, subleveas);
//            subTrees.put(i, subTree);
//        }
//        return subTrees;
//    }

    public List<List<Integer>> getSubLeaves(int node) {
        List<List<Integer>> subTree = new ArrayList<>();
        List<Integer> leftSubTree = new ArrayList<>();
        List<Integer> rightSubTree = new ArrayList<>();
        int maxNum = graph.getMAXVEX();
        for (int i = 0; i < maxNum; i++) {
            if (i != node && graph.reach(i, node)){
                leftSubTree.add(i);
            }
            else if (i != node && graph.reach(node, i))
                rightSubTree.add(i);
        }
        subTree.add(leftSubTree);
        subTree.add(rightSubTree);
        return subTree;
    }

    public List<List<Integer>> getSubLeaves(int node, List<Integer> centerNodes) {
        List<List<Integer>> subTree = new ArrayList<>();
        List<Integer> leftSubTree = new ArrayList<>();
        List<Integer> rightSubTree = new ArrayList<>();

        for (int i : centerNodes) {
            if (i != node && graph.reach(i, node))
                leftSubTree.add(i);
            if (i != node && graph.reach(node, i))
                rightSubTree.add(i);
        }
        subTree.add(leftSubTree);
        subTree.add(rightSubTree);
        return subTree;
    }

//    public List<Map<Integer, String>> getWeight(int node, List<List<Integer>> subTree) {
//        List<Map<Integer, String>> res = new ArrayList<>();
//
//        List<Integer> leftSubTrees = subTree.get(0);
//        Map<Integer, String> leftWeight = new HashMap<>();
//        for (int leftLeaves : leftSubTrees){
//            Map<String, String> path = new HashMap<>();
//            getPaths(path, leftLeaves, node, ""+leftLeaves,String.join("", Collections.nCopies(N,"1")));
//            String sum = getOrSum(path);
//            leftWeight.put(leftLeaves, sum);
//        }
//        res.add(leftWeight);
//
//        List<Integer> rightSubTrees = subTree.get(1);
//        Map<Integer, String> rightWeight = new HashMap<>();
//        for (int rightLeaves : rightSubTrees){
//            Map<String, String> path = new HashMap<>();
//            getPaths(path,node, rightLeaves, ""+rightLeaves,String.join("", Collections.nCopies(N,"1")));
//            String sum = getOrSum(path);
//            rightWeight.put(rightLeaves, sum);
//
//        }
//        res.add(rightWeight);
//
//        return res;
//    }


    // 循环次数过多
    public void getPaths(Map<String,String> res, int start, int end, String path) throws InterruptedException {
        hasFlag[start] = true;//源点已访问过.
        for (int i = 0; i < offlineGraph.length; i++) {
            if (offlineGraph[start][i] == null || hasFlag[i]) {
//                System.out.println("pass");
                continue;
            }
            //若无路可通或已访问过，则找下一个结点。
            if (i == end)//若已找到一条路径
            {
                res.put(++COUNT+"", path + "->" + graph.getName(end));//加入结果。
                System.out.println(path + "->" + graph.getName(end));
                TimeUnit.SECONDS.sleep(1);
                continue;
            }

            getPaths(res, i, end, path + "->" + graph.getName(i));//继续找
            hasFlag[i] = false;


        }
    }

    public String and(String a, String b) {
        char[] c1 = a.toCharArray();
        char[] c2 = b.toCharArray();
        StringBuilder builder = new StringBuilder();
        if (c1.length != c2.length){
            System.out.println("Invalid Operation");
            return null;
        }
        for (int i = 0; i < c1.length; i++) {
            int andValue = (c1[i]-48) & (c2[i]-48);
            builder.append(andValue);
        }
        return builder.toString();
    }

    public String getOrSum(Map<String, String> path) {
        String sum = String.join("", Collections.nCopies(N,"0"));
        for (Map.Entry<String ,String> entry : path.entrySet()) {
            String andValue = entry.getValue();
            sum = or(sum, andValue);
        }
        return sum;
    }

    public String or(String a, String b) {
        char[] c1 = a.toCharArray();
        char[] c2 = b.toCharArray();
        StringBuilder builder = new StringBuilder();
        if (c1.length != c2.length){
            System.out.println("Invalid Operation");
            return null;
        }
        for (int i = 0; i < c1.length; i++) {
            int andValue = (c1[i]-48) | (c2[i]-48);
            builder.append(andValue);
        }
        return builder.toString();
    }
}
