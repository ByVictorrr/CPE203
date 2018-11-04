import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Ore extends Activated{


    public static final String BLOB_KEY = "blob";
    public static final String BLOB_ID_SUFFIX = " -- blob";
    public static final int BLOB_PERIOD_SCALE = 4;
    public static final int BLOB_ANIMATION_MIN = 50;
    public static final int BLOB_ANIMATION_MAX = 150;






    public Ore(String id, Point position,
               List<PImage> images, int resourceLimit, int resourceCount,
               int actionPeriod, int animationPeriod) {

        super(id,position,images,resourceLimit, resourceCount,actionPeriod,animationPeriod);

    }

    public static Ore createOre(String id, Point position, int actionPeriod,
                                   List<PImage> images) {
        return new Ore(id, position, images, 0, 0,
                actionPeriod, 0);
    }



    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point pos = this.position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity blob = Oreblob.createOreBlob(this.id + BLOB_ID_SUFFIX,
                pos, this.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        Functions.rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                Functions.getImageList(imageStore, BLOB_KEY));

        world.addEntity(blob);
        ((Oreblob) blob).scheduleActions(scheduler, world, imageStore);
    }






}