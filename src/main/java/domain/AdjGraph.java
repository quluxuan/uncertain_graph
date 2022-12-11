package domain;

import SSSPD.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AdjGraph{
    private int MAXVEX; // 顶点数组长度
    private ArrayList<VertexType> verArr;// 顶点数组
    private Boolean[] visited;

    public Boolean[] getVisited() {
        return visited;
    }

    public void setVisited(Boolean[] visited) {
        this.visited = visited;
    }

    public int getMAXVEX() {
        return MAXVEX;
    }

    public void setMAXVEX(int MAXVEX) {
        this.MAXVEX = MAXVEX;
    }

    public ArrayList<VertexType> getVerArr() {
        return verArr;
    }

    public void setVerArr(ArrayList<VertexType> verArr) {
        this.verArr = verArr;
    }


    // 顶点数据
    public class VertexType {
        String vertex = null; // 存储顶点
        AdgvexType adg = null; // 存储第一个邻接点的引用
        int verNum = -1; // 存储顶点的下标

        public int getVerNum() {
            return verNum;
        }

        public void setVerNum(int verNum) {
            this.verNum = verNum;
        }

        public VertexType() {
        }

        public VertexType(String vertex) {
            this.vertex = vertex;
        }

        public VertexType(String vertex, int verNum) {
            this.vertex = vertex;
            this.verNum = verNum;
        }

        public String getVertex() {
            return vertex;
        }

        public void setVertex(String vertex) {
            this.vertex = vertex;
        }

        public AdgvexType getEdg() {
            return adg;
        }

        public void setEdg(AdgvexType adg) {
            this.adg = adg;
        }

        @Override
        public String toString() {
            return "VertexType{" +
                    "vertex='" + vertex + '\'' +
                    ", verNum=" + verNum +
//                    ", adg=" + adg +
                    '}';
        }
    }

    // 边表节点
    public class AdgvexType {
        int verNum = -1; // 存储顶点数组下标，默认为零
        double WeightNum = 0.0; // 权重
        String verName = null;
        AdgvexType adg = null;  // 存储下一个邻接点的引用
        public List<Integer> sampleEdge = new ArrayList<>() ;

        public int getVerNum() {
            return verNum;
        }

        public void setVerNum(int verNum) {
            this.verNum = verNum;
        }

        public double getWeightNum() {
            return WeightNum;
        }

        public void setWeightNum(double weightNum) {
            WeightNum = weightNum;
        }

        public String getVerName() {
            return verName;
        }

        public void setVerName(String verName) {
            this.verName = verName;
        }

        public AdgvexType getNext() {
            return adg;
        }

        public void setAdg(AdgvexType adg) {
            this.adg = adg;
        }

        @Override
        public String toString() {
            String str = "AdgvexType{" +
//                    "verName='" + verName + '\'' +
                    "verNum = " + verNum +
                    ", WeightNum = " + WeightNum;
            if (sampleEdge.size() != 0) {
                StringBuffer buff = new StringBuffer();
                buff.append(", sampleEdge = ");
                for (int i : sampleEdge)
                    buff.append(i);
                str += buff.toString() + "}";
            } else
                str += "}";
            return str;
        }
    }


    // 初始化，为顶点数组赋值
    public AdjGraph(String[] arr) {
        this.MAXVEX = arr.length;
        this.verArr = new ArrayList<VertexType>();
        this.visited = new Boolean[MAXVEX];
        for (int i = 0; i < arr.length; i++) {
            verArr.add(new VertexType(arr[i], i));
            visited[i] =false;
        }

    }

    public void addEdg(String[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            AdgvexType temp = null;
            int[] pos = this.getPosition(arr[i]);
            AdgvexType adg = new AdgvexType();
            adg.setWeightNum(Double.valueOf(arr[i][2])); // 设置权值
            adg.setVerName(arr[i][1]);
            adg.setVerNum(pos[1]);
            temp = verArr.get(getIndex(pos[0])).getEdg(); // temp为当前顶点的邻接点
            if (temp == null) {
                verArr.get(pos[0]).setEdg(adg);
                continue;
            }
            while (true) {
                // 如果temp的邻接点不为null，则temp继续指向下一个邻接点
                if (temp.getNext() != null)
                    temp = temp.getNext();
                    // 直到temp的邻接点为null，插入当前邻接点
                else {
                    temp.setAdg(adg);
                    break;
                }
            }
        }
    }

    // 查找顶点位置
    private int[] getPosition(String[] arr) {
        int[] arrInt = new int[2];
        for (int i = 0; i < this.MAXVEX; i++) {
            if (verArr.get(i).getVertex().equals(arr[0])) {
                arrInt[0] = verArr.get(i).getVerNum();
                break;
            }
        }
        for (int i = 0; i < this.MAXVEX; i++) {
            if (verArr.get(i).getVertex().equals(arr[1])) {
                arrInt[1] = verArr.get(i).getVerNum();
                break;
            }
        }
        return arrInt;
    }

    private int getIndex(int x) {
        int index = 0;
        for (int i = 0; i < verArr.size(); i++) {
            if (verArr.get(i).getVerNum() == x) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void show() {
        for (VertexType v : verArr) {
            if (v.getEdg() == null) {
                System.out.println(v);
                continue;
            }
            AdgvexType temp = v.getEdg();
            String s = v + "-->" + temp;
            while (true) {
                if (temp.getNext() != null) {
                    temp = temp.getNext();
                    s += "-->" + temp;
                } else {
                    System.out.println(s);
                    break;
                }
            }
        }
    }

    // 深度遍历

    public void DFS(int verNum) {
        if (verNum > MAXVEX || verNum < 0) throw new IndexOutOfBoundsException("角标越界");
        this.visited[verNum] = true;
//        System.out.print(verArr.get(verNum).getVertex() + "-->");
        AdgvexType temp = verArr.get(verNum).getEdg();
        while (temp != null) {
            if (!this.visited[temp.verNum])
                DFS(temp.verNum);
            temp = temp.getNext();
        }

    }

    public void DFSTraverse() {
        for (int v = 0; v < this.MAXVEX; v++)
            this.visited[v] = false;
        for (int v = 0; v < this.MAXVEX; v++)
            if (!visited[v])
                DFS(v);
        System.out.println();    
    }
    public int ifConnectivity(){
        for (int v = 0; v < this.MAXVEX; v++)
            this.visited[v] = false;
        DFS(0);
        for (int v = 0;v<this.MAXVEX;v++){
            if (!visited[v]) return 0;
        }
        return 1;
    }

    // 可达查询
    public int reach(String s, String t) {
        int startNum = -1, endNum = -1;
        for (int i = 0; i < MAXVEX; i++) {
            if (verArr.get(i).getVertex().equals(s))
                startNum = i;
            if (verArr.get(i).getVertex().equals(t))
                endNum = i;
        }
        if (startNum < 0 || endNum < 0) throw new IllegalArgumentException("给定顶点不存在");
        this.visited[startNum] = true;
        if (startNum == endNum)
            return 1;
        AdgvexType temp = verArr.get(startNum).getEdg();
        while (temp != null) {
            if (!this.visited[temp.verNum])
                // 有返回值得函数得递归调用一定要加return
                return reach(temp.verName,t);
            temp = temp.getNext();
        }
        return 0;
    }

    public String indexOf(int index) {
        return verArr.get(index).vertex;
    }
}
