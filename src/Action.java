/**
 * An action that can be taken by an entity
 */
public abstract class Action {
    private Entity entity;

    public Action(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity(){
        return entity;
    }

    public static Action createAnimationAction(Entity entity, int repeatCount) {
        //return new Action(ActionKind.ANIMATION, entity, null, null, repeatCount);
        return new Animation(entity, repeatCount);

    }

    public static Action createActivityAction(Entity entity, WorldModel world, ImageStore imageStore) {
        return new Activity(entity, world, imageStore);
    }

    public abstract void executeAction(EventScheduler scheduler);
}
