public class Animation extends Action{
    private int repeatCount;

    public Animation(Entity entity, int repeatCount) {
        super(entity);
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        super.getEntity().nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(super.getEntity(),
                    createAnimationAction(super.getEntity(),
                            Math.max(this.repeatCount - 1, 0)),
                    super.getEntity().getAnimationPeriod());
        }
    }
}
