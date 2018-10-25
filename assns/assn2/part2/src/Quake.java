import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Quake{


    public static final String QUAKE_KEY = "quake";
    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;
    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    public static final String ORE_KEY = "ore";

    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public static Entity createQuake(Point position, List<PImage> images)
    {
        return new Entity(EntityKind.QUAKE, QUAKE_ID, position, images,
                0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
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



    public void executeQuakeActivity( WorldModel world,
                                      ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents( this);
        world.removeEntity(this);
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