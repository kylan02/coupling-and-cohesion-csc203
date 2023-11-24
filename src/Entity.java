import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity {
    //private EntityKind kind;
    public static final int PROPERTY_KEY = 0;
    public static final int PROPERTY_ID = 1;
    public static final int PROPERTY_COL = 2;
    public static final int PROPERTY_ROW = 3;
    public static final int ENTITY_NUM_PROPERTIES = 4;
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private String key;

    public void setId(String id) {
        this.id = id;
    }

    public void setImages(List<PImage> images) {
        this.images = images;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<PImage> getImages() {
        return images;
    }

    public String getKey() {
        return key;
    }

    public Entity(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public int getImageIndex() {  return imageIndex;  }

    public String getId() {  return id; }

    public Point getPosition() { return position; }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }
    public PImage getCurrentImage() {

        return this.images.get(this.imageIndex % this.images.size());
     }

    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */
    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.getX(),
                        this.position.getY(), this.imageIndex);
    }
}
