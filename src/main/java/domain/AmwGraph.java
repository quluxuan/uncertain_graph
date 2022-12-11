package domain;

import java.util.*;

public class AmwGraph {
    private int MAXVEX;
    private List<String> verrArr;
    private double[][] edges;
    private int[] color = new int[600];
    private String[][] sampleEdge;

    public String[][] getSampleEdge() {
        return sampleEdge;
    }

    public void setSampleEdge(String[][] sampleEdge) {
        this.sampleEdge = sampleEdge;
    }

    public boolean isDAG = true;

    public int getMAXVEX() {
        return MAXVEX;
    }

    public void setMAXVEX(int MAXVEX) {
        this.MAXVEX = MAXVEX;
    }

    public List<String> getVerrArr() {
        return verrArr;
    }

    public void setVerrArr(List<String> verrArr) {
        this.verrArr = verrArr;
    }

    public double[][] getEdges() {
        return edges;
    }

    public void setEdges(double[][] edges) {
        this.edges = edges;
    }

    AmwGraph(){}

    // 初始化邻接矩阵，为顶点数组赋值
    public AmwGraph(String[] arr){
        this.MAXVEX = arr.length;
        this.verrArr = new ArrayList<>();
        this.edges = new double[this.MAXVEX][this.MAXVEX];
        this.sampleEdge = new String[this.MAXVEX][this.MAXVEX];
        for (int i = 0; i < arr.length; i++){
            verrArr.add(arr[i]);
        }
        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr.length; j++){
                this.edges[i][j] = 0;
                this.sampleEdge[i][j] = null;
            }
        }
    }


    public void addEdg(String[][] arr){
        for (int i = 0; i < arr.length; i++){
            int[] pos = this.getPosition(arr[i]);
            int start = pos[0];
            int end = pos[1];
            this.edges[start][end] = Double.valueOf(arr[i][2]);
        }
    }

    public void addSampleEdge(String[][] arr) {
        for (int i = 0; i < arr.length; i++){
            int[] pos = this.getPosition(arr[i]);
            int start = pos[0];
            int end = pos[1];
            this.sampleEdge[start][end] = (arr[i][2]);
        }
    }

    private int[] getPosition(String[] arr){
        int[] pos = new int[2];
        for (int i = 0; i < this.MAXVEX; i++){
            if (verrArr.get(i).equals(arr[0])){
                pos[0] = i;
            }
            if (verrArr.get(i).equals(arr[1])){
                pos[1] = i;
            }
        }
        return pos;
    }

    // 得到index的第一个邻接点
    public int getFirst(int index){
        for (int i = 0; i < this.MAXVEX; i++){
            if (edges[index][i] > 0)
                return i;
        }
        return -1;
    }

    // 根据前一个邻接点获得下一个邻接点
    public int getNext(int index, int first){
        for (int i = first+1; i < this.MAXVEX; i++){
            if (edges[index][i] > 0)
                return i;
        }
        return -1;
    }

    /**
     * 深拷贝，复制一个新的对象，修改新对象的值不会影响原对象。
     */
    public AmwGraph clone(){
        AmwGraph cgraph = new AmwGraph();
        int len = this.MAXVEX;
        cgraph.setMAXVEX(this.MAXVEX);
        cgraph.setVerrArr(this.verrArr);
        double[][] edges = new double[len][len];
        double[][] temp = this.edges;
        for (int i = 0; i < len; i++){
            for (int j = 0; j < len; j++){
                edges[i][j] = temp[i][j];
            }
        }
        cgraph.setEdges(edges);
        return cgraph;
    }

    public List<String> getName(List<Integer> list){
        List<String> res = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++){
            res.add(this.getVerrArr().get(list.get(i)));
        }
        return res;
    }

    public String getName(Integer i) {
        return this.getVerrArr().get(i);
    }
    public int getIndex(String name){
        for (int i = 0; i < MAXVEX; i++){
            if (verrArr.get(i).equals(name))
                return i;
        }
        return -1;
    }

    public void show(){
        for (int i = 0; i < this.MAXVEX; i++){
            for (int j = 0; j < this.MAXVEX; j++){
                System.out.print(edges[i][j]+"\t");
            }
            System.out.println();
        }
    }
    public void showSampling() {
        for (int i = 0; i < this.MAXVEX; i++){
            for (int j = 0; j < this.MAXVEX; j++){
                System.out.print(sampleEdge[i][j]+"\t");
            }
            System.out.println();
        }
    }


    public boolean reach(int startNum, int endNum){
        boolean[] visited = new boolean[this.getMAXVEX()];
        double[][] edges = this.getEdges();
        int len = this.getMAXVEX();
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(startNum);
        visited[startNum] = true;
        while (!queue.isEmpty()){
            int currNode = queue.poll();
            if (currNode == endNum)
                return true;
            for (int j = 0; j < len; j++){
                if (edges[currNode][j] != 0 && !visited[j]){
                    queue.offer(j);
                    visited[j] = true;
                }
            }
        }
        return false;
    }

    public void DFS(int i) {
        color[i] = 1;
        for (int j = 0; j < MAXVEX; j++) {
            if (edges[i][j] != 0) {
                if (color[j] == 1){
                    isDAG = false;
                    break;
                } else if (color[j] == -1)
                    continue;
                else
                    DFS(j);
            }
        }
        color[i] = -1;
    }

    public boolean getResult() {
        for (int i = 0; i < MAXVEX; i++) {
            if (color[i] == -1) {
                continue;
            }
            DFS(i);
            if (!isDAG) {
                return false;
            }
        }
        return true;
    }

    public AmwGraph initGraph() {
        AmwGraph graph = this.clone();
        double[][] initEdges = new double[MAXVEX][MAXVEX];
        graph.setEdges(initEdges);
        return graph;
    }

}
