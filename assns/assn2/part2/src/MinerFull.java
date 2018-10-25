import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull{

    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public static Entity createMinerFull(String id, int resourceLimit,
                                         Point position, int actionPeriod, int animationPeriod,
                                         List<PImage> images)
    {
        return new Entity(EntityKind.MINER_FULL, id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }


    public Point getPosition() { return this.position;}
    public List<PImage> getImages() { return this.images;}
    public int getImageIndex() { return this.imageIndex;}
    //public int getResourceLimit() { return this.resourceLimit;}
    //public int getResourceCount() { return this.resourceCount;}
    // public int getActionPeriod() { return this.actionPeriod;}
    //public int getAnimationPeriod() { return this.animationPeriod;}



    //setters
    //public void setEntityKind(EntityKind k) {  this.kind =k;}
    //public void setID(String i ) {  this.id = i;}
    public void setPosition(Point p) { this.position = p;}
    public void setImages(List<PImage> i) { this.images =i; }
    //public void setImageIndex(int i) { this.imageIndex = i;}
    //public void setResourceLimit(int r) { this.resourceLimit = r;}
    //public void setResourceCount(int r) { this.resourceCount=r;}
    //public void setActionPeriod(int a) { this.actionPeriod = a;}
    //public void setAnimationPeriod(int a) {this.animationPeriod = a;}



    public static Action createAnimationAction(Entity entity, int repeatCount)
    {
        return new Action(ActionKind.ANIMATION, entity, null, null, repeatCount);
    }

    public static Action createActivityAction(Entity entity, WorldModel world,
                                              ImageStore imageStore)
    {
        return new Action(ActionKind.ACTIVITY, entity, world, imageStore, 0);
    }


    public void executeMinerFullActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.position, EntityKind.BLACKSMITH);

        if (fullTarget.isPresent() &&
                moveToFull(this, world, fullTarget.get(), scheduler))
        {
            this.transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void transformFull( WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore)
    {
        Entity miner = createMinerNotFull(this.id, this.resourceLimit,
                this.position, this.actionPeriod, this.animationPeriod,
                this.images);

        world.removeEntity( this);
        scheduler.unscheduleAllEvents( this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
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


    public  boolean moveToFull(Entity miner, WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (adjacent(miner.position, target.position))
        {
            return true;
        }
        else
        {
            Point nextPos = miner.nextPositionMiner( world, target.position);

            if (!miner.position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents( occupant.get());
                }

                world.moveEntity( miner, nextPos);
            }
            return false;
        }
    }



    public Optional<Point> findOpenAround(Point pos)
    {
        for (int dy = -Entity.ORE_REACH; dy <= Entity.ORE_REACH; dy++)
        {
            for (int dx = -Entity.ORE_REACH; dx <= Entity.ORE_REACH; dx++)
            {
                Point newPt = new Point(pos.x + dx, pos.y + dy);
                if (withinBounds( newPt) &&
                        !isOccupied( newPt))
                {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
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