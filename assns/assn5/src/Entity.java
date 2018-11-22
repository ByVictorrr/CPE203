import java.util.List;
import java.util.Optional;

import processing.core.PImage;

abstract public class Entity
{

private Point position;
private List<PImage> images;
private int imageIndex;
private int actionPeriod;
private int animationPeriod;
private String id;


    public Entity( String id, Point position,
                      List<PImage> images, int actionPeriod, int animationPeriod)
    {
        this.id=id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public int getActionPeriod(){return actionPeriod;}
    public List<PImage> getImages() { return this.images;}
    public void setActionPeriod(int ap){actionPeriod = ap;}
    public void setAnimationPeriod(int ap){animationPeriod = ap;}
    public void setPosition(Point p) { this.position = p;}
    public void setImages(List<PImage> images){this.images=images;}
    public int getImageIndex(){return imageIndex;}

    public Point getPosition() { return this.position;}

    public int getAnimationPeriod() {
        return animationPeriod;
    }
    public String getId(){return id;}
    public void nextImage()
    {
        this.imageIndex = (this.getImageIndex()+ 1) % this.getImages().size();
    }






}
