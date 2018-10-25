import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull{

    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public static Entity createMinerNotFull(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images)
    {
        return new Entity(EntityKind.MINER_NOT_FULL, id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }



    public Point nextPositionMiner( WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        if (horiz == 0 || world.isOccupied( newPos))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x,
                    this.position.y + vert);

            if (vert == 0 || world.isOccupied( newPos))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }



    public boolean transformNotFull( WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit)
        {
            Entity miner = createMinerFull(this.id, this.resourceLimit,
                    this.position, this.actionPeriod, this.animationPeriod,
                    this.images);

            world.removeEntity( this);
            scheduler.unscheduleAllEvents( this);

            world.addEntity( miner);
            miner.scheduleActions( scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public boolean moveToNotFull(Entity miner, WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (adjacent(miner.position, target.position))
        {
            miner.resourceCount += 1;
            world.removeEntity( target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = miner.nextPositionMiner( world, target.position);

            if (!miner.position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity( miner, nextPos);
            }
            return false;
        }
    }





    public  void executeMinerNotFullActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest( this.position, EntityKind.ORE);

        if (!notFullTarget.isPresent() ||
                !moveToNotFull(this, world, notFullTarget.get(), scheduler) ||
                !transformNotFull( world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }


    public int getAnimationPeriod()
    {
        switch (this.getEntityKind())
        {
            case MINER_FULL:
            case MINER_NOT_FULL:
            case ORE_BLOB:
            case QUAKE:
                return this.animationPeriod;
            default:
                throw new UnsupportedOperationException(
                        String.format("getAnimationPeriod not supported for %s", this.getEntityKind()));
        }
    }

    //edited
    public void nextImage()
    {
        this.imageIndex = (this.getImageIndex()+ 1) % this.getImages().size();
    }







    public void scheduleActions( EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        switch (this.kind)
        {
            case MINER_FULL:
                scheduler.scheduleEvent(this,
                        createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent(this, createAnimationAction(this, 0),
                        this.getAnimationPeriod());
                break;

            case MINER_NOT_FULL:
                scheduler.scheduleEvent( this,
                        createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent( this,
                        createAnimationAction(this, 0), this.getAnimationPeriod());
                break;

            case ORE:
                scheduler.scheduleEvent( this,
                        createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                break;

            case ORE_BLOB:
                scheduler.scheduleEvent( this,
                        createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent( this,
                        createAnimationAction(this, 0), this.getAnimationPeriod());
                break;

            case QUAKE:
                scheduler.scheduleEvent(this,
                        createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent( this,
                        createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),
                        this.getAnimationPeriod());
                break;

            case VEIN:
                scheduler.scheduleEvent( this,
                        createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                break;

            default:
        }
    }
}

