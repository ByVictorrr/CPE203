import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Oreblob{

    public static final String BLOB_KEY = "blob";
    public static final String BLOB_ID_SUFFIX = " -- blob";
    public static final int BLOB_PERIOD_SCALE = 4;
    public static final int BLOB_ANIMATION_MIN = 50;
    public static final int BLOB_ANIMATION_MAX = 150;



    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;


    public static Entity createOreBlob(String id, Point position,
                                       int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Entity(EntityKind.ORE_BLOB, id, position, images,
                0, 0, actionPeriod, animationPeriod);
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




    public boolean moveToOreBlob(Entity blob, WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (adjacent(blob.position, target.position))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents( target);
            return true;
        }
        else
        {
            Point nextPos = blob.nextPositionOreBlob( world, target.position);

            if (!blob.position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(blob, nextPos);
            }
            return false;
        }
    }



    public Point nextPositionOreBlob( WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        Optional<Entity> occupant = world.getOccupant( newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);
            occupant = world.getOccupant( newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }






    public  void executeOreBlobActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = world.findNearest( this.position, Vein.class);
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().position;

            if (moveToOreBlob(this, world, blobTarget.get(), scheduler))
            {
                Entity quake = Quake.createQuake(tgtPos,
                        Functions.getImageList(imageStore, QUAKE_KEY));

                world.addEntity( quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleActions( scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this,
                createActivityAction(this, world, imageStore),
                nextPeriod);
    }


    public int getAnimationPeriod()
    {

                return this.animationPeriod;

    }

    //edited
    public void nextImage()
    {
        this.imageIndex = (this.getImageIndex()+ 1) % this.getImages().size();




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