package com.example.magic06patterncomposite.demo1;

/**
 * 组合模式案例
 *
 * Province是一个省份，包含多个城市，每个城市包含多个区县
 * 同时，省份也是一个PopulationNode，城市也是一个PopulationNode，区县也是一个PopulationNode
 * 一个PopulationNode肚子里装着其他PopulationNode，是一种嵌套的关系，组合在一起后，可以表达出很复杂的类似树状的结构
 *
 */
public class Main {

    public static void main(String[] args) {
        Province province = new Province("浙江");

        City hz  = new City("杭州");
        hz.addDistrict(new District("钱塘", 100));
        hz.addDistrict(new District("滨江", 200));
        hz.addDistrict(new District("西湖", 300));

        City huz = new City("湖州");
        huz.addDistrict(new District("南浔", 10));
        huz.addDistrict(new District("吴兴", 20));
        huz.addDistrict(new District("德清", 30));

        province.addCity(hz);
        province.addCity(huz);
        System.out.println("杭州人口:" + hz.computePopulation());
        System.out.println("湖州人口:" + huz.computePopulation());

    }

}
