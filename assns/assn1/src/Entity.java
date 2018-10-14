import java.util.List;
import java.util.Optional;

import processing.core.PImage;

final class Entity
{

   public static final String BLOB_KEY = "blob";
   public static final String BLOB_ID_SUFFIX = " -- blob";
   public static final int BLOB_PERIOD_SCALE = 4;
   public static final int BLOB_ANIMATION_MIN = 50;
   public static final int BLOB_ANIMATION_MAX = 150;

   public static final String ORE_ID_PREFIX = "ore -- ";
   public static final int ORE_CORRUPT_MIN = 20000;
   public static final int ORE_CORRUPT_MAX = 30000;
   public static final int ORE_REACH = 1;

   public static final String QUAKE_KEY = "quake";
   public static final String QUAKE_ID = "quake";
   public static final int QUAKE_ACTION_PERIOD = 1100;
   public static final int QUAKE_ANIMATION_PERIOD = 100;
   public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
   public static final String ORE_KEY = "ore";


   private EntityKind kind;
   private String id;
   private Point position;
   private List<PImage> images;
   private int imageIndex;
   private int resourceLimit;
   private int resourceCount;
   private int actionPeriod;
   private int animationPeriod;

   public Entity(EntityKind kind, String id, Point position,
      List<PImage> images, int resourceLimit, int resourceCount,
      int actionPeriod, int animationPeriod)
   {
      this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }

//getters
   public EntityKind getEntityKind() { return this.kind;}
  // public String getID() { return this.id;}
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




//edited

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

   public  void executeOreBlobActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> blobTarget = world.findNearest( this.position, EntityKind.VEIN);
      long nextPeriod = this.actionPeriod;

      if (blobTarget.isPresent())
      {
         Point tgtPos = blobTarget.get().position;

         if (moveToOreBlob(this, world, blobTarget.get(), scheduler))
         {
            Entity quake = createQuake(tgtPos,
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

   public void executeQuakeActivity( WorldModel world,
                                           ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents( this);
      world.removeEntity(this);
   }

   public void executeVeinActivity( WorldModel world,
                                          ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = world.findOpenAround(this.position);

      if (openPt.isPresent())
      {
         Entity ore = createOre(ORE_ID_PREFIX + this.id,
                 openPt.get(), ORE_CORRUPT_MIN +
                         Functions.rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                 Functions.getImageList(imageStore, ORE_KEY));
         world.addEntity(ore);
         ore.scheduleActions( scheduler, world, imageStore);
      }

      scheduler.scheduleEvent( this,
              createActivityAction(this, world, imageStore),
              this.actionPeriod);
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



    public boolean adjacent(Point p1, Point p2) {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
                (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }

   public  boolean moveToNotFull(Entity miner, WorldModel world, Entity target, EventScheduler scheduler)
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

   public boolean moveToOreBlob(Entity blob, WorldModel world,
                                       Entity target, EventScheduler scheduler)
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


   public static Action createAnimationAction(Entity entity, int repeatCount)
   {
      return new Action(ActionKind.ANIMATION, entity, null, null, repeatCount);
   }

   public static Action createActivityAction(Entity entity, WorldModel world,
                                             ImageStore imageStore)
   {
      return new Action(ActionKind.ACTIVITY, entity, world, imageStore, 0);
   }

   public static Entity createBlacksmith(String id, Point position,
                                         List<PImage> images)
   {
      return new Entity(EntityKind.BLACKSMITH, id, position, images,
              0, 0, 0, 0);
   }

   public static Entity createMinerFull(String id, int resourceLimit,
                                        Point position, int actionPeriod, int animationPeriod,
                                        List<PImage> images)
   {
      return new Entity(EntityKind.MINER_FULL, id, position, images,
              resourceLimit, resourceLimit, actionPeriod, animationPeriod);
   }

   public static Entity createMinerNotFull(String id, int resourceLimit,
                                           Point position, int actionPeriod, int animationPeriod,
                                           List<PImage> images)
   {
      return new Entity(EntityKind.MINER_NOT_FULL, id, position, images,
              resourceLimit, 0, actionPeriod, animationPeriod);
   }

   public static Entity createObstacle(String id, Point position,
                                       List<PImage> images)
   {
      return new Entity(EntityKind.OBSTACLE, id, position, images,
              0, 0, 0, 0);
   }

   public static Entity createOre(String id, Point position, int actionPeriod,
                              List<PImage> images)
   {
      return new Entity(EntityKind.ORE, id, position, images, 0, 0,
              actionPeriod, 0);
   }

   public static Entity createOreBlob(String id, Point position,
                                      int actionPeriod, int animationPeriod, List<PImage> images)
   {
      return new Entity(EntityKind.ORE_BLOB, id, position, images,
              0, 0, actionPeriod, animationPeriod);
   }

   public static Entity createQuake(Point position, List<PImage> images)
   {
      return new Entity(EntityKind.QUAKE, QUAKE_ID, position, images,
              0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
   }

   public static Entity createVein(String id, Point position, int actionPeriod,
                                   List<PImage> images) {
      return new Entity(EntityKind.VEIN, id, position, images, 0, 0,
              actionPeriod, 0);
   }



}
