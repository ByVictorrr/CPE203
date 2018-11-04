public interface Executable extends Entity{

    void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    void  scheduleActions( EventScheduler scheduler, WorldModel world, ImageStore imageStore);
}




