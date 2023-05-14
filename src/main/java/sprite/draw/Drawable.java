package sprite.draw;

import util.media.Animation;
import util.resources.ConfigReader;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Handles animation images.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.0
 *
 * @param <T> the animation type
 */
public abstract class Drawable<T extends Enum<T>> {
    private boolean isAnimationPaused = false;

    /**
     * The Current animation image.
     */
    BufferedImage currentAnimationImage;

    private T currentAnimation;
    private final List<BufferedImage> animationImages;
    private final Map<T, List<Integer>> animationImageNumbers;
    // Keep track of the current index in the list of type int in animationImageNumbers.
    private int currentAnimationFrame = 0;
    /**
     * The Flip animation horizontally.
     */
    // Used to flip the image, for example, for walking left and right.
    protected boolean flipAnimationHorizontally = false;

    // Keep track of how many frames the current animation frame has played for. Switch to the next frame every
    // currentAnimationFrameCountSwitchAt frames.
    private double currentAnimationSeconds = 0;
    private final double currentAnimationFramesPerSecond = ConfigReader.getAnimationFramesPerSecond();
    private final double currentAnimationSecondsPerFrame = 1 / currentAnimationFramesPerSecond;

    /**
     * Instantiates an animated image.
     *
     * @param animationImages       the animation images of the entities
     * @param animationImageNumbers the animation image numbers of the entities
     * @param initialAnimation      the initial animation of the entities
     */
    public Drawable(List<BufferedImage> animationImages, Map<T, List<Integer>> animationImageNumbers,
                    T initialAnimation) {
        this.animationImages = animationImages;
        this.animationImageNumbers = animationImageNumbers;

        setCurrentAnimation(initialAnimation);
    }

    /**
     * Sets current animation, its frame, and its seconds.
     *
     * @param animation the animation of an entity
     */
    protected void setCurrentAnimation(T animation) {
        if (animation == currentAnimation) {
            return;
        }

        currentAnimation = animation;
        currentAnimationFrame = 0;
        currentAnimationSeconds = 0;

        currentAnimationImage = getCurrentAnimationImage();
    }

    /**
     * Sets the value of flip animation horizontally for the entity.
     *
     * @param value the boolean
     */
    protected void setFlipAnimationHorizontally(boolean value) {
        flipAnimationHorizontally = value;
    }

    /**
     * Update the animated images.
     *
     * @param deltaTime the delta time
     */
    protected void update(double deltaTime) {
        if (isAnimationPaused) {
            return;
        }

        currentAnimationSeconds += deltaTime;
        if (currentAnimationSeconds >= currentAnimationSecondsPerFrame) {
            int frameIncrease = (int) (currentAnimationSeconds / currentAnimationSecondsPerFrame);
            currentAnimationSeconds = 0;
            currentAnimationFrame = (currentAnimationFrame + frameIncrease) % animationImageNumbers.get(
                    currentAnimation).size();
        }

        currentAnimationImage = getCurrentAnimationImage();

        if (flipAnimationHorizontally) {
            currentAnimationImage = Animation.flipImageHorizontally(currentAnimationImage);
        }
    }

    private BufferedImage getCurrentAnimationImage() {
        List<Integer> currentAnimationImageNumbers = animationImageNumbers.get(currentAnimation);
        return animationImages.get(currentAnimationImageNumbers.get(currentAnimationFrame));
    }

    /**
     * Gets current image of the entities.
     *
     * @return the image
     */
    protected BufferedImage getCurrentImage() {
        return currentAnimationImage;
    }

    /**
     * Gets current animation of the entities.
     *
     * @return the current animation
     */
    public T getCurrentAnimation() {
        return currentAnimation;
    }

    /**
     * Gets flip animation horizontally of the entities.
     *
     * @return the horizontal flipped animation
     */
    protected boolean getFlipAnimationHorizontally() {
        return flipAnimationHorizontally;
    }

    /**
     * Pause the animation of an entity.
     */
    protected void pauseAnimation() {
        isAnimationPaused = true;
    }

}
