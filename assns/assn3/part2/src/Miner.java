import processing.core.PImage;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.lang.reflect.Executable;
import java.util.List;
import java.util.Optional;

abstract public class Miner extends Moved {

    protected int resourceLimit;
    protected int resourceCount;


    public Miner(String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }


    abstract public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler);



}
