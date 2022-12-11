package reachablilityQuery;

import java.util.*;

// 求强连通分量
public class Tarjan {
    private int numOfNode;
    private List<ArrayList<Integer>> graph; // 图
    private List<ArrayList<Integer>> result; // 保存极大强连通图
    private boolean[] inStack; // 节点是否在栈内，因为stack中寻找一个节点不方便
    private Stack<Integer> stack;
    private int[] dfn;
    private int[] low;
    private int time;

    public Tarjan(List<ArrayList<Integer>> graph, int numOfNode) {
        this.graph = graph;
        this.numOfNode = numOfNode;
        this.inStack = new boolean[numOfNode];
        this.stack = new Stack<>();
        dfn = new int[numOfNode];
        low = new int[numOfNode];
        Arrays.fill(dfn, -1); // 将dfn所有元素都置为-1，代表还没有被访问过
        result = new ArrayList<>();
    }

    public List<ArrayList<Integer>> run(){
        for (int i =0;i<numOfNode;i++){
            if (dfn[i] == -1) tarjan1(i);
        }
        return result;
    }

    public void tarjan1(int current) {
        dfn[current] = low[current] = time++;
        inStack[current] = true;
        stack.push(current);

        for (int i=0;i<graph.get(current).size();i++){
            int next = graph.get(current).get(i);
            if (dfn[next] == -1){
                tarjan1(next);
                low[current] = Math.min(low[current], low[next]);
            }else if (inStack[next]){
                low[current] = Math.min(low[current],dfn[next]);
            }
        }
        if (low[current] == dfn[current]){
            ArrayList<Integer> temp = new ArrayList<>();
            int j = -1;
            while (current!=j){
                j = stack.pop();
                inStack[j] = false;
                temp.add(j);
            }
            result.add(temp);
        }
    }

}
