final class Action
{
   public ActionKind kind;
   public Entity entity;
   public WorldModel world;
   public ImageStore imageStore;
   public int repeatCount;

   public Action(ActionKind kind, Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.kind = kind;
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }
}
