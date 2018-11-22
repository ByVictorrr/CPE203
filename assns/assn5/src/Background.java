import java.util.List;
import processing.core.PImage;

final class Background
{
   public String id;
   public List<PImage> images;
   public int imageIndex;
   public Point position;

   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }

   public PImage getCurrentImage()
   {
    return images.get(imageIndex);
   }

   /*public static PImage getCurrentImage(Object entity) {
      if (entity instanceof Background) {
         return ((Background) entity).images
                 .get(((Background) entity).imageIndex);
      } else if (entity instanceof Entity) {
         return ((Entity) entity).getImages().get(((Entity) entity).getImageIndex());
      } else {
         throw new UnsupportedOperationException(
                 String.format("getCurrentImage not supported for %s",
                         entity));
      }
   }
   */
}
