import processing.core.PImage;

import java.util.*;

final class WorldModel
{
   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;

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

   public  boolean withinBounds(  Point pos)
   {
      return pos.y >= 0 && pos.y < this.numRows &&
              pos.x >= 0 && pos.x < this.numCols;
   }

   public  boolean isOccupied(  Point pos)
   {
      return withinBounds( pos) &&
              getOccupancyCell( pos) != null;
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
         int nearestDistance = distanceSquared(nearest.getPosition(), pos);

         for (Entity other : entities)
         {
            int otherDistance = distanceSquared(other.getPosition(), pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   public  int distanceSquared(Point p1, Point p2)
   {
      int deltaX = p1.x - p2.x;
      int deltaY = p1.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }

   public Optional<Entity> findNearest(  Point pos, EntityKind kind)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : entities)
      {
         if (entity.getEntityKind()== kind)
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
         return Optional.of(Functions.getCurrentImage(getBackgroundCell( pos)));
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
      return occupancy[pos.y][pos.x];
   }

   public  void setOccupancyCell(  Point pos,
                                       Entity entity)
   {
      occupancy[pos.y][pos.x] = entity;
   }

   public  Background getBackgroundCell(  Point pos)
   {
      return background[pos.y][pos.x];
   }

   public void setBackgroundCell(  Point pos, Background background)
   {
      this.background[pos.y][pos.x] = background;
   }


   public Optional<Point> findOpenAround( Point pos)
   {
      for (int dy = -Entity.ORE_REACH; dy <= Entity.ORE_REACH; dy++)
      {
         for (int dx = -Entity.ORE_REACH; dx <= Entity.ORE_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
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
