package test;


import SSSPD.Graph;
import domain.*;
import centralityCalculate.*;
import reachablilityQuery.*;
import indexTree.*;
import sun.java2d.opengl.OGLContext;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class TestAdg { 
    public static void main(String[] args) throws IOException, InterruptedException {
//        String filePath = "G:\\workspace\\uncertain_graph\\src\\main\\java\\data\\test.txt";



//        Set<Integer> set = new HashSet<>(list);
//        ReliabilityQuery query = new ReliabilityQuery(adj,amw,set);
//        for (String str : amw.getVerrArr()) {
//            System.out.println(amw.getIndex(str)+":" +query.getFirstCenter(amw.getIndex(str), amw.getEdges()));
//        }


        String filePath = "grn3.txt";
        AdjGraph adj = GraphCreate.createAdjGraph(filePath);
        AmwGraph amw = GraphCreate.createAmwGraph(filePath);

        Integer[] arr = {161, 131, 195, 69, 198, 39, 103, 135, 104, 43, 205, 79, 56, 156, 30};
        List<Integer> list = new ArrayList<>(Arrays.asList(arr));
        Set<Integer> set = new HashSet<>(list);

        for (Integer node : set) {
            System.out.println(amw.getName(node));
        }


//         采样
//        List<String> list = new ArrayList();
//        List<Float> time = new ArrayList<>();
//        for(int i = 0; i < 30; i ++){
//            float seconds = 0;
////            double result = 0;
//
//            long startTime = System.currentTimeMillis();
//            String s = Sampling.reachability(adj, "BAP1,YTHDC2", 10000);
//            long endTime = System.currentTimeMillis();
//            seconds = (endTime - startTime) / 1000F;
//            list.add(s);
//            time.add(seconds);
////            System.out.println("Run Time: " + Float.toString(seconds) + " seconds.");
//        }


//         没有可以缩减的路径
//        Reduction.Dist2Hop(adj,0,13);
//        adj.show();
//
//        int numOfNode = adj.getMAXVEX();
//        Prunning p = new Prunning(adj);
//        AdjGraph undirgraph = p.dirToUndir(filePath);
//        System.out.println(undirgraph.ifConnectivity());
//        Prunning p1 = new Prunning(undirgraph);
//        p1.getCut(0);

    // 求强连通分量
//        List<ArrayList<Integer>> graph = getGraph(undirgraph);
//        int numOfNode = adj.getMAXVEX();
//        Tarjan t = new Tarjan(graph, numOfNode);
//        List<ArrayList<Integer>> result = t.run();
//        for (int i = 0; i < result.size(); i++) {
//            System.out.println("The " + i + " group:");
//            for (int j = 0; j < result.get(i).size(); j++) {
//                System.out.print(result.get(i).get(j) + " ");
//            }
//            System.out.println();
//        }


//        ArrayList<ArrayList<Integer>> result = p.getConnected();
//        for (ArrayList a :result)
//            System.out.println(a);



    }

    public static void testCentrality(AdjGraph adjGraph, AmwGraph amwGraph) {
        System.out.println("------------------BetweennessCentrality------------------");
        List<Integer> res1 = CentralityProcess.moreThan(BetweennessCentrality.getCentrality(amwGraph, 10), 0.02);
        System.out.println(res1);
        System.out.println(res1.size());
        System.out.println();
        System.out.println("------------------DegreeCentrality------------------");
        List<Integer> res2 = CentralityProcess.moreThan(DegreeCentrality.getDegreeCentrality(adjGraph, 10), 0.012);
        System.out.println(res2);
        System.out.println(res2.size());
        System.out.println();
        System.out.println("------------------ClosenessCentrality------------------");
        List<Integer> res3 = CentralityProcess.moreThan(ClosenessCentrality.getClosenessCentrality(amwGraph, 10));
        System.out.println(res3);
        System.out.println(res3.size());
        System.out.println();
        System.out.println(CentralityProcess.getSame(res1,res2,res3));
        System.out.println(CentralityProcess.getSame(res1,res2,res3).size());
    }


    public static void testBSS(int start, int end, AmwGraph amwGraph, int r, int N) {

        ImprovedSampling sampling = new ImprovedSampling();
        long startTime, endTime;
        float time;

//        startTime = System.currentTimeMillis();
//        System.out.println(sampling.BSSI(amwGraph,N,r,start,end));
//        endTime = System.currentTimeMillis();
//        time = (endTime - startTime) / 1000F;
//        System.out.println("BSSI Run Time: " + Float.toString(time) + " seconds.");

        startTime = System.currentTimeMillis();
        double count = 0;
        for (int i = 0; i < N; i++){
            AmwGraph graph1 = Sampling.samping(amwGraph);
            int reach = graph1.reach(start,end)?1:0;
            count += reach;
        }
        double res = count / N;
        System.out.println(String.format("%.8f", res));
        endTime = System.currentTimeMillis();
        time = (endTime - startTime) / 1000F;
        System.out.println("MC Run Time: " + Float.toString(time) + " seconds.");

//        startTime = System.currentTimeMillis();
//        System.out.println(sampling.RSSI(amwGraph,N, 10, amwGraph.getMAXVEX(), r, start, end));
//        endTime = System.currentTimeMillis();
//        System.out.println("RSSI run time " + (endTime - startTime) + "ms");

        startTime = System.currentTimeMillis();
        System.out.println(sampling.BSSII(amwGraph,N,r,start,end));
        endTime = System.currentTimeMillis();
        time = (endTime - startTime) / 1000F;
        System.out.println("BSSII Run Time: " + Float.toString(time) + " seconds.");

        startTime = System.currentTimeMillis();
        System.out.println(sampling.RSSII(amwGraph,N,100,amwGraph.getMAXVEX(),r,start,end));
        endTime = System.currentTimeMillis();
        time = (endTime - startTime) / 1000F;
        System.out.println("RSSII Run Time: " + Float.toString(time) + " seconds.");
    }

//    public static List<ArrayList<Integer>> getGraph(AdjGraph adj) {
//        List<ArrayList<Integer>> graph = new ArrayList<>();
//        for (int i = 0; i < adj.getMAXVEX(); i++)
//            graph.add(new ArrayList<>());
//        for (int i = 0; i < adj.getMAXVEX(); i++) {
//            AdjGraph.AdgvexType e = adj.getVerArr().get(i).getEdg();
//            while (e != null) {
//                graph.get(i).add(e.getVerNum());
//                e = e.getNext();
//            }
//        }
//        return graph;
//    }
}
