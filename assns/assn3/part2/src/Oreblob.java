import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Oreblob extends Actioned{




    public static final String QUAKE_KEY = "quake";




    public Oreblob(String id, Point position,
               List<PImage> images, int resourceLimit, int resourceCount,
               int actionPeriod, int animationPeriod) {

        super(id,position,images,resourceLimit, resourceCount,actionPeriod,animationPeriod);

   }

    public static Oreblob createOreBlob(String id, Point position,
                                       int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Oreblob( id, position, images,
                0, 0, actionPeriod, animationPeriod);
    }





    public boolean moveToOreBlob( WorldModel world, Entity target, EventScheduler scheduler)
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

            if (!this.position.equals(nextPos))
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



    public Point nextPosition( WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        Optional<Entity> occupant = world.getOccupant( newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().getClass() == Ore.class )))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);
            occupant = world.getOccupant( newPos);

            if ((vert == 0) ||
                    (occupant.isPresent() && !(occupant.get().getClass() == Ore.class)))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }






    public  void execute( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = world.findNearest( this.position, Vein.class);
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveToOreBlob(world, blobTarget.get(), scheduler))
            {
                Entity quake = Quake.createQuake(tgtPos,

                        Functions.getImageList(imageStore, QUAKE_KEY));

                world.addEntity( quake);
                nextPeriod += this.actionPeriod;
                ((Quake)quake).scheduleActions( scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }






    }