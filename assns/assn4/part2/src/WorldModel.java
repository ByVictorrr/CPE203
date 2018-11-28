import processing.core.PImage;

import java.util.*;

final class WorldModel
{
   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;



   public static final String ORE_ID_PREFIX = "ore -- ";
   public static final int ORE_CORRUPT_MIN = 20000;
   public static final int ORE_CORRUPT_MAX = 30000;
   public static final int ORE_REACH = 1;




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
   //getters
   public int getNumRows(){return numRows;}
   public int getNumCols(){return numCols;}
   public Background [][] getBackground(){return background;}
   public Set<Entity> getEntities(){return entities;}


   public  void tryAddEntity( Entity entity)
   {
      if (isOccupied( entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity( entity);
   }

   public boolean withinBounds(  Point pos)
   {
      return pos.getY() >= 0 && pos.getY() < this.numRows &&
              pos.getX() >= 0 && pos.getX() < this.numCols;
   }

   public  boolean isOccupied(  Point pos)
   {
      return withinBounds( pos) && getOccupancyCell( pos) != null;
   }



   public  Optional<Entity> nearestEntity(List<Entity> entities, Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = Point.distanceSquared(nearest.getPosition(), pos);

         for (Entity other : entities)
         {
            int otherDistance = Point.distanceSquared(other.getPosition(), pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }



   public Optional<Entity> findNearest(  Point pos, Class type)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : entities)
      {
         if (entity.getClass() == type)
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }





   public  void addEntity(  Entity entity)
   {
      if (withinBounds( entity.getPosition()))
      {
         setOccupancyCell( entity.getPosition(), entity);
         entities.add(entity);
      }
   }

   public  void moveEntity(  Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (withinBounds( pos) && !pos.equals(oldPos))
      {
         setOccupancyCell( oldPos, null);
         removeEntityAt( pos);
         setOccupancyCell( pos, entity);
         entity.setPosition(pos);
      }
   }

   public  void removeEntity(  Entity entity)
   {
      removeEntityAt( entity.getPosition());
   }

   public void removeEntityAt(  Point pos)
   {
      if (withinBounds( pos)
              && getOccupancyCell( pos) != null)
      {
         Entity entity = getOccupancyCell( pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */

         entity.setPosition(new Point(-1, -1));

         entities.remove(entity);
         setOccupancyCell( pos, null);
      }
   }

   public  Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds( pos))
      {
         return Optional.of(getBackgroundCell( pos).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }

   public  void setBackground(  Point pos, Background background)
   {
      if (withinBounds( pos))
      {
         setBackgroundCell( pos, background);
      }
   }

   public  Optional<Entity> getOccupant(  Point pos)
   {
      if (isOccupied( pos))
      {
         return Optional.of(getOccupancyCell( pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public  Entity getOccupancyCell(  Point pos)
   {
      return occupancy[pos.getY()][pos.getX()];
   }

   public  void setOccupancyCell(  Point pos, Entity entity)
   {
      occupancy[pos.getY()][pos.getX()] = entity;
   }

   public  Background getBackgroundCell(  Point pos)
   {
      return background[pos.getY()][pos.getX()];
   }

   public void setBackgroundCell(  Point pos, Background background)
   {
      this.background[pos.getY()][pos.getX()] = background;
   }


   public Optional<Point> findOpenAround( Point pos)
   {
      for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
      {
         for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
         {
            Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
            if (withinBounds( newPt) &&
                    !isOccupied( newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }


}
