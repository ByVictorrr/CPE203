import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public interface Entity
{





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


//edited
















}
