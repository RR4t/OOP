public class Point {
    private int x;
    private int y;
    private long hilbertValue;

    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }

    public void SetX(int x){
        this.x=x;
    }

    public void SetY(int y){
        this.y=y;
    }

    public double Getx(){
        return this.x;
    }

    public double Gety(){
        return this.y;
    }

    public void setHilbertValue(long value) {
        this.hilbertValue = value;
    }

    public long getHilbertValue() {
        return hilbertValue;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d) H=%d", x, y, hilbertValue);
    }
}
