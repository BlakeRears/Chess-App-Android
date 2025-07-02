package models;

public class Piece {
    int col;
    int row;
    String color;
    String name;
    String type;
    boolean valid;
    
    public String getStringName() {
        return this.name;
    }
    public String getType() {
        return this.type;
    }
    public String getColor() {
        return this.color;
    }
    public void setStringName(String inputName) {
        this.name = inputName;
    }
    public int getCol() {
        return this.col;
    }
    public int getRow() {
        return this.row;
    }
   

}
	
