package SSSPD;

import java.util.LinkedList;

public class Vertex {
    String name;   //顶点名称
    LinkedList<Vertex> adj;  //相邻顶点
    int dist;      //距离
    Vertex path;  // 最短路径中的前一个顶点

    public Vertex(String nm){
        name=nm;
        adj=new LinkedList<Vertex>();
        reset();
    }
    public void reset(){
        dist=Graph.INFINITY;
        path=null;
    }

}
