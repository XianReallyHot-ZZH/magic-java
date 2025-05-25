package com.example.magic06patterncomposite.demo1;

public class District implements PopulationNode {

    private final String name;

    private int population;

    public District(String name, int population) {
        this.name = name;
        this.population = population;
    }

    @Override
    public int computePopulation() {
        return population;
    }

}
