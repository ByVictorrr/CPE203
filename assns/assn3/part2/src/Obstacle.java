import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity{



    public Obstacle(String id, Point position,
                        List<PImage> images, int resourceLimit, int resourceCount,
                        int actionPeriod, int animationPeriod) {

        super(id,position,images,resourceLimit, resourceCount,actionPeriod,animationPeriod);
    }

    public static Obstacle createObstacle(String id, Point position,
                                        List<PImage> images)
    {
        return new Obstacle( id, position, images,
                0, 0, 0, 0);
    }









}