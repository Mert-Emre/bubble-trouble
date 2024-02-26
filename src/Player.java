// player class is used for drawing game character and controlling its movements and shootings.
import java.awt.event.KeyEvent;
public class Player {
    private final int PERIOD_OF_PLAYER = 6000;
    private final double PLAYER_HEIGHT_WIDTH_RATE = 37.0 / 23.0;
    private final double PLAYER_HEIGHT_SCALEY_RATE = 1.0 / 8.0;

    private double x;
    private final double y;
    private final double height;
    private final double width;
    private final Arrow arrow;


    public Player(Arrow arrow){
        height = (Environment.maxY - Environment.minY) * PLAYER_HEIGHT_SCALEY_RATE;
        width = height / PLAYER_HEIGHT_WIDTH_RATE;
        x = Environment.maxX/2;
        y = height /2;
        this.arrow = arrow;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    // Draws the player to the canvas.
    private void draw(){
        StdDraw.picture(x,y,"player_back.png",width, height);
    }

    // First changes the position of the player (if it has moves), then checks if the player shot, then draws the player.
    public void update(double elapsedTime){
        move(elapsedTime);
        shoot();
        draw();
    }

    // If user pressed left or right arrow player changes position.
    private void move(double elapsedTime){

        if(StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)){
            x += (elapsedTime / PERIOD_OF_PLAYER) * Environment.maxX;
        }
        if(StdDraw.isKeyPressed(KeyEvent.VK_LEFT)){
            x -= elapsedTime / PERIOD_OF_PLAYER * Environment.maxX;
        }
        checkBorderCollision();
    }

    // If user presses space, an arrow is shot.
    private void shoot(){
        if(!arrow.getActive() && StdDraw.isKeyPressed(KeyEvent.VK_SPACE)){
            arrow.setActive(true);
            arrow.setX(x);
        }
    }

    // If the player comes to the walls prevent it from pass the walls.
    private void checkBorderCollision(){
        if(x - width/2 < 0){
            x = width / 2;
        }
        else if(x + width / 2 > Environment.maxX){
            x = Environment.maxX - width/2;
        }
    }

}
