import processing.core.PImage;
import sun.util.resources.cldr.es.CalendarData_es_NI;

import java.util.List;
import java.util.Optional;

public class MinerNotFull implements Moveable{

    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;
    private String id;


    public MinerNotFull(String id, Point position,
                        List<PImage> images, int resourceLimit, int resourceCount,
                        int actionPeriod, int animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public static MinerNotFull createMinerNotFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        return new MinerNotFull(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
    }





    public Point getPosition() { return this.position;}
    public List <PImage> getImages() { return this.images;}
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



    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x,
                    this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }


    public boolean transformNotFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {

            Entity miner = MinerFull.createMinerFull(this.id, this.resourceLimit,
                    this.position, this.actionPeriod, this.animationPeriod,
                    this.images);

            world.removeEntity((Entity) this);
            scheduler.unscheduleAllEvents((Entity) this);

            world.addEntity(miner);

            ((MinerFull)miner).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public boolean moveToNotFull(WorldModel world, Entity target, EventScheduler scheduler) {

        if (Point.adjacent(this.position, target.getPosition())) {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity((Entity) this, nextPos);
            }
            return false;
        }
    }


    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Optional<Entity> notFullTarget = world.findNearest(this.position, Ore.class);

        if (!notFullTarget.isPresent() ||
                !moveToNotFull(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore)) {
            scheduler.scheduleEvent((Entity) this,
                    Activity.createActivityAction((Entity) this, world, imageStore),
                    this.actionPeriod);
        }
    }


    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    //edited
    public void nextImage() {
        this.imageIndex = (this.getImageIndex() + 1) % this.getImages().size();
    }



    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {


        scheduler.scheduleEvent( this, Activity.createActivityAction( this,  world, imageStore), this.actionPeriod);

        scheduler.scheduleEvent(  this, (Action)Animation.createAnimationAction( this, 0), this.getAnimationPeriod());

    }

}