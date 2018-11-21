import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map.*;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.*;

class AStarPathingStrategy implements PathingStrategy{
    //private List<Point> points;



    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        //going to return
        List<Point> computedPath_List = new ArrayList<>();
        //closed list ( node holds points)
        Map<Point,Node> closedMap = new HashMap<Point, Node>();
        Map<Point,Node> openMap = new HashMap<Point, Node>();
        //open list
        Queue<Node> openList = new PriorityQueue<Node>(Comparator.comparingInt(Node::getF));

        Node startNode = new Node(0,heuristic(start,end), 0 + heuristic(start,end), start, null);

        //add current to openlist
        openList.add(startNode);
        //curent node in loop
        Node current = null;

        //step 1 - loop while the open list is not empty
        while (!openList.isEmpty()) {

            //step 2 - getting a value with f_min
            current = openList.remove();

            //step 3 - checking if we have got to the end point
            if (withinReach.test(current.getPosition(), end)){
                System.out.print("found path");
                return computedPath(computedPath_List, current);
            }

            //filter the neighbors and end in list
            List<Point> neighbors = potentialNeighbors.apply(current.getPosition())
                    .filter(canPassThrough)
                    .filter(p -> !p.equals(start) && !p.equals(end)).collect(Collectors.toList());

            //List <Node> NodeNeighbors = transformPointToNode(neighbors,current.g+1, current, 0, current);

            //step 6 - evaluating currents neighbors
            for (Point neighbor: neighbors) {
                //step 7 - if that neighbor is not on the closed list
                if (!closedMap.containsKey(neighbor)) {

                    int temp_g = current.getG() + 1; //that neigbor has to be one away from curren
                    //step 8 - if neighbor is in the open list
                    //that is we have gotten to it already
                    if(openMap.containsKey(neighbor)) {

                        //Step 8.1 - if we already have that neighbor on our open list but it has a butt
                        if(temp_g < openMap.get(neighbor).getG()){ //if we have gotten to it already we need to update its G value
                            //this is because we are using current.G()+1 so therefore our g can change dpeending on the neighbor
                            Node betterNode = new Node(temp_g, heuristic(neighbor,end), heuristic(neighbor, end)+temp_g, neighbor, current);
                            //adding betterNode with smaller G but same cell
                            openList.add(betterNode);
                            //removing the old node with the bigger G
                            openList.remove(openMap.get(neighbor));
                            //replacing in the open map
                            openMap.replace(neighbor,betterNode);
                        }
                    } //end of step 8
                    //step 9 - if neighbor isnt in open list
                    else {
                        //create neighbor that has a g value one plug current
                        Node neighborNode = new Node(current.getG()+1, heuristic(neighbor, end), current.getG()+ 1 + heuristic(neighbor,end) , neighbor, current);
                        openList.add(neighborNode);
                        openMap.put(neighbor,neighborNode);
                    }

                } //end of step 7
                //step 4 - remove the current node from the open list

                //openList.remove(current);
                //step 5 - add that current node to the closedList
                closedMap.put(current.getPosition(),current);
            } //end of step 6


        }//end of step 1
        System.out.println("returned nothing");
        //return computedPath(computedPath_List,current);
        return computedPath_List;
    }


    public List<Point> computedPath(List<Point> compPath, Node winner)
    {
        compPath.add(winner.getPosition());
        if(winner.getPrevNode() == null)
        {
            Collections.reverse(compPath);
            return compPath;
        }
        //while prior sqaure isnt null
        return computedPath(compPath, winner.getPrevNode());

    }


    public void printOpenList(Queue<Node> openList) {

        openList.stream().forEach(n->System.out.println(n));
    }



    //calualte the heuristic
    public int heuristic(Point current, Point goal){return Point.distanceSquared(current,goal); }


    //Astar data structure
    class Node {
        private int g; //distance from start
        private int h; //heursitc distance
        private int f; //total distance f=g+h
        private Node prev_node; // prior node;
        private Point position;

        public Node (int g, int h, int f, Point position, Node prev_node){
            this.g = g; //no distance from the start
            this.h = h; // heuristic distance from the gaol
            this.f = f; //total distance f = g+h
            this.prev_node = prev_node; //parent node;
            this.position = position;
            // this.currentPoint = currentPoint;
        }

        //used to map p -> node
        public boolean containsPoint(Point p ){

            if(this.position == p){
                return true;
            }
            else{

                return false;
            }
        }

        //public int getG(){return g;}
        public int getH(){return h;}
        public int getF(){return f;}
        public void setG(int g){this.g = g;}
        public void setH(int h){this.h = h;}
        public int getG(){return g;}
        public void setPostion(Point p){position = p;}
        public Point getPosition(){return position;}
        public Node getPrevNode(){return prev_node;}
        public String toString(){return "getX() = "+ this.position.getX() + " getY() = " + this.position.getY(); }

    }
}

