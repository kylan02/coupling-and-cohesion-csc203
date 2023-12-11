import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class BloodyDude extends Mobile{
    public static final String BLOODY_DUDE_KEY = "bloodyDude";
    public static final double BLOODY_DUDE_ACTION_PERIOD = 0.784;
    public static final double BLOODY_DUDE_ANIMATION_PERIOD = 0.180;
    public static final int BLOODY_DUDE_LIMIT = 04;
    public static final int BLOODY_DUDE_NUM_PROPERTIES = 3;
    //private static final PathingStrategy strategy = new SingleStepPathingStrategy();
    private static final PathingStrategy strategy = new AStarPathingStrategy();
    ImageStore imageStore;
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

    public BloodyDude(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
        setKey(BLOODY_DUDE_KEY);
    }

    public Point nextPosition(WorldModel world, Point destPos) {
//        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
//        Point newPos = new Point(super.getPosition().getX() + horiz, this.getPosition().getY());
//
//        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getKey() != Stump.STUMP_KEY) {
//            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
//            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);
//
//            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getKey() != Stump.STUMP_KEY) {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;
        List<Point> path = strategy.computePath(this.getPosition(), destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),
                (p1, p2) -> p1.adjacent(p2),
                //PathingStrategy.CARDINAL_NEIGHBORS);
                //OR
                //CARDINAL_NEIGHBORS);
                //DIAGONAL_NEIGHBORS);
                DIAGONAL_CARDINAL_NEIGHBORS);
        if (path == null || path.isEmpty()) {
            return this.getPosition();
        } else {
            return path.get(0);
        }
    }
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            if(target instanceof Crab){
                world.removeEntity(scheduler, target);
                return true;
            }else if(target instanceof Sand){
                System.out.println("in sand target");
                DIAGONAL_CARDINAL_NEIGHBORS.apply(target.getPosition())
                        .filter(world::withinBounds)
                        .forEach(neighbor -> {
                            world.setBackgroundCell(neighbor, new Background("flower", imageStore.getImageList("flowers")));
                        });
                world.setBackgroundCell(target.getPosition(), new Background("flower", imageStore.getImageList("flowers")));
                world.removeEntity(scheduler, target);


                return true;
            }
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
        return false;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.imageStore = imageStore;
        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Crab.CRAB_KEY, Sand.SAND_KEY)));

        if (target.isPresent()) {
            Point tgtPos = target.get().getPosition();

            if (moveTo( world, target.get(), scheduler)) {

            }
        }
//        else{
//            System.out.println("in else statement");
//            world.changeBloodyDudeToDude(this, scheduler, imageStore);
//        }
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.getActionPeriod());
    }
}

