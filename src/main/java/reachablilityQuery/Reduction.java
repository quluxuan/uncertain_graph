package reachablilityQuery;

import domain.AdjGraph;
import domain.GraphProcess;
import java.util.*;

public class Reduction {

    /**
     * 1、查找s的二跳路径，判断中间结点出入度是否为1
     * 将二跳路径第一个结点取出，对其查询
     * 2、判断该路径为串联型还是三角型，计算相应的权值
     * 若s与其二跳结点之间右边则为三角型
     * 3、去掉中间结点，将s指向二跳结点
     * 两种路径，两种删除方法
     */

    public static void Dist2Hop(AdjGraph adj, int s, int t) {
        ArrayList<Integer> visited = new ArrayList<>();
        for (int i = 0; i < adj.getMAXVEX(); i++) {
            visited.add(i);
        }
        Integer[] path = new Integer[2];
        ArrayList<Integer> deleteList = new ArrayList<>();
        while (!visited.isEmpty()) {
            int v0 = visited.get(0);
            System.out.println(v0);
            ArrayList<Integer> vNe = findNeighbor(adj, v0);
            ArrayList<Integer> v2hop = find2hop(adj, v0);
            ArrayList<String> list = find2hopPath(adj, vNe, v2hop);

            while (!list.isEmpty()) {
//                if (list.isEmpty()){
//                    list = find2hopPath(adj,vNe,v2hop);
//                }
                String[] str = list.get(0).split(" ");
                list.remove(0);
                path[0] = Integer.valueOf(str[0]);
                path[1] = Integer.valueOf(str[1]);
//                System.out.println(path[0] + "..." + path[1]);
                if (GraphProcess.getInDegree(adj, path[0]) == 1 && GraphProcess.getOutDegree(adj, getIndex(adj,path[0])) == 1) {
                    int flag = judgeLinkKind(adj, v0, path);
                    deleteVertex(adj, v0, path, flag, deleteList);
                    AdjGraph.AdgvexType e = adj.getVerArr().get(getIndex(adj,path[1])).getEdg();
                    if (e != null) {
                        v2hop.add(e.getVerNum());
                    }
//                    v2hop.remove(path[1]);
                    System.out.println("删除的点是" + path[0]);
                    visited.remove(path[0]);
//                    vNe.remove(path[0]);
//                    vNe.add(path[1]);
                }
//                else {
//                    v2hop.remove(path[1]);
//                }
            }
            visited.remove(0);
        }

    }
    // 有问题
    public static void deleteVertex(AdjGraph adj, int v0, Integer[] path, int flag, ArrayList<Integer> list) {
        AdjGraph.VertexType v = adj.getVerArr().get(getIndex(adj,v0));
        AdjGraph.AdgvexType pre = v.getEdg();
        AdjGraph.AdgvexType e = pre.getNext();
        double w1 = 0;
        int index = 0;
        // 删除出现问题 判断pre是否为需要删除的那个结点
        if (pre.getVerNum() == path[0]){
            w1 = pre.getWeightNum();
            v.setEdg(e);
        }
        else {
            while (e != null) {
                if (e.getVerNum() == path[0]) {
                    w1 = e.getWeightNum();
                    pre.setAdg(e.getNext());
                }
                pre = e;
                e = e.getNext();
            }
        }
        double w2 = adj.getVerArr().get(getIndex(adj,path[0])).getEdg().getWeightNum();
        if (flag == 0) {
            AdjGraph.VertexType v2 = adj.getVerArr().get(getIndex(adj,path[1]));
            double weight = w1 * w2;
            String RPr = String.format("%.6f", weight);
            String[][] edge = {{v.getVertex(), v2.getVertex(), RPr}};
            adj.addEdg(edge);
            System.out.println(0 + "!");
        }
        if (flag == 1) {
            System.out.println(1 + "!");
            AdjGraph.AdgvexType e2 = v.getEdg();
            while (e2 != null) {
                if (e2.getVerNum() == path[1]) {
                    double w3 = e2.getWeightNum();
                    double weight = 1 - (1 - w1 * w2) * (1 - w3);
                    String RPr = String.format("%.6f", weight);
                    weight = Double.valueOf(RPr);
                    e2.setWeightNum(weight);
                    break;
                }
                e2 = e2.getNext();
            }
        }
        adj.getVerArr().remove(getIndex(adj,path[0]));

    }

    public static int getIndex(AdjGraph adj, int x) {
        int index = 0;
        for (int i = 0; i < adj.getVerArr().size(); i++) {
            if (adj.getVerArr().get(i).getVerNum() == x) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static int judgeLinkKind(AdjGraph adj, int v0, Integer[] path) {
        AdjGraph.AdgvexType e = adj.getVerArr().get(getIndex(adj,v0)).getEdg();
        while (e != null) {
            if (e.getVerNum() == path[1])
                return 1;
            e = e.getNext();
        }
        return 0;
    }

    // 根据邻接点和二跳结点查找二条路径
    public static ArrayList<String> find2hopPath(AdjGraph adj, ArrayList<Integer> vNeb, ArrayList<Integer> v2hop) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < vNeb.size(); i++) {
            AdjGraph.AdgvexType e = adj.getVerArr().get(getIndex(adj,vNeb.get(i))).getEdg();
            while (e != null) {
                if (v2hop.contains(e.getVerNum())) {
                    String str = vNeb.get(i) + " " + e.getVerNum();
                    list.add(str);
                }
                e = e.getNext();
            }
        }
        return list;
    }

    // 二跳结点数组是否应为唯一
    public static ArrayList<Integer> find2hop(AdjGraph adj, int i) {
//        HashSet<Integer> set = new HashSet<>();
        ArrayList<Integer> newList = new ArrayList<>();
        ArrayList<Integer> list = findNeighbor(adj, i);
        for (int m = 0; m < list.size(); m++) {
            AdjGraph.AdgvexType e = adj.getVerArr().get(getIndex(adj,list.get(m))).getEdg();
            while (e != null) {
                newList.add(e.getVerNum());
                e = e.getNext();
            }
        }
//        newList.addAll(set);
        return newList;
    }


    public static ArrayList<Integer> findNeighbor(AdjGraph adj, int i) {
        ArrayList<Integer> list = new ArrayList<>();
        AdjGraph.VertexType v = adj.new VertexType();
        for (int m = 0; m < adj.getVerArr().size(); m++) {
            if (adj.getVerArr().get(m).getVerNum() == i) {
                v = adj.getVerArr().get(m);
                break;
            }
        }
        AdjGraph.AdgvexType e = v.getEdg();
        while (e != null) {
            list.add(e.getVerNum());
            e = e.getNext();
        }
        return list;
    }
}
