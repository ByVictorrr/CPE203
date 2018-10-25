public interface Moveable extends Entity {

    Point position();
    Point nextPosition();
    void move();


}