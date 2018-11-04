import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public interface Entity
{
   void nextImage();

    int getAnimationPeriod();
    List <PImage> getImages();
    Point getPosition();
    void setPosition(Point p);

    int getImageIndex();







}
