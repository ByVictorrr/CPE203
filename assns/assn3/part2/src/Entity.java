import java.util.List;
import java.util.Optional;

import processing.core.PImage;

abstract public class Entity
{

protected Point position;
protected List<PImage> images;
protected int imageIndex;
protected int resourceLimit;
protected int resourceCount;
protected int actionPeriod;
protected int animationPeriod;
protected String id;


    public Entity( String id, Point position,
                      List<PImage> images, int resourceLimit, int resourceCount,
                      int actionPeriod, int animationPeriod)
    {
        this.id=id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public List<PImage> getImages() { return this.images;}

    public void setPosition(Point p) { this.position = p;}

    int getImageIndex(){return imageIndex;}

    public Point getPosition() { return this.position;}

    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }

    //edited
    public void nextImage()
    {
        this.imageIndex = (this.getImageIndex()+ 1) % this.getImages().size();
    }






}
