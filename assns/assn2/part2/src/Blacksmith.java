import processing.core.PImage;

import java.util.List;

public class Blacksmith implements Entity{


    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;




    public Blacksmith( String id, Point position,
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





    public static Blacksmith createBlacksmith(String id, Point position,
                                          List<PImage> images)
    {
        return new Blacksmith( id, position, images,
                0, 0, 0, 0);
    }

    public Point getPosition() { return this.position;}
    public List<PImage> getImages() { return this.images;}
    public int getImageIndex() { return this.imageIndex;}
    public int getResourceLimit() { return this.resourceLimit;}
    public int getResourceCount() { return this.resourceCount;}
    public int getActionPeriod() { return this.actionPeriod;}
    //public int getAnimationPeriod() { return this.animationPeriod;}



    //setters
    public void setPosition(Point p) { this.position = p;}
    public void setImages(List<PImage> i) { this.images =i; }
    public void setImageIndex(int i) { this.imageIndex = i;}
    public void setResourceLimit(int r) { this.resourceLimit = r;}
    public void setResourceCount(int r) { this.resourceCount=r;}
    public void setActionPeriod(int a) { this.actionPeriod = a;}
    public void setAnimationPeriod(int a) {this.animationPeriod = a;}






    public int getAnimationPeriod()
    {

                return this.animationPeriod;

    }

    //edited
    public void nextImage()
    {
        this.imageIndex = (this.getImageIndex()+ 1) % this.getImages().size();
    }



    public void scheduleActions( EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent((Entity) this,
                Activity.createActivityAction(this, world, imageStore),
                this.actionPeriod);

        scheduler.scheduleEvent(this, new Animation(this,null,null) );
    }




}