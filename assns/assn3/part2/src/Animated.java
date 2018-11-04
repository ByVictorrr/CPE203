import java.util.List;

abstract public class Animated extends Entity{

    public Animated(String id, Point position,
                      List<PImage> images, int resourceLimit, int resourceCount,
                      int actionPeriod, int animationPeriod)
    {
        super(id,position,images,resourceLimit, resourceCount,actionPeriod,animationPeriod);
    }

    public void scheduleActions( EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {

        scheduler.scheduleEvent( this,
                Activity.createActivityAction(this, world, imageStore),
                this.actionPeriod);

    }


    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }


    abstract public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
