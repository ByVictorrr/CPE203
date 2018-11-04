import processing.core.PImage;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.lang.reflect.Executable;
import java.util.List;

abstract public class Activated extends Actioned{




    public Activated(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod)
    {
        super(id,position,images,resourceLimit, resourceCount,actionPeriod,animationPeriod);
    }





    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

}