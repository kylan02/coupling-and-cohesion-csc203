import processing.core.PImage;

import java.util.List;

public class Sapling extends Plant{

    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH = 0;
    public static final int SAPLING_NUM_PROPERTIES = 1;
    public static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000; // have to be in sync since grows and gains health at same time
    public static final int SAPLING_HEALTH_LIMIT = 5;

    public Sapling(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, int health, int healthLimit) {
        super(id, position, images, animationPeriod, actionPeriod, health, healthLimit);
        setKey(SAPLING_KEY);
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getHealth() <= 0) {
            //Entity stump = Functions.createStump(Stump.STUMP_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList(Stump.STUMP_KEY));
            Stump stump = new Stump(Stump.STUMP_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (this.getHealth() >= this.getHealthLimit()) {
//            Entity tree = Functions.createTree(Tree.TREE_KEY + "_" + this.getId(), this.getPosition(),
//                    this.getPosition().getNumFromRange(Tree.TREE_ACTION_MAX, Tree.TREE_ACTION_MIN),
//                    this.getPosition().getNumFromRange(Tree.TREE_ANIMATION_MAX, Tree.TREE_ANIMATION_MIN),
//                    this.getPosition().getIntFromRange(Tree.TREE_HEALTH_MAX, Tree.TREE_HEALTH_MIN),
//                    imageStore.getImageList(Tree.TREE_KEY));

            Tree tree = new Tree(Tree.TREE_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList(Tree.TREE_KEY), this.getPosition().getNumFromRange(Tree.TREE_ANIMATION_MAX, Tree.TREE_ANIMATION_MIN), this.getPosition().getNumFromRange(Tree.TREE_ACTION_MAX, Tree.TREE_ACTION_MIN), this.getPosition().getIntFromRange(Tree.TREE_HEALTH_MAX, Tree.TREE_HEALTH_MIN), 0);

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.setHealth(this.getHealth()+1);
        if (!this.transform( world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.getActionPeriod());
        }
    }

}
