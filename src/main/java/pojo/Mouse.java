package pojo;

public class Mouse {
    public int x;
    public int y;

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Mouse (" + x + "|" + y + ")";
    }
}
