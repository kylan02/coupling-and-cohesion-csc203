public class Activity extends Action{
    private WorldModel world;
    private ImageStore imageStore;


    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        super(entity);
        this.world = world;
        this.imageStore = imageStore;
    }


    public void executeAction(EventScheduler scheduler) {
        if (this.getEntity() instanceof Active){
            ((Active)this.getEntity()).executeActivity(this.world, this.imageStore, scheduler);
        }
//        switch (super.getEntity().getKind()) {
//            case SAPLING:
//                super.getEntity().executeSaplingActivity(this.world, this.imageStore, scheduler);
//                break;
//            case TREE:
//                super.getEntity().executeTreeActivity( this.world, this.imageStore, scheduler);
//                break;
//            case FAIRY:
//                super.getEntity().executeFairyActivity(this.world, this.imageStore, scheduler);
//                break;
//            case DUDE_NOT_FULL:
//                super.getEntity().executeDudeNotFullActivity(this.world, this.imageStore, scheduler);
//                break;
//            case DUDE_FULL:
//                super.getEntity().executeDudeFullActivity( this.world, this.imageStore, scheduler);
//                break;
//            default:
//                throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s",
//                        super.getEntity().getKind()));
//        }
    }
}
