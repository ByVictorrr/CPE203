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

   public static final int COLOR_MASK = 0xffffff;
   public static final int KEYED_IMAGE_MIN = 5;
   private static final int KEYED_RED_IDX = 2;
   private static final int KEYED_GREEN_IDX = 3;
   private static final int KEYED_BLUE_IDX = 4;

   public static final int PROPERTY_KEY = 0;

   public static final String BGND_KEY = "background";
   public static final int BGND_NUM_PROPERTIES = 4;
   public static final int BGND_ID = 1;
   public static final int BGND_COL = 2;
   public static final int BGND_ROW = 3;

   public static final String MINER_KEY = "miner";
   public static final int MINER_NUM_PROPERTIES = 7;
   public static final int MINER_ID = 1;
   public static final int MINER_COL = 2;
   public static final int MINER_ROW = 3;
   public static final int MINER_LIMIT = 4;
   public static final int MINER_ACTION_PERIOD = 5;
   public static final int MINER_ANIMATION_PERIOD = 6;

   public static final String OBSTACLE_KEY = "obstacle";
   public static final int OBSTACLE_NUM_PROPERTIES = 4;
   public static final int OBSTACLE_ID = 1;
   public static final int OBSTACLE_COL = 2;
   public static final int OBSTACLE_ROW = 3;

   public static final String ORE_KEY = "ore";
   public static final int ORE_NUM_PROPERTIES = 5;
   public static final int ORE_ID = 1;
   public static final int ORE_COL = 2;
   public static final int ORE_ROW = 3;
   public static final int ORE_ACTION_PERIOD = 4;

   public static final String SMITH_KEY = "blacksmith";
   public static final int SMITH_NUM_PROPERTIES = 4;
   public static final int SMITH_ID = 1;
   public static final int SMITH_COL = 2;
   public static final int SMITH_ROW = 3;

   public static final String VEIN_KEY = "vein";
   public static final int VEIN_NUM_PROPERTIES = 5;
   public static final int VEIN_ID = 1;
   public static final int VEIN_COL = 2;
   public static final int VEIN_ROW = 3;
   public static final int VEIN_ACTION_PERIOD = 4;

   public EntityKind kind;
   public String id;
   public Point position;
   public List<PImage> images;
   public int imageIndex;
   public int resourceLimit;
   public int resourceCount;
   public int actionPeriod;
   public int animationPeriod;

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



   public static PImage getCurrentImage(Object entity)
   {
      if (entity instanceof Background)
      {
         return ((Background)entity).images
                 .get(((Background)entity).imageIndex);
      }
      else if (entity instanceof Entity)
      {
         return ((Entity)entity).images.get(((Entity)entity).imageIndex);
      }
      else
      {
         throw new UnsupportedOperationException(
                 String.format("getCurrentImage not supported for %s",
                         entity));
      }
   }



   public static int getAnimationPeriod(Entity entity)
   {
      switch (entity.kind)
      {
         case MINER_FULL:
         case MINER_NOT_FULL:
         case ORE_BLOB:
         case QUAKE:
            return entity.animationPeriod;
         default:
            throw new UnsupportedOperationException(
                    String.format("getAnimationPeriod not supported for %s",
                            entity.kind));
      }
   }

   public static void nextImage(Entity entity)
   {
      entity.imageIndex = (entity.imageIndex + 1) % entity.images.size();
   }



   public static int getAnimationPeriod(Entity entity)
   {
      switch (entity.kind)
      {
         case MINER_FULL:
         case MINER_NOT_FULL:
         case ORE_BLOB:
         case QUAKE:
            return entity.animationPeriod;
         default:
            throw new UnsupportedOperationException(
                    String.format("getAnimationPeriod not supported for %s",
                            entity.kind));
      }
   }

   public static void nextImage(Entity entity)
   {
      entity.imageIndex = (entity.imageIndex + 1) % entity.images.size();
   }



   public static void executeMinerFullActivity(Entity entity, WorldModel world,
                                               ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> fullTarget = findNearest(world, entity.position,
              EntityKind.BLACKSMITH);

      if (fullTarget.isPresent() &&
              moveToFull(entity, world, fullTarget.get(), scheduler))
      {
         transformFull(entity, world, scheduler, imageStore);
      }
      else
      {
         scheduleEvent(scheduler, entity,
                 createActivityAction(entity, world, imageStore),
                 entity.actionPeriod);
      }
   }

   public static void executeMinerNotFullActivity(Entity entity,
                                                  WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> notFullTarget = findNearest(world, entity.position,
              EntityKind.ORE);

      if (!notFullTarget.isPresent() ||
              !moveToNotFull(entity, world, notFullTarget.get(), scheduler) ||
              !transformNotFull(entity, world, scheduler, imageStore))
      {
         scheduleEvent(scheduler, entity,
                 createActivityAction(entity, world, imageStore),
                 entity.actionPeriod);
      }
   }

   public static void executeOreActivity(Entity entity, WorldModel world,
                                         ImageStore imageStore, EventScheduler scheduler)
   {
      Point pos = entity.position;  // store current position before removing

      removeEntity(world, entity);
      unscheduleAllEvents(scheduler, entity);

      Entity blob = createOreBlob(entity.id + BLOB_ID_SUFFIX,
              pos, entity.actionPeriod / BLOB_PERIOD_SCALE,
              BLOB_ANIMATION_MIN +
                      rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
              getImageList(imageStore, BLOB_KEY));

      addEntity(world, blob);
      scheduleActions(blob, scheduler, world, imageStore);
   }

   public static void executeOreBlobActivity(Entity entity, WorldModel world,
                                             ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> blobTarget = findNearest(world,
              entity.position, EntityKind.VEIN);
      long nextPeriod = entity.actionPeriod;

      if (blobTarget.isPresent())
      {
         Point tgtPos = blobTarget.get().position;

         if (moveToOreBlob(entity, world, blobTarget.get(), scheduler))
         {
            Entity quake = createQuake(tgtPos,
                    getImageList(imageStore, QUAKE_KEY));

            addEntity(world, quake);
            nextPeriod += entity.actionPeriod;
            scheduleActions(quake, scheduler, world, imageStore);
         }
      }

      scheduleEvent(scheduler, entity,
              createActivityAction(entity, world, imageStore),
              nextPeriod);
   }

   public static void executeQuakeActivity(Entity entity, WorldModel world,
                                           ImageStore imageStore, EventScheduler scheduler)
   {
      unscheduleAllEvents(scheduler, entity);
      removeEntity(world, entity);
   }

   public static void executeVeinActivity(Entity entity, WorldModel world,
                                          ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = findOpenAround(world, entity.position);

      if (openPt.isPresent())
      {
         Entity ore = createOre(ORE_ID_PREFIX + entity.id,
                 openPt.get(), ORE_CORRUPT_MIN +
                         rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                 getImageList(imageStore, ORE_KEY));
         addEntity(world, ore);
         scheduleActions(ore, scheduler, world, imageStore);
      }

      scheduleEvent(scheduler, entity,
              createActivityAction(entity, world, imageStore),
              entity.actionPeriod);
   }

   public static void scheduleActions(Entity entity, EventScheduler scheduler,
                                      WorldModel world, ImageStore imageStore)
   {
      switch (entity.kind)
      {
         case MINER_FULL:
            scheduleEvent(scheduler, entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
            scheduleEvent(scheduler, entity, createAnimationAction(entity, 0),
                    getAnimationPeriod(entity));
            break;

         case MINER_NOT_FULL:
            scheduleEvent(scheduler, entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
            scheduleEvent(scheduler, entity,
                    createAnimationAction(entity, 0), getAnimationPeriod(entity));
            break;

         case ORE:
            scheduleEvent(scheduler, entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
            break;

         case ORE_BLOB:
            scheduleEvent(scheduler, entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
            scheduleEvent(scheduler, entity,
                    createAnimationAction(entity, 0), getAnimationPeriod(entity));
            break;

         case QUAKE:
            scheduleEvent(scheduler, entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
            scheduleEvent(scheduler, entity,
                    createAnimationAction(entity, QUAKE_ANIMATION_REPEAT_COUNT),
                    getAnimationPeriod(entity));
            break;

         case VEIN:
            scheduleEvent(scheduler, entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
            break;

         default:
      }
   }

   public static boolean transformNotFull(Entity entity, WorldModel world,
                                          EventScheduler scheduler, ImageStore imageStore)
   {
      if (entity.resourceCount >= entity.resourceLimit)
      {
         Entity miner = createMinerFull(entity.id, entity.resourceLimit,
                 entity.position, entity.actionPeriod, entity.animationPeriod,
                 entity.images);

         removeEntity(world, entity);
         unscheduleAllEvents(scheduler, entity);

         addEntity(world, miner);
         scheduleActions(miner, scheduler, world, imageStore);

         return true;
      }

      return false;
   }

   public static void transformFull(Entity entity, WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore)
   {
      Entity miner = createMinerNotFull(entity.id, entity.resourceLimit,
              entity.position, entity.actionPeriod, entity.animationPeriod,
              entity.images);

      removeEntity(world, entity);
      unscheduleAllEvents(scheduler, entity);

      addEntity(world, miner);
      scheduleActions(miner, scheduler, world, imageStore);
   }

   public static boolean moveToNotFull(Entity miner, WorldModel world,
                                       Entity target, EventScheduler scheduler)
   {
      if (adjacent(miner.position, target.position))
      {
         miner.resourceCount += 1;
         removeEntity(world, target);
         unscheduleAllEvents(scheduler, target);

         return true;
      }
      else
      {
         Point nextPos = nextPositionMiner(miner, world, target.position);

         if (!miner.position.equals(nextPos))
         {
            Optional<Entity> occupant = getOccupant(world, nextPos);
            if (occupant.isPresent())
            {
               unscheduleAllEvents(scheduler, occupant.get());
            }

            moveEntity(world, miner, nextPos);
         }
         return false;
      }
   }

   public static boolean moveToFull(Entity miner, WorldModel world,
                                    Entity target, EventScheduler scheduler)
   {
      if (adjacent(miner.position, target.position))
      {
         return true;
      }
      else
      {
         Point nextPos = nextPositionMiner(miner, world, target.position);

         if (!miner.position.equals(nextPos))
         {
            Optional<Entity> occupant = getOccupant(world, nextPos);
            if (occupant.isPresent())
            {
               unscheduleAllEvents(scheduler, occupant.get());
            }

            moveEntity(world, miner, nextPos);
         }
         return false;
      }
   }

   public static boolean moveToOreBlob(Entity blob, WorldModel world,
                                       Entity target, EventScheduler scheduler)
   {
      if (adjacent(blob.position, target.position))
      {
         removeEntity(world, target);
         unscheduleAllEvents(scheduler, target);
         return true;
      }
      else
      {
         Point nextPos = nextPositionOreBlob(blob, world, target.position);

         if (!blob.position.equals(nextPos))
         {
            Optional<Entity> occupant = getOccupant(world, nextPos);
            if (occupant.isPresent())
            {
               unscheduleAllEvents(scheduler, occupant.get());
            }

            moveEntity(world, blob, nextPos);
         }
         return false;
      }
   }

   public static Point nextPositionMiner(Entity entity, WorldModel world,
                                         Point destPos)
   {
      int horiz = Integer.signum(destPos.x - entity.position.x);
      Point newPos = new Point(entity.position.x + horiz,
              entity.position.y);

      if (horiz == 0 || isOccupied(world, newPos))
      {
         int vert = Integer.signum(destPos.y - entity.position.y);
         newPos = new Point(entity.position.x,
                 entity.position.y + vert);

         if (vert == 0 || isOccupied(world, newPos))
         {
            newPos = entity.position;
         }
      }

      return newPos;
   }

   public static Point nextPositionOreBlob(Entity entity, WorldModel world,
                                           Point destPos)
   {
      int horiz = Integer.signum(destPos.x - entity.position.x);
      Point newPos = new Point(entity.position.x + horiz,
              entity.position.y);

      Optional<Entity> occupant = getOccupant(world, newPos);

      if (horiz == 0 ||
              (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
      {
         int vert = Integer.signum(destPos.y - entity.position.y);
         newPos = new Point(entity.position.x, entity.position.y + vert);
         occupant = getOccupant(world, newPos);

         if (vert == 0 ||
                 (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
         {
            newPos = entity.position;
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
                                   List<PImage> images)
   {
      return new Entity(EntityKind.VEIN, id, position, images, 0, 0,
              actionPeriod, 0);
   }
}


}
