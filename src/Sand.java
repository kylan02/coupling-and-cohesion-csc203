import processing.core.PImage;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Sand extends Entity{

    public static final String SAND_KEY = "sand";
    public static final int HOUSE_NUM_PROPERTIES = 0;
    public Sand(String id, Point position, List<PImage> images) {
        super(id, position, images);
        setKey(SAND_KEY);
    }

    public void spawnCrab(Point pressed, ImageStore imageStore, WorldModel world, EventScheduler scheduler, Predicate<Point> canPassThrough, Function<Point, Stream<Point>> potentialNeighbors) throws Exception {

            Point crabSpawnPoint = null;
            Optional<Point> optionalPoint = potentialNeighbors.apply(pressed)
                    .filter(canPassThrough)
                    .findAny();

            if (optionalPoint.isPresent()) {
                // If the Optional contains a Point, retrieve it
                crabSpawnPoint = optionalPoint.get();
                System.out.println("Point exists: " + crabSpawnPoint);
            } else {
                System.out.println("No Point present");
            }
            Active crab = new Crab(" ", new Point(crabSpawnPoint.getX(), crabSpawnPoint.getY()), imageStore.getImageList(Crab.CRAB_KEY), Crab.CRAB_ANIMATION_PERIOD, Crab.CRAB_ACTION_PERIOD);
            world.tryAddEntity(crab);
            crab.scheduleActions(scheduler, world, imageStore);
    }

}