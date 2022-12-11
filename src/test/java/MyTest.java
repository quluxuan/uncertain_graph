import domain.AdjGraph;
import domain.AmwGraph;
import domain.GraphCreate;
import org.junit.Test;
import reachablilityQuery.BaseLine;
import reachablilityQuery.ImprovedSampling;
import reachablilityQuery.ReliabilityQuery;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class MyTest {
    @Test
    public void tempTest(){
        Double[] arr1 = {4.0,3.0,2.0,1.0};
        Double[] arr2 = {2.0,2.0,1.0,0.0};
        List<Double> list1 = new ArrayList<>(Arrays.asList(arr1));
        List<Double> list2 = new ArrayList<>(Arrays.asList(arr2));
        double result = getMSE(list1,list2);
        System.out.println(result);
    }

    @Test
    public void testResult() {
        String filePath = "D:\\东北大学\\毕设\\uncertain_graph\\src\\main\\java\\data\\test.txt";
        AdjGraph adj = GraphCreate.createAdjGraph(filePath);
//            adj.show();
        AmwGraph amw = GraphCreate.createAmwGraph(filePath);
//        amw.show();

        Integer[] arr = {129, 194, 514, 132, 133, 325, 263, 264, 137, 329, 522, 139, 76, 205, 79, 464, 401, 146, 404, 86, 343, 412, 287, 160, 164, 107, 500, 53, 504, 185, 60, 125, 317, 254};
        List<Integer> list = new ArrayList<>(Arrays.asList(arr));
        Set<Integer> set = new HashSet<>(list);

        ReliabilityQuery query = new ReliabilityQuery(amw,set,100);
        BaseLine base = new BaseLine(amw,set,100);

        List<Set<Integer>> subGraphs = query.partitionSubGraph();


        long startTime, endTime;
        double improve, baseResult;
        float seconds;

        System.out.println("优化算法：");
        startTime = System.currentTimeMillis();
        improve = query.getReliability(2,4,0.5,50);
        endTime = System.currentTimeMillis();
        seconds = (endTime - startTime) / 1000F;
        System.out.println("优化算法结果：" + improve);
        System.out.println("Run Time: " + Float.toString(seconds) + " seconds.");

        System.out.println();
        System.out.println("基础算法：");
        startTime = System.currentTimeMillis();
        baseResult = base.getReliability(2,4);
        endTime = System.currentTimeMillis();
        seconds = (endTime - startTime) / 1000F;
        System.out.println("优化算法结果：" + baseResult);
        System.out.println("Run Time: " + Float.toString(seconds) + " seconds.");

        System.out.println();
        System.out.println("优化算法：");
        startTime = System.currentTimeMillis();
        improve = query.getReliability(2,7,0.2,50);
        endTime = System.currentTimeMillis();
        seconds = (endTime - startTime) / 1000F;
        System.out.println("优化算法结果：" + improve);
        System.out.println("Run Time: " + Float.toString(seconds) + " seconds.");

        System.out.println();
        System.out.println("基础算法：");
        startTime = System.currentTimeMillis();
        baseResult = base.getReliability(2,7);
        endTime = System.currentTimeMillis();
        seconds = (endTime - startTime) / 1000F;
        System.out.println("优化算法结果：" + baseResult);
        System.out.println("Run Time: " + Float.toString(seconds) + " seconds.");

    }

    public double getMSE (List<Double> baseLine, List<Double> improved) {
        int size = baseLine.size();
        BigDecimal p1, p2;
        BigDecimal result = new BigDecimal(0);
        for (int i = 0; i < size; i++) {
            p1 = new BigDecimal(baseLine.get(i).toString());
            p2 = new BigDecimal(improved.get(i).toString());
            result = result.add((p1.subtract(p2)).pow(2));
        }
        result = result.divide(new BigDecimal(size), 8, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    @Test
    public void testNode() {
        String filePath = "D:\\东北大学\\毕设\\uncertain_graph\\src\\main\\java\\data\\grn1.txt";
        AdjGraph adj = GraphCreate.createAdjGraph(filePath);
        AmwGraph amw = GraphCreate.createAmwGraph(filePath);

        Integer[] arr = {161, 131, 195, 69, 198, 39, 103, 135, 104, 43, 205, 79, 56, 156, 30};
        List<Integer> list = new ArrayList<>(Arrays.asList(arr));
        Set<Integer> set = new HashSet<>(list);

        for (Integer node : set) {
            System.out.println(amw.getName(node));
        }
    }
}
