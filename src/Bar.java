//this class is used to draw a time bar, and visualise remaining time to the user.
import java.awt.Color;

public class Bar {
    private final double BAND_HEIGHT = 0.5;
    private static final double HEIGHT = 1.0;
    private double bandWidth = Environment.maxX;
    private final int red = 225;
    private int green = 225;
    private final int blue = 0;
    private final int maxGreen = 225;

    public Bar() {
    }

    // Used for changing the green value.
    private void setGreen(int green) {
        this.green = green < 0 ? 0 : green;

    }
    // First draws the background for time bar then draws the bar.
    private void draw() {
        StdDraw.picture(Environment.maxX / 2, Environment.minY + HEIGHT / 2, "bar.png",Environment.maxX,HEIGHT);
        StdDraw.setPenColor(new Color(red, green, blue));
        StdDraw.filledRectangle(bandWidth / 2, Environment.minY + HEIGHT / 2, bandWidth / 2, BAND_HEIGHT/2);
    }

    // Changes the width and the color of the bar, then calls the draw function.
    public void update(double elapsedTime) {
        updateWidth(elapsedTime);
        changeColor(elapsedTime);
        draw();
    }
    //Used for changing the color from yellow to red by decreasing the value of green linearly with time.
    private void changeColor(double elapsedTime) {
        this.setGreen((int)(maxGreen * ((Environment.TOTAL_GAME_DURATION - elapsedTime) / Environment.TOTAL_GAME_DURATION)));
    }
    // Used for changing the width of the bar linearly with time.
    private void updateWidth(double elapsedTime) {
        bandWidth = Environment.maxX * ((Environment.TOTAL_GAME_DURATION - elapsedTime) / Environment.TOTAL_GAME_DURATION);
        bandWidth = bandWidth < 0 ? 0: bandWidth;
    }
}
