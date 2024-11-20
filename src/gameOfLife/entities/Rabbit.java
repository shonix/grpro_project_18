package gameOfLife.entities;

import itumulator.executable.DisplayInformation;

import java.awt.*;

public class Rabbit {
    //class fields begin
    //define on the class, all possible images a rabbit can have
    public static final Color RABBIT_COLOR = new Color(218, 205, 184); //color on top-down world view
    private static final DisplayInformation SMALL_RABBIT = new DisplayInformation(RABBIT_COLOR, "rabbit-small");
    private static final DisplayInformation SMALL_RABBIT_SLEEPING = new DisplayInformation(RABBIT_COLOR, "rabbit-small-sleeping");
    private static final DisplayInformation SMALL_RABBIT_FUNGI = new DisplayInformation(RABBIT_COLOR, "rabbit-fungi-small");
    private static final DisplayInformation SMALL_RABBIT_FUNGI_SLEEPING = new DisplayInformation(RABBIT_COLOR, "rabbit-fungi-small-sleeping");
    private static final DisplayInformation LARGE_RABBIT = new DisplayInformation(RABBIT_COLOR, "rabbit-large");
    private static final DisplayInformation LARGE_RABBIT_SLEEPING = new DisplayInformation(RABBIT_COLOR, "rabbit-large-sleeping");
    private static final DisplayInformation LARGE_RABBIT_FUNGI = new DisplayInformation(RABBIT_COLOR, "rabbit-fungi-large");
    private static final DisplayInformation LARGE_RABBIT_FUNGI_SLEEPING = new DisplayInformation(RABBIT_COLOR, "rabbit-fungi-large-sleeping");
    //class fields end

    //instance fields begin
    private DisplayInformation currentDisplayInformation;


}
