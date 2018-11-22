import processing.core.PImage;

import java.util.List;
import java.util.Optional;

//turns into reverse flash when he starts moving
public class ReverseFlash extends Moved{
    private static final String GORILLA_KEY = "gorilla";

        public ReverseFlash(String id, Point position,List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id,position,images,actionPeriod,animationPeriod);
    }
    public static ReverseFlash createReverseFlash(String id, Point position,
        int actionPeriod, int animationPeriod, List<PImage> images)
        {
            return new ReverseFlash( id, position, images, actionPeriod, animationPeriod);
        }



    public  void execute( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> ReverseFlashTarget = world.findNearest( this.getPosition(), Oreblob.class);
        long nextPeriod = this.getActionPeriod();

        if (ReverseFlashTarget.isPresent())
        {
            Point tgtPos = ReverseFlashTarget.get().getPosition();

            if (moveTo(world, ReverseFlashTarget.get(), scheduler, imageStore))
            {
               /*Entity quake = Quake.createQuake(tgtPos, imageStore.getImageList(Oreblob.QUAKE_KEY));
                world.addEntity( quake);

                nextPeriod += this.getActionPeriod();
                ((Quake)quake).scheduleActions( scheduler, world, imageStore);
                */
            }
        }

        scheduler.scheduleEvent( this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }



    public boolean moveTo( WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore) {

            if (Point.adjacent(this.getPosition(), target.getPosition()) && world.getOccupant(target.getPosition()).isPresent()) {

                Oreblob gorilla = (Oreblob) (world.getOccupant(target.getPosition()).get());

                //transforming the blob adjacent to reverse flash into a gorilla
                gorilla.setImageIndex(3);
                gorilla.setImages(imageStore.getImageList(GORILLA_KEY));
                gorilla.setAnimationPeriod(5);
                gorilla.setActionPeriod(5);

                return true;

        }
        else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
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

