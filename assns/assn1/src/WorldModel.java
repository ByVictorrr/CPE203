import processing.core.PImage;

import java.util.*;

final class WorldModel
{
   public int numRows;
   public int numCols;
   public Background background[][];
   public Entity occupancy[][];
   public Set<Entity> entities;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
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




}
