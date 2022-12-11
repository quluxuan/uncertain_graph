package domain;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import indexTree.CreateTree;
import reachablilityQuery.Sampling;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Reduction {


    public static void main(String[] args) throws IOException {
        GraphCreate create = new GraphCreate();
        String filePath = "G:\\workspace\\uncertain_graph\\src\\main\\java\\data\\test.txt";
        List<String> list = create.readTxtFile(filePath);
        double threshold = getThreshold(list);
//        System.out.println(threshold);
        reduction(list, 0.15);
    }

    public static Map<Integer, List<Integer>> getCount(Map<Integer, Map<Integer,Double>> reliablity) {
        Map<Integer, List<Integer>> res = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, Double>> entry : reliablity.entrySet()) {
            List<Integer> list = new ArrayList<>();
            int count1 = 0;
            int count2 = 0;
            int count3 = 0;
            int count4 = 0;
            int count5 = 0;
            int count6 = 0;
            for (Map.Entry<Integer,Double> entry1 : entry.getValue().entrySet()) {
                double value = entry1.getValue();
                if (value  == 1.0)
                    count1++;
                else if (value > 0.95)
                    count2++;
                else if (value > 0.90)
                    count3++;
                else if (value > 0.80)
                    count4++;
                else if (value > 0.70)
                    count5++;
                else
                    count6++;
            }
            list.add(count1);
            list.add(count2);
            list.add(count3);
            list.add(count4);
            list.add(count5);
            list.add(count6);
            res.put(entry.getKey(), list);
        }
        return res;
    }

    public static Map<Integer, Map<Integer,Double>> getReliability(AmwGraph amwGraph, List<Integer> list, int N) {
        Map<Integer, Map<Integer,Double>> res = new HashMap<>();
        for (int i : list) {
            Map<Integer, Double> map = new HashMap<>();
            for (int j : list) {
                if (i != j && amwGraph.reach(i,j)) {
                    double reachability = Sampling.getReachability(amwGraph, i, j, N);
                    map.put(j, reachability);
                }
            }
            res.put(i,map);
        }
        return res;
    }

    public static double getThreshold(List<String> list) {
        List<Double> weight = new ArrayList<>();
        for (String str : list) {
            String[] arr = str.split(" ");
            weight.add(Double.valueOf(arr[2]));
        }
        Collections.sort(weight);
        Collections.reverse(weight);
        double num = weight.size() *0.5;
        return weight.get((int)Math.floor(num)-1);
    }
    public static void reduction(List<String> list, double threshold) throws IOException {
        FileWriter fw = new FileWriter("G:\\workspace\\uncertain_graph\\src\\main\\java\\data\\test_reduction.txt");
        for (String str : list){
            String[] arr = str.split(" ");
            double weigth = Double.valueOf(arr[2]);
            if (weigth > threshold)
                fw.write(str+"\n");
        }
        System.out.println("finish writing");
        fw.close();
    }
}

