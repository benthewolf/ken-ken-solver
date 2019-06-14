package kenkensolver;

import java.util.*;

public class Constraint {
    enum METHOD{
         MULTIPLY, ADD, SUBTRACT, DIVIDE, EQUALTO
     }

    private METHOD method;

    private int result;
    private String coordinates;
    private int coloumnNumber;
    private int rowNumber;
    private List<Integer> possibleValues = new ArrayList<>();
    private int size;
    private ArrayDeque<Integer> Search = new ArrayDeque<>();
    private int currentVal = 0;
    private Cage cage;

    public Constraint(METHOD method,
                      String coordinates,
                       int result, int size, Cage cage ){
         this.method = method;
         this.coordinates = coordinates;
         this.result = result;
         this.size = size;
         this.cage = cage;


         if (this.method == METHOD.EQUALTO){
             possibleValues.add(this.result);
         }

         else{
             this.getFullRange(this.size);
         }

     }

    public List<Integer> getPossibleValues() {
        return possibleValues;
    }

    public ArrayDeque<Integer> getSearch() {
        return Search;
    }

    public void setCurrentVal(int currentVal) {
        this.currentVal = currentVal;
    }

    public int getCurrentVal() {
        return currentVal;
    }

    public int getColoumnNumber() {
        return coloumnNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setColoumnNumber(int coloumnNumber) {
        this.coloumnNumber = coloumnNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    private void getFullRange(int a){
        for (; a > 0; --a){
            this.possibleValues.add(a);
        }
     }


    public Cage getCage() {
        return cage;
    }

    @Override
    public boolean equals(Object obj) {

        if (this.coordinates.equals(
                ((Constraint) obj).getCoordinates()
        )){
          return true;
      }

      else{
          return false;
      }
    }
}
