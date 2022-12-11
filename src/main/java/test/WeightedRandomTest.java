package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.DoubleStream;

public class WeightedRandomTest {
    public static void main(String[] args) {
        ArrayList<Atom> atomList = new ArrayList<Atom>();
        atomList.add(new Atom("01",0.123104667));
        atomList.add(new Atom("02",0.227615453));
//        atomList.add(new Atom("03",30));
//        atomList.add(new Atom("04",40));
//
        Atom atom;
//        = WeightedRadom.getWeigthedRandomAtom(atomList);

        // 累积记录某种对象出现的次数
        Map<String,Integer> countAtom = new HashMap<String, Integer>();
        for (int i=0;i<10000;i++){
            atom = WeightedRadom.getWeigthedRandomAtom(atomList);
            if (countAtom.containsKey(atom.getId())){
                countAtom.put(atom.getId(), countAtom.get(atom.getId())+1);
            }else countAtom.put(atom.getId(),1);
        }
        System.out.println("概率统计：");
        for (String id:countAtom.keySet()){
            System.out.println(" " + id + "出现了" +countAtom.get(id) + "次");
        }
        

    }
}

class Atom {
    private String id; // 对象标识参数
    private double weight;

    public Atom(String id, double weight) {
        this.id = id;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

/**
 * 加权随机算法，获取带有权值的随即元素
 */
class WeightedRadom {
    /**
     * 获取加权随机对象
     *
     * @param atomList
     * @return Atom
     */
    public static Atom getWeigthedRandomAtom(ArrayList<Atom> atomList) {
        if (atomList.isEmpty()) return null;
        // 获取总权值之间任意一随机数
        double random = new Random().nextDouble(); // random in [0,weightSum]
        // {.},{..},{...}...根据权值概率区间，获得加权随机对象
        for (Atom atom:atomList){
            random -= atom.getWeight();
            if (random<0)
                return atom;
        }
        return null;
    }
}