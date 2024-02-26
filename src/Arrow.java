//this class is used to create an arrow that destroys a ball.
public class Arrow {
    private final int PERIOD_OF_ARROW = 1500;
    private boolean isActive;
    private double x;
    private double height;

    public Arrow() {
        setHeight(0);
        setActive(false);
        setX(0);
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getActive() {
        return isActive;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getHeight() {
        return height;
    }

    private void setHeight(double height){
        this.height = height < 0 ? 0: height;
    }

    private void draw() {
        StdDraw.picture(x, height / 2, "arrow.png", 0.2, height);
    }

    // If arrow is active then increase its height and draw.
    public void update(double elapsedTime) {
        if (isActive) {
            changeHeight(elapsedTime);
            draw();
        }

    }

    // Increase the height of the arrow linearly with time.
    private void changeHeight(double elapsedTime) {
        height += elapsedTime / PERIOD_OF_ARROW * Environment.maxY;
        if(height >= Environment.maxY){
            setActive(false);
            setHeight(0);
        }
    }

    // If arrow shoots a ball or reaches to the ceiling, then change its height to 0 and deactivate it.
    public void removeArrow(){
        setActive(false);
        setHeight(0);
    }

}
