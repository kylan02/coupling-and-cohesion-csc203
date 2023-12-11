import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class Crab extends Mobile{
    public static final String CRAB_KEY = "crab";
    public static final double CRAB_ANIMATION_PERIOD = 0.180;
    public static final int CRAB_ACTION_PERIOD = 1;
    public static final int CRAB_NUM_PROPERTIES = 2;
    //private PathingStrategy strategy;
    //private static final PathingStrategy strategy = new SingleStepPathingStrategy();
    private static final PathingStrategy strategy = new AStarPathingStrategy();

    public Crab(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
        setKey(CRAB_KEY);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(scheduler,target);
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    private static final Function<Point, Stream<Point>> CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.getX(), point.getY() - 1))
                            .add(new Point(point.getX(), point.getY() + 1))
                            .add(new Point(point.getX() - 1, point.getY()))
                            .add(new Point(point.getX() + 1, point.getY()))
                            .build();
    private static final Function<Point, Stream<Point>> DIAGONAL_CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.getX() - 1, point.getY() - 1))
                            .add(new Point(point.getX() + 1, point.getY() + 1))
                            .add(new Point(point.getX() - 1, point.getY() + 1))
                            .add(new Point(point.getX() + 1, point.getY() - 1))
                            .add(new Point(point.getX(), point.getY() - 1))
                            .add(new Point(point.getX(), point.getY() + 1))
                            .add(new Point(point.getX() - 1, point.getY()))
                            .add(new Point(point.getX() + 1, point.getY()))
                            .build();
    public Point nextPosition(WorldModel world, Point destPos) {
        List<Point> path = strategy.computePath(this.getPosition(), destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),
                (p1, p2) -> p1.adjacent(p2),
                //PathingStrategy.CARDINAL_NEIGHBORS);
                //OR
                //CARDINAL_NEIGHBORS);
                //DIAGONAL_NEIGHBORS);
                DIAGONAL_CARDINAL_NEIGHBORS);
        if (path == null || path.isEmpty())
            return this.getPosition();
        else{
            return path.get(0);
        }
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> crabTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Dude.DUDE_KEY)));

        if (crabTarget.isPresent()) {
            Point tgtPos = crabTarget.get().getPosition();

            if (moveTo( world, crabTarget.get(), scheduler)) {
                    BloodyDude bloodyDude = new BloodyDude(" ", tgtPos, imageStore.getImageList(BloodyDude.BLOODY_DUDE_KEY), BloodyDude.BLOODY_DUDE_ANIMATION_PERIOD, BloodyDude.BLOODY_DUDE_ACTION_PERIOD);
                    world.addEntity(bloodyDude);
                    bloodyDude.scheduleActions(scheduler, world, imageStore);
            }

        }
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.getActionPeriod());
    }
}

