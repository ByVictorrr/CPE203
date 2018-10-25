import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public interface Entity
{
   void nextImage();

   public int getAnimationPeriod();
   public PImage getImages();
   public Point getPosition();
   public void setPosition();
   public void executeActivity();
   public void scheduleActions( EventScheduler scheduler, WorldModel world, ImageStore imageStore);

   public int getImageIndex();







}
