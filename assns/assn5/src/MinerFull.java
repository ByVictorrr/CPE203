import processing.core.PImage;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.List;
import java.util.Optional;

 public class MinerFull extends Miner{




    public MinerFull( String id, Point position,
                       List<PImage> images, int resourceLimit, int resourceCount,
                       int actionPeriod, int animationPeriod)
    {
        super(id , position,images,resourceLimit, resourceCount,actionPeriod,animationPeriod);
    }


    public static MinerFull createMinerFull(String id, int resourceLimit,
                                         Point position, int actionPeriod, int animationPeriod,
                                         List<PImage> images)
    {
        return new MinerFull( id, position, images, resourceLimit,0, actionPeriod, animationPeriod);
    }




    public void execute( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(), Blacksmith.class);

        if (fullTarget.isPresent() &&
                moveTo( world, fullTarget.get(), scheduler))
        {
            this.transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    public void transformFull( WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore)
    {
        MinerNotFull miner = MinerNotFull.createMinerNotFull(this.getId(), this.getResourceLimit(),
                this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity( this);
        scheduler.unscheduleAllEvents( this);

        world.addEntity((Entity) miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }


//MoveToFull
    public  boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition( world, target.getPosition());

           // System.out.println("next Position: " + nextPos);
            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant( nextPos);

                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents( occupant.get());
                }
                world.moveEntity( this, nextPos);
            }
            return false;
        }
    }






}