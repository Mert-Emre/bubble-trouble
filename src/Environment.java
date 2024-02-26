// this class is used to control game loop, detecting collisions, drawing background,
// setting canvas and controlling restart of the game.
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Font;

//Environment is responsible for creating game objects, calling update functions of these game objects,
// drawing background, setting canvas, detecting collisions between different game objects and restarting the game.
public class Environment {
    public static final int canvasHeight = 500;
    public static final int canvasWidth = 800;
    public static final double maxX = 16.0;
    public static final double maxY = 9.0;
    public static final double minY = -1.0;
    private boolean lose;
    private boolean win;

    public static final int TOTAL_GAME_DURATION = 40000;
    public static final int PAUSE_DURATION = 5;
    private double gameFinishTime;
    private double startTime;
    private Player player;
    private ArrayList<Ball> ballList;
    private Bar timeBar;
    private Arrow arrow;

    public Environment() {
        startCanvas();
        lose = false;
        win = false;
        gameFinishTime = 0;
    }

    // Creates game objects, resets the timer, death and win situations.
    private void createGameObjects() {
        Bar timeBar = new Bar();
        Arrow arrow = new Arrow();
        Player player = new Player(arrow);
        ArrayList<Ball> ballList = new ArrayList<>();
        Ball firstBall = new Ball(Environment.maxX / 4, 1.5, 2, 1, 0);
        Ball secondBall = new Ball(Environment.maxX / 3, 1.5, 1, -1, 0);
        Ball thirdBall = new Ball(Environment.maxX / 4, 1.5, 0, 1, 0);
        ballList.add(firstBall);
        ballList.add(secondBall);
        ballList.add(thirdBall);
        this.player = player;
        this.arrow = arrow;
        this.timeBar = timeBar;
        this.ballList = ballList;
        startTime = System.currentTimeMillis();
        lose = false;
        win = false;
        gameFinishTime = 0;
    }

    // Sets the canvas for game.
    private void startCanvas() {
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.setXscale(0.0, maxX);
        StdDraw.setYscale(minY, maxY);
        StdDraw.enableDoubleBuffering();
    }

    // Draws background
    private void draw() {
        StdDraw.picture(maxX / 2, maxY / 2, "background.png", maxX, maxY);
    }

    public void update() {
        createGameObjects();
        double lastUpdate = startTime;
        while (true) {
            double currentTime = System.currentTimeMillis();
            double totalTime = currentTime - startTime;
            if (lose) {
                pauseScreen(false);
            } else if (win) {
                pauseScreen(true);
            } else {
                double elapsedTime = currentTime - lastUpdate;
                lastUpdate = currentTime;
                StdDraw.clear();
                draw();
                timeBar.update(totalTime);
                arrow.update(elapsedTime);
                for (Ball ball : ballList) {
                    ball.update(totalTime);
                }
                player.update(elapsedTime);
                StdDraw.show();
                arrowBallCollision(totalTime);
                detectResult(totalTime);
                StdDraw.pause(PAUSE_DURATION);
            }
        }
    }

    // If user collided with a ball or the time is up, it is a loss. If there is still time and all balls are shot
    // it is a win.
    private void detectResult(double totalTime) {
        if (playerBallCollision() || totalTime >= Environment.TOTAL_GAME_DURATION) {
            gameFinishTime = totalTime;
            lose = true;
        } else if (ballList.size() == 0) {
            gameFinishTime = totalTime;
            win = true;
        }
    }

    // Draw a line from the center of the player to the center of the ball. Find the intersection points of this line
    // with the player's rectangle. Find the distance from center of the player to this intersection point.
    // If the radius + this distance is bigger than distance between the center of the ball and the center of the player
    // it is a collision.
    private boolean playerBallCollision() {
        for (Ball ball : ballList) {
            double yDifference = player.getY() - ball.getY();
            double xDifference = player.getX() - ball.getX();
            if(xDifference == 0){
                xDifference = 0.0000001;
            }
            double slope = yDifference / xDifference;
            double yIntersection = 0;
            double xIntersection = 0;
            if(Math.abs(slope * player.getWidth()/2) <= player.getHeight()/2){
                if(ball.getX() < player.getX()){
                    yIntersection = player.getY() - slope * player.getWidth()/2;
                    xIntersection = player.getX() - player.getWidth()/2;
                }
                else{
                    yIntersection = player.getY() + slope * player.getWidth()/2;
                    xIntersection = player.getX() + player.getWidth()/2;
                }
            }
            else if(Math.abs(slope * player.getWidth()/2)> player.getHeight()/2){
                if(ball.getX() < player.getX()) {
                    xIntersection = player.getX() - player.getHeight() / (2 * slope);
                    yIntersection = player.getY() + player.getHeight()/2;
                }
                else{
                    xIntersection = player.getX() + player.getHeight() / (2 * slope);
                    yIntersection = player.getY() + player.getHeight()/2;
                }
            }
            double distance = Math.pow(Math.pow(ball.getX()- player.getX(),2) + Math.pow(ball.getY()- player.getY(),2),0.5);
            double intersectionDistance = Math.pow(Math.pow(player.getX()-xIntersection,2) + Math.pow(player.getY()-yIntersection,2),0.5);
            if(intersectionDistance + ball.getRadius() >= distance){
                return true;
            }
        }
        return false;
    }

    // Detects if there is a collision between the arrow and a ball. If arrow is already higher than the ball and distance
    // between them is less than or equal to the radius of ball then it is a collision.
    // If ball is higher than the arrow then we need to calculate the distance between the center of the ball and tip of the
    // arrow. If it is less than or equal to the radius of the ball, then we have a collision.
    private void arrowBallCollision(double totalTime) {
        if (arrow.getActive()) {
            for (int i = 0; i < ballList.size(); i++) {
                double distanceSquared = Math.pow(ballList.get(i).getY() - arrow.getHeight(), 2) + Math.pow(ballList.get(i).getX() - arrow.getX(), 2);
                double distance = Math.pow(distanceSquared, 0.5);
                Ball ball = ballList.get(i);
                if (ball.getY() <= arrow.getHeight() && Math.abs(ball.getX() - arrow.getX()) <= ball.getRadius()) {
                    if (ball.getLevel() > 0) {
                        ballList.add(new Ball(ball.getX(), ball.getY(), ball.getLevel() - 1, 1, totalTime));
                        ballList.add(new Ball(ball.getX(), ball.getY(), ball.getLevel() - 1, -1, totalTime));
                    }
                    ballList.remove(i);
                    if (ballList.size() > 0) {
                        arrow.removeArrow();
                    }
                    break;
                } else if (ball.getY() > arrow.getHeight() && distance <= ball.getRadius()) {
                    if (ball.getLevel() > 0) {
                        ballList.add(new Ball(ball.getX(), ball.getY(), ball.getLevel() - 1, 1, totalTime));
                        ballList.add(new Ball(ball.getX(), ball.getY(), ball.getLevel() - 1, -1, totalTime));
                    }
                    ballList.remove(i);
                    if (ballList.size() > 0) {
                        arrow.removeArrow();
                    }
                    break;
                }
            }
        }
    }

    // Draws game screen, prints game result and stops the timer, player and balls.
    private void pauseScreen(boolean status) {
        StdDraw.pause(PAUSE_DURATION);
        StdDraw.clear();
        draw();
        timeBar.update(gameFinishTime);
        arrow.update(0);
        player.update(0);
        StdDraw.picture(maxX / 2, (maxY - minY) / 2.18, "game_screen.png", maxX / 3.8, (maxY - minY) / 4);
        StdDraw.setFont(new Font("Helvetica", Font.BOLD, 30));
        StdDraw.setPenColor(StdDraw.BLACK);
        String pauseText = status ? "You Won!" : "Game Over!";
        StdDraw.text(maxX / 2, (maxY - minY) / 2, pauseText);
        StdDraw.setFont(new Font("Helvetica", Font.ITALIC, 15));
        StdDraw.text(maxX / 2, (maxY - minY) / 2.3, "To Replay Click \"Y\"");
        StdDraw.text(maxX / 2, (maxY - minY) / 2.6, "To Quit Click \"N\"");
        int userInput = pauseUserInput();
        if (userInput == 1) {
            createGameObjects();
        } else if (userInput == 2) {
            System.exit(0);
        }
        StdDraw.show();
    }

    // 0 means invalid input, 1 means start a new game and 2 means stop the game.
    private int pauseUserInput() {
        if (StdDraw.isKeyPressed(KeyEvent.VK_Y)) {
            return 1;
        } else if (StdDraw.isKeyPressed(KeyEvent.VK_N)) {
            return 2;
        }
        return 0;
    }
}
