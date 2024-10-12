package models;

public class Rectangle {
    int width;
    int height;

    public Rectangle(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public boolean equals(Rectangle rect) {
        return width == rect.width && height == rect.height ||
                height == rect.width && width == rect.height;
    }

    public int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        if (width <= 0)
            throw new IllegalArgumentException();
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        if (height <= 0)
            throw new IllegalArgumentException();
        this.height = height;
    }
}
