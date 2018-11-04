






public class Activity extends Action{


    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public Activity( Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }




    public static Activity createActivityAction(Entity entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore, 0);
    }


    //make the excute function gneral to each enitty

    public void executeAction(EventScheduler scheduler) {


            if(this.entity instanceof MinerFull) {

                ((MinerFull) this.entity).execute(this.world,
                        this.imageStore, scheduler);
            }
        if(this.entity instanceof MinerNotFull) {

            ((MinerNotFull) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }
        //depending on the type of enitiy we will cast

        if(this.entity instanceof Ore) {

            ((Ore) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }
        if(this.entity instanceof Oreblob) {

            ((Oreblob) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }


       if(this.entity instanceof Quake) {

           ((Quake) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }


        if(this.entity instanceof Vein) {

            ((Vein)this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }


}

    }