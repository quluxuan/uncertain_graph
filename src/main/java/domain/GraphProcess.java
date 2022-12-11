package domain;

import java.util.*;

public class GraphProcess {


    // 获得节点i的入度
    public static int getInDegree(AdjGraph adj, int i) {
        int count = 0;
        for (int n = 0; n < adj.getVerArr().size(); n++) {
            AdjGraph.VertexType v = adj.getVerArr().get(n);
            AdjGraph.AdgvexType e = v.getEdg();
            while (e != null) {
                if (e.getVerNum() == i)
                    count++;
                e = e.getNext();
            }
        }
        return count;
    }

    // 获得节点i的出度
    public static int getOutDegree(AdjGraph adj, int i) {
        AdjGraph.AdgvexType e = adj.getVerArr().get(i).getEdg();
        int count = 0;
        while (e != null) {
            count++;
            e = e.getNext();
        }
        return count;
    }

}
