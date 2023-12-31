import processing.core.PImage;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class Dude extends Mobile{
    private int resourceLimit;

    public static final String DUDE_KEY = "dude";
    public static final int DUDE_ACTION_PERIOD = 0;
    public static final int DUDE_ANIMATION_PERIOD = 1;
    public static final int DUDE_LIMIT = 2;
    public static final int DUDE_NUM_PROPERTIES = 3;
    //private static final PathingStrategy strategy = new SingleStepPathingStrategy();
    private static final PathingStrategy strategy = new AStarPathingStrategy();

    public Dude(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, int resourceLimit) {
        super(id, position, images, animationPeriod, actionPeriod);
        setKey(DUDE_KEY);
        this.resourceLimit = resourceLimit;
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

    public int getResourceLimit() {
        return resourceLimit;
    }
    public abstract boolean transform( WorldModel world, EventScheduler scheduler, ImageStore imageStore);
}
