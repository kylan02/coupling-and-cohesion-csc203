import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy {

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        // Initialize path, open list, and closed list
        List<Point> path = new LinkedList<>();
        HashSet<Point> closedList = new HashSet<>();
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));

        // Create start node and add to open list
        Node startNode = new Node(start, null, 0, calculateHeuristic(start, end));
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            closedList.add(current.point);
            //System.out.println(current.point.x+" "+current.point.y);
            // Check if reached end point
            if (withinReach.test(current.point, end)) {
                return buildPath(current);
            }

            //Explore Neighbors
            potentialNeighbors.apply(current.point)
                    .filter(canPassThrough)
                    .filter(neighbor -> !(closedList.contains(neighbor)))
                    .forEach(neighbor -> {
                        int g = current.g + 1;
                        int h = calculateHeuristic(neighbor, end);
                        Node neighborNode;
                        if(current.equals(startNode)) {
                            neighborNode = new Node(neighbor, null, g, h);
                        }else{
                            neighborNode = new Node(neighbor, current, g, h);
                        }

                        if (!openList.contains(neighborNode) || g < neighborNode.g) {
                            openList.removeIf(n -> n.equals(neighborNode));
                            openList.add(neighborNode);
                        }
                    });
        }

        // No path found
        return path;
    }

    private List<Point> buildPath(Node target) {
        LinkedList<Point> path = new LinkedList<>();
        Node current = target;
        while (current != null) {
            path.addFirst(current.point);
            current = current.parent;
        }
        return path;
    }

    private int calculateHeuristic(Point a, Point b) {
        // Euclidean
        return (int) Math.sqrt(Math.pow(a.getX()-b.getX(), 2) + Math.pow(a.getY()-b.getY(), 2));
    }

    class Node {
        Point point;
        Node parent;
        int g; // Cost from start to current node
        int h; // Heuristic cost from current node to end
        int f; // Total cost (g + h)

        Node(Point point, Node parent, int g, int h) {
            this.point = point;
            this.parent = parent;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return point.equals(node.point);
        }
    }
}

