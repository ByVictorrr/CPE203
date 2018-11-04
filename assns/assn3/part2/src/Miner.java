import processing.core.PImage;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.lang.reflect.Executable;
import java.util.List;
import java.util.Optional;

abstract public class Miner extends Actioned {

    protected int resourceLimit;
    protected int resourceCount;


    public Miner(String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }


    abstract public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler);


    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x,
                    this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }


}
