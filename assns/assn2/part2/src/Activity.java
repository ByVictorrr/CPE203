public class Activity implements Action{


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


    public void executeAction(EventScheduler scheduler) {
                executeActivityAction(scheduler);

    }

    public void executeAnimationAction(EventScheduler scheduler) {

            this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    this.entity.createAnimationAction(this.entity,
                            Math.max(this.repeatCount - 1, 0)),

                    this.entity.getAnimationPeriod());
        }
    }

    public void executeActivityAction(EventScheduler scheduler) {
        switch (this.entity.getEntityKind()) {
            case MINER_FULL:
                this.entity.executeMinerFullActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case MINER_NOT_FULL:
                this.entity.executeMinerNotFullActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case ORE:
                this.entity.executeOreActivity(this.world, this.imageStore,
                        scheduler);
                break;

            case ORE_BLOB:
                this.entity.executeOreBlobActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case QUAKE:
                this.entity.executeQuakeActivity(this.world, this.imageStore,
                        scheduler);
                break;

            case VEIN:
                this.entity.executeVeinActivity(this.world, this.imageStore,
                        scheduler);
                break;

            default:
                throw new UnsupportedOperationException(
                        String.format("executeActivityAction not supported for %s",
                                this.entity.getEntityKind()));
        }
    }

}