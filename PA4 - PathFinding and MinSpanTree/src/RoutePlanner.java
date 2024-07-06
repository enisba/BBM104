import java.util.*;
/**
 * The RoutePlanner class is responsible for calculating the shortest paths and constructing a minimally connected network of roads.
 * It supports calculating the shortest routes between points, creating a barely connected map using a union-find algorithm,
 * and analyzing the efficiency of the generated routes compared to original routes.
 */
public class RoutePlanner {
    private List<Road> roads;
    private Map<String, Route> shortestPathMap;
    private Map<String, String> parent = new HashMap<>();
    private Map<String, Integer> rank = new HashMap<>();

    /**
     * Initializes a new RoutePlanner with a specified list of roads.
     *
     * @param roads the list of roads.
     */
    public RoutePlanner(List<Road> roads) {
        this.roads = roads;
        this.shortestPathMap = new HashMap<>();
    }

    /**
     * Calculates the shortest route starting from a specified point.
     * The method initializes paths and progressively builds the shortest path using a modified it is explained in pdf for fastest route algorithm.
     *
     * @param start starting point for the shortest path calculation.
     * @param roads roads available for constructing the route.
     */
    public void calculateShortestRoute(String start, List<Road> roads) {
        List<Route> routeList = new ArrayList<>();
        shortestPathMap.clear();
        shortestPathMap.put(start, new Route(0, null, start, start, 0));
        for (Road road : roads) {
            if (road.getPoint1().equals(start) || road.getPoint2().equals(start)) {
                String neighbor = road.getPoint1().equals(start) ? road.getPoint2() : road.getPoint1();
                routeList.add(new Route(road.getLength(), road, start, neighbor, 0));
            }
        }

        int i = 1;
        while (!routeList.isEmpty()) {
            routeList.sort(new Comparator<Route>() {
            public int compare(Route o1, Route o2) {
                // 1. Compare by Distance (if priorities are equal)
                int distanceComparison = Integer.compare(o1.getDistance(), o2.getDistance());
                if (distanceComparison != 0) {
                    return distanceComparison;
                }

                // 2. Compare by Priority
                int priorityComparison = Integer.compare(o1.getPriority(), o2.getPriority());
                if (priorityComparison != 0) {
                    return priorityComparison;
                }

                // 3. Compare by ID (if both priority and distance are equal)
                return Integer.compare(o1.getRoad().getId(), o2.getRoad().getId());
            }
        });

            Route currentRoute = routeList.remove(0);
            String currentPoint = currentRoute.getPoint2();

            if (shortestPathMap.containsKey(currentPoint)) {
                continue;
            }

            shortestPathMap.put(currentPoint, currentRoute);

            for (Road road : roads) {
                if (road.getPoint1().equals(currentPoint) || road.getPoint2().equals(currentPoint)) {
                    String neighbor = road.getPoint1().equals(currentPoint) ? road.getPoint2() : road.getPoint1();
                    if (!shortestPathMap.containsKey(neighbor)) {
                        int newDistance = currentRoute.getDistance() + road.getLength();
                        routeList.add(new Route(newDistance, road, currentPoint, neighbor, i));
                    }
                }
            }
            i++;
        }
    }

    /**
     * Retrieves the shortest route from the starting point to a specified endpoint.
     * If no route exists, returns an empty list.
     *
     * @param start the start point of the route
     * @param end the end point of the route
     * @return a list of Roads representing the shortest route from start to end
     */
    public List<Road> getShortestRoute(String start, String end) {
        List<Road> route = new ArrayList<>();
        String current = end;
        while (!current.equals(start) && shortestPathMap.containsKey(current)) {
            Route routeInfo = shortestPathMap.get(current);
            route.add(routeInfo.getRoad());
            current = routeInfo.getPoint1().equals(current) ? routeInfo.getPoint2() : routeInfo.getPoint1();
        }
        if (!current.equals(start)) {
            return new ArrayList<>();
        }
        Collections.reverse(route);
        return route;
    }
    /**
     * Implements the union-find algorithm to find the root of the point.
     * If the point is not already present, initializes it.
     *
     * @param point the point to find the root of
     * @return the root point
     */
    private String unionFind(String point) {
        if (!parent.containsKey(point)) {
            parent.put(point, point);
            rank.put(point, 0);
            return point;
        }

        if (!parent.get(point).equals(point)) {
            parent.put(point, unionFind(parent.get(point)));
        }
        return parent.get(point);
    }
    /**
     * Unites two subsets into one subset, used in the union-find algorithm to ensure no cycles are formed.
     *
     * @param root1 the root of first subset
     * @param root2 the root of second subset
     */
    private void union(String root1, String root2) {
        if (rank.get(root1) < rank.get(root2)) {
            parent.put(root1, root2);
        } else if (rank.get(root1) > rank.get(root2)) {
            parent.put(root2, root1);
        } else {
            parent.put(root1, root2);
            rank.put(root2, rank.get(root2) + 1);
        }
    }
    /**
     * Calculates a barely connected map using It is explained in pdf  algorithm for barely connected map.
     * This function sorts the roads by length and attempts to add the shortest roads without forming a cycle.
     *
     * @return a list of Roads that forms the barely connected map
     */
    public List<Road> calculateBarelyConnectedMap() {
        List<Road> barelyConnectedMap = new ArrayList<>();
        parent.clear();
        rank.clear();

        for (Road road : roads) {
            unionFind(road.getPoint1());
            unionFind(road.getPoint2());
        }

        roads.sort(new Comparator<Road>() {
            @Override
            public int compare(Road r1, Road r2) {
                int lengthCompare = Integer.compare(r1.getLength(), r2.getLength());
                if (lengthCompare != 0) return lengthCompare;
                return Integer.compare(r1.getId(), r2.getId());
            }
        });
        for (Road road : roads) {
            String root1 = unionFind(road.getPoint1());
            String root2 = unionFind(road.getPoint2());

            if (!root1.equals(root2)) {
                barelyConnectedMap.add(road);
                union(root1, root2);
            }
        }
        return barelyConnectedMap;
    }
    /**
     * Analyzes and compares the original shortest route with the route on the barely connected map.
     * Provides metrics on efficiency changes due to the modifications.
     *
     * @param start the start point of the route
     * @param end the end point of the route
     * @param shortestRouteOriginal the shortest route based on the original roads
     * @param barelyConnectedMap the barely connected map roads
     * @return a map of analysis results containing route and material usage ratios
     */
    public Map<String, Double> calculateMapsRatio(String start, String end, List<Road> shortestRouteOriginal, List<Road> barelyConnectedMap) {
        calculateShortestRoute(start, barelyConnectedMap);
        List<Road> shortestRouteBarelyConnected = getShortestRoute(start, end);

        double routeRatio = (double) calculateRouteDistance(shortestRouteBarelyConnected) / calculateRouteDistance(shortestRouteOriginal);
        double materialRatio = (double) calculateRouteDistance(barelyConnectedMap) / calculateRouteDistance(roads);

        Map<String, Double> analysisResults = new HashMap<>();
        analysisResults.put("routeRatio", routeRatio);
        analysisResults.put("materialRatio", materialRatio);

        return analysisResults;
    }
    /**
     * Calculates the total distance for a given list of roads.
     *
     * @param route the list of roads to calculate the distance for
     * @return the total distance of the route
     */
    public int calculateRouteDistance(List<Road> route) {
        return route.stream().mapToInt(Road::getLength).sum();
    }
}
