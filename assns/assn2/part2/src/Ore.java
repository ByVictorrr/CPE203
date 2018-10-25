import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Ore{




    public static final String ORE_ID_PREFIX = "ore -- ";
    public static final int ORE_CORRUPT_MIN = 20000;
    public static final int ORE_CORRUPT_MAX = 30000;
    public static final int ORE_REACH = 1;




    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public static Entity createOre(String id, Point position, int actionPeriod,
                                   List<PImage> images)
    {
        return new Entity(EntityKind.ORE, id, position, images, 0, 0,
                actionPeriod, 0);
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


    public void executeOreActivity(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity blob = createOreBlob(this.id + BLOB_ID_SUFFIX,
                pos, this.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        Functions.rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                Functions.getImageList(imageStore, BLOB_KEY));

        world.addEntity( blob);
        blob.scheduleActions(scheduler, world, imageStore);
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