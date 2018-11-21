
public class Animation extends Action{



    public Animation(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount)
    {
        super(entity,world,imageStore,repeatCount);
    }


    public static Animation createAnimationAction(Entity entity, int repeatCount)
    {
        return new Animation( entity, null, null, repeatCount);
    }



    public  void executeAction(EventScheduler scheduler)
    {
        //if (this instanceof )
        this.entity.nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity, createAnimationAction(this.entity, Math.max(this.repeatCount - 1, 0) ), this.entity.getAnimationPeriod());
        }
    }

}