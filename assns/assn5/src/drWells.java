import processing.core.PImage;

import java.util.List;
import java.util.Optional;

//turns into reverse flash when he starts moving
public class drWells extends Moved{

        public drWells(String id, Point position,List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id,position,images,actionPeriod,animationPeriod);
    }
    public static drWells createdrWells(String id, Point position,
        int actionPeriod, int animationPeriod, List<PImage> images)
        {
            return new drWells( id, position, images, actionPeriod, animationPeriod);
        }



    public  void execute( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> drWellsTarget = world.findNearest( this.getPosition(), Oreblob.class);
        long nextPeriod = this.getActionPeriod();

        if (drWellsTarget.isPresent())
        {
            Point tgtPos = drWellsTarget.get().getPosition();

            if (moveTo(world, drWellsTarget.get(), scheduler))
            {
                Entity quake = Quake.createQuake(tgtPos, imageStore.getImageList(Oreblob.QUAKE_KEY));

                world.addEntity( quake);
                nextPeriod += this.getActionPeriod();
                ((Quake)quake).scheduleActions( scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public boolean moveTo( WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents( target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition( world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

}