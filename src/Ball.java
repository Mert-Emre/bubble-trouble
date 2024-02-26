//this class is used for drawing balls that makes projectile motion.
public class Ball {
    private final int PERIOD_OF_BALL = 12000;
    private final double HEIGHT_MULTIPLIER = 1.75;
    private final double RADIUS_MULTIPLIER = 2.0;
    private final double GRAVITY = 0.0000005 * (Environment.maxY - Environment.minY);
    private final double minPossibleHeight = (Environment.maxY - Environment.minY) / 8 * 1.4;
    private final double minPossibleRadius = (Environment.maxY - Environment.minY) * 0.0175;
    // level can be 0,1,2 and 0 is the smallest ball, 2 is the biggest ball
    private int level;
    // direction can be -1 or 1. -1 is to the left and 1 is to the right.
    private double x;
    private double y;
    private double x0;
    private double y0;
    private double radius;
    private double lastFloorCollision;
    private double lastWallCollision;
    private double vx;
    private double vy;
    private Ball(){}

    public Ball(double x, double y,int level, int direction,double time){
        x0=x;
        y0=y + getRadius();
        setX(x0);
        setY(y0);
        setLevel(level);
        setVx(Environment.maxX/PERIOD_OF_BALL * direction);
        setVy(0.004 + level *0.001);
        lastFloorCollision =time;
        lastWallCollision = time;
        radius = minPossibleRadius * Math.pow(RADIUS_MULTIPLIER,level);
    }

    public double getRadius() {
        return radius;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public double getX() {
        return x;
    }

    // Projectile motion x position
    private void setX(double time) {
        x = x0 + getVx() * time;
    }

    public double getY() {
        return y;
    }

    // Projectile motion y position
    private void setY(double time) {
        y = y0 + getVy() * time - 0.5*GRAVITY * Math.pow(time,2);
        if(y-getRadius() <0){
            y = getRadius();
        }
    }

    public double getVx(){
        return vx;
    }

    private void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    // Draws ball to the canvas.
    private void draw(){
        StdDraw.picture(getX(),getY(),"ball.png",2*radius,2*radius);
    }

    //First calculates new x and y position then checks if the ball collides with the floor or borders and draws the ball.
    public void update(double totalTime){
        setX(totalTime-lastWallCollision);
        setY(totalTime-lastFloorCollision);
        checkWallCollision(totalTime);
        checkFloorCollision(totalTime);
        draw();
    }

    // If the center of ball is close enough(less than its radius) to a wall then it will change its x velocity to -vx.
    // This function also keeps track of last collision with walls because it is used for finding x position.
    private void checkWallCollision(double totalTime){
        if(getX() + radius >= Environment.maxX && getVx()>0){
            setVx(-getVx());
            x0=Environment.maxX -radius;
            lastWallCollision = totalTime;
        }else if(getX() - radius<=0 && getVx()<0){
            setVx(-getVx());
            x0=radius;
            lastWallCollision = totalTime;
        }
    }

    // If the center of ball is close enough(less than its radius) to the
    // floor then it will change its y velocity to required velocity.
    // This function also keeps track of last collision with the floor because it is used for finding y position.
    private void checkFloorCollision(double totalTime){
        if(getY()-radius <=0){
            y0 = radius;
            setVy(Math.pow(2*GRAVITY*(minPossibleHeight * Math.pow(HEIGHT_MULTIPLIER,level)),0.5));
            lastFloorCollision = totalTime;
        }
    }
}
