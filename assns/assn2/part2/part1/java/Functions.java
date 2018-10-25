import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import processing.core.PImage;
import processing.core.PApplet;

final class Functions
{
   public static final Random rand = new Random();

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

   public static void executeAction(Action action, EventScheduler scheduler)
   {
      switch (action.kind)
      {
      case ACTIVITY:
         executeActivityAction(action, scheduler);
         break;

      case ANIMATION:
         executeAnimationAction(action, scheduler);
         break;
      }
   }

   public static void executeAnimationAction(Action action,
      EventScheduler scheduler)
   {
      nextImage(action.entity);

      if (action.repeatCount != 1)
      {
         scheduleEvent(scheduler, action.entity,
            createAnimationAction(action.entity,
               Math.max(action.repeatCount - 1, 0)),
            getAnimationPeriod(action.entity));
      }
   }

   public static void executeActivityAction(Action action,
      EventScheduler scheduler)
   {
      switch (action.entity.kind)
      {
      case MINER_FULL:
         executeMinerFullActivity(action.entity, action.world,
            action.imageStore, scheduler);
         break;

      case MINER_NOT_FULL:
         executeMinerNotFullActivity(action.entity, action.world,
            action.imageStore, scheduler);
         break;

      case ORE:
         executeOreActivity(action.entity, action.world, action.imageStore,
            scheduler);
         break;

      case ORE_BLOB:
         executeOreBlobActivity(action.entity, action.world,
            action.imageStore, scheduler);
         break;

      case QUAKE:
         executeQuakeActivity(action.entity, action.world, action.imageStore,
            scheduler);
         break;

      case VEIN:
         executeVeinActivity(action.entity, action.world, action.imageStore,
            scheduler);
         break;

      default:
         throw new UnsupportedOperationException(
            String.format("executeActivityAction not supported for %s",
            action.entity.kind));
      }
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

   public static boolean adjacent(Point p1, Point p2)
   {
      return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
         (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
   }

   public static Optional<Point> findOpenAround(WorldModel world, Point pos)
   {
      for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
      {
         for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (withinBounds(world, newPt) &&
               !isOccupied(world, newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public static void scheduleEvent(EventScheduler scheduler,
      Entity entity, Action action, long afterPeriod)
   {
      long time = System.currentTimeMillis() +
         (long)(afterPeriod * scheduler.timeScale);
      Event event = new Event(action, time, entity);

      scheduler.eventQueue.add(event);

      // update list of pending events for the given entity
      List<Event> pending = scheduler.pendingEvents.getOrDefault(entity,
         new LinkedList<>());
      pending.add(event);
      scheduler.pendingEvents.put(entity, pending);
   }

   public static void unscheduleAllEvents(EventScheduler scheduler,
      Entity entity)
   {
      List<Event> pending = scheduler.pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
            scheduler.eventQueue.remove(event);
         }
      }
   }

   public static void removePendingEvent(EventScheduler scheduler,
      Event event)
   {
      List<Event> pending = scheduler.pendingEvents.get(event.entity);

      if (pending != null)
      {
         pending.remove(event);
      }
   }

   public static void updateOnTime(EventScheduler scheduler, long time)
   {
      while (!scheduler.eventQueue.isEmpty() &&
         scheduler.eventQueue.peek().time < time)
      {
         Event next = scheduler.eventQueue.poll();
         
         removePendingEvent(scheduler, next);
         
         executeAction(next.action, scheduler);
      }
   }

   public static List<PImage> getImageList(ImageStore imageStore, String key)
   {
      return imageStore.images.getOrDefault(key, imageStore.defaultImages);
   }

   public static void loadImages(Scanner in, ImageStore imageStore,
      PApplet screen)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            processImageLine(imageStore.images, in.nextLine(), screen);
         }
         catch (NumberFormatException e)
         {
            System.out.println(String.format("Image format error on line %d",
               lineNumber));
         }
         lineNumber++;
      }
   }

   public static void processImageLine(Map<String, List<PImage>> images,
      String line, PApplet screen)
   {
      String[] attrs = line.split("\\s");
      if (attrs.length >= 2)
      {
         String key = attrs[0];
         PImage img = screen.loadImage(attrs[1]);
         if (img != null && img.width != -1)
         {
            List<PImage> imgs = getImages(images, key);
            imgs.add(img);

            if (attrs.length >= KEYED_IMAGE_MIN)
            {
               int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
               int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
               int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
               setAlpha(img, screen.color(r, g, b), 0);
            }
         }
      }
   }

   public static List<PImage> getImages(Map<String, List<PImage>> images,
      String key)
   {
      List<PImage> imgs = images.get(key);
      if (imgs == null)
      {
         imgs = new LinkedList<>();
         images.put(key, imgs);
      }
      return imgs;
   }

   /*
     Called with color for which alpha should be set and alpha value.
     setAlpha(img, color(255, 255, 255), 0));
   */
   public static void setAlpha(PImage img, int maskColor, int alpha)
   {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha)
         {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }

   public static void shift(Viewport viewport, int col, int row)
   {
      viewport.col = col;
      viewport.row = row;
   }

   public static boolean contains(Viewport viewport, Point p)
   {
      return p.y >= viewport.row && p.y < viewport.row + viewport.numRows &&
         p.x >= viewport.col && p.x < viewport.col + viewport.numCols;
   }

   public static void load(Scanner in, WorldModel world, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), world, imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                  lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
               lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
               lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   public static boolean processLine(String line, WorldModel world,
      ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
         case BGND_KEY:
            return parseBackground(properties, world, imageStore);
         case MINER_KEY:
            return parseMiner(properties, world, imageStore);
         case OBSTACLE_KEY:
            return parseObstacle(properties, world, imageStore);
         case ORE_KEY:
            return parseOre(properties, world, imageStore);
         case SMITH_KEY:
            return parseSmith(properties, world, imageStore);
         case VEIN_KEY:
            return parseVein(properties, world, imageStore);
         }
      }

      return false;
   }

   public static boolean parseBackground(String [] properties,
      WorldModel world, ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
            Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         setBackground(world, pt,
            new Background(id, getImageList(imageStore, id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   public static boolean parseMiner(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == MINER_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
            Integer.parseInt(properties[MINER_ROW]));
         Entity entity = createMinerNotFull(properties[MINER_ID],
            Integer.parseInt(properties[MINER_LIMIT]),
            pt,
            Integer.parseInt(properties[MINER_ACTION_PERIOD]),
            Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
            getImageList(imageStore, MINER_KEY));
         tryAddEntity(world, entity);
      }

      return properties.length == MINER_NUM_PROPERTIES;
   }

   public static boolean parseObstacle(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
            Integer.parseInt(properties[OBSTACLE_COL]),
            Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = createObstacle(properties[OBSTACLE_ID],
            pt, getImageList(imageStore, OBSTACLE_KEY));
         tryAddEntity(world, entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   public static boolean parseOre(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == ORE_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
            Integer.parseInt(properties[ORE_ROW]));
         Entity entity = createOre(properties[ORE_ID],
            pt, Integer.parseInt(properties[ORE_ACTION_PERIOD]),
            getImageList(imageStore, ORE_KEY));
         tryAddEntity(world, entity);
      }

      return properties.length == ORE_NUM_PROPERTIES;
   }

   public static boolean parseSmith(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == SMITH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
            Integer.parseInt(properties[SMITH_ROW]));
         Entity entity = createBlacksmith(properties[SMITH_ID],
            pt, getImageList(imageStore, SMITH_KEY));
         tryAddEntity(world, entity);
      }

      return properties.length == SMITH_NUM_PROPERTIES;
   }

   public static boolean parseVein(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == VEIN_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
            Integer.parseInt(properties[VEIN_ROW]));
         Entity entity = createVein(properties[VEIN_ID],
            pt,
            Integer.parseInt(properties[VEIN_ACTION_PERIOD]),
            getImageList(imageStore, VEIN_KEY));
         tryAddEntity(world, entity);
      }

      return properties.length == VEIN_NUM_PROPERTIES;
   }

   public static void tryAddEntity(WorldModel world, Entity entity)
   {
      if (isOccupied(world, entity.position))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(world, entity);
   }

   public static boolean withinBounds(WorldModel world, Point pos)
   {
      return pos.y >= 0 && pos.y < world.numRows &&
         pos.x >= 0 && pos.x < world.numCols;
   }

   public static boolean isOccupied(WorldModel world, Point pos)
   {
      return withinBounds(world, pos) &&
         getOccupancyCell(world, pos) != null;
   }

   public static Optional<Entity> nearestEntity(List<Entity> entities,
      Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = distanceSquared(nearest.position, pos);

         for (Entity other : entities)
         {
            int otherDistance = distanceSquared(other.position, pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   public static int distanceSquared(Point p1, Point p2)
   {
      int deltaX = p1.x - p2.x;
      int deltaY = p1.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }

   public static Optional<Entity> findNearest(WorldModel world, Point pos,
      EntityKind kind)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : world.entities)
      {
         if (entity.kind == kind)
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }

   /*
      Assumes that there is no entity currently occupying the
      intended destination cell.
   */
   public static void addEntity(WorldModel world, Entity entity)
   {
      if (withinBounds(world, entity.position))
      {
         setOccupancyCell(world, entity.position, entity);
         world.entities.add(entity);
      }
   }

   public static void moveEntity(WorldModel world, Entity entity, Point pos)
   {
      Point oldPos = entity.position;
      if (withinBounds(world, pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(world, oldPos, null);
         removeEntityAt(world, pos);
         setOccupancyCell(world, pos, entity);
         entity.position = pos;
      }
   }

   public static void removeEntity(WorldModel world, Entity entity)
   {
      removeEntityAt(world, entity.position);
   }

   public static void removeEntityAt(WorldModel world, Point pos)
   {
      if (withinBounds(world, pos)
         && getOccupancyCell(world, pos) != null)
      {
         Entity entity = getOccupancyCell(world, pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.position = new Point(-1, -1);
         world.entities.remove(entity);
         setOccupancyCell(world, pos, null);
      }
   }

   public static Optional<PImage> getBackgroundImage(WorldModel world,
      Point pos)
   {
      if (withinBounds(world, pos))
      {
         return Optional.of(getCurrentImage(getBackgroundCell(world, pos)));
      }
      else
      {
         return Optional.empty();
      }
   }

   public static void setBackground(WorldModel world, Point pos,
      Background background)
   {
      if (withinBounds(world, pos))
      {
         setBackgroundCell(world, pos, background);
      }
   }

   public static Optional<Entity> getOccupant(WorldModel world, Point pos)
   {
      if (isOccupied(world, pos))
      {
         return Optional.of(getOccupancyCell(world, pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public static Entity getOccupancyCell(WorldModel world, Point pos)
   {
      return world.occupancy[pos.y][pos.x];
   }

   public static void setOccupancyCell(WorldModel world, Point pos,
      Entity entity)
   {
      world.occupancy[pos.y][pos.x] = entity;
   }

   public static Background getBackgroundCell(WorldModel world, Point pos)
   {
      return world.background[pos.y][pos.x];
   }

   public static void setBackgroundCell(WorldModel world, Point pos,
      Background background)
   {
      world.background[pos.y][pos.x] = background;
   }

   public static Point viewportToWorld(Viewport viewport, int col, int row)
   {
      return new Point(col + viewport.col, row + viewport.row);
   }

   public static Point worldToViewport(Viewport viewport, int col, int row)
   {
      return new Point(col - viewport.col, row - viewport.row);
   }

   public static int clamp(int value, int low, int high)
   {
      return Math.min(high, Math.max(value, low));
   }

   public static void shiftView(WorldView view, int colDelta, int rowDelta)
   {
      int newCol = clamp(view.viewport.col + colDelta, 0,
         view.world.numCols - view.viewport.numCols);
      int newRow = clamp(view.viewport.row + rowDelta, 0,
         view.world.numRows - view.viewport.numRows);

      shift(view.viewport, newCol, newRow);
   }

   public static void drawBackground(WorldView view)
   {
      for (int row = 0; row < view.viewport.numRows; row++)
      {
         for (int col = 0; col < view.viewport.numCols; col++)
         {
            Point worldPoint = viewportToWorld(view.viewport, col, row);
            Optional<PImage> image = getBackgroundImage(view.world,
               worldPoint);
            if (image.isPresent())
            {
               view.screen.image(image.get(), col * view.tileWidth,
                  row * view.tileHeight);
            }
         }
      }
   }

   public static void drawEntities(WorldView view)
   {
      for (Entity entity : view.world.entities)
      {
         Point pos = entity.position;

         if (contains(view.viewport, pos))
         {
            Point viewPoint = worldToViewport(view.viewport, pos.x, pos.y);
            view.screen.image(getCurrentImage(entity),
               viewPoint.x * view.tileWidth, viewPoint.y * view.tileHeight);
         }
      }
   }

   public static void drawViewport(WorldView view)
   {
      drawBackground(view);
      drawEntities(view);
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
