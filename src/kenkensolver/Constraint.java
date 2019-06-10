package kenkensolver;

import java.util.*;

public class Constraint {
    enum METHOD{
         MULTIPLY, ADD, SUBTRACT, DIVIDE, EQUALTO
     }

    private METHOD method;

    private int result;
    private String coordinates;
    private String row;
    private String coloumn;
    private int coloumnNumber;
    private int rowNumber;
    private List<Integer> possibleValues = new ArrayList<>();
    private int size;
    private ArrayDeque<Integer> Search = new ArrayDeque<>();
    private int currentVal;
    private Cage cage;

    public Constraint(METHOD method,
                      String coordinates,
                       int result, int size, Cage cage ){
         this.method = method;
         this.coordinates = coordinates;
         this.result = result;
         this.size = size;
         this.cage = cage;

         if (this.method == METHOD.MULTIPLY){
             this.getFactors();
         }

         else if(this.method == METHOD.DIVIDE){
             this.getDivsors();
         }

         else if (this.method == METHOD.EQUALTO){
             possibleValues.add(this.result);
         }

         else{
             this.getFullRange(this.size);
         }

     }

    public void setColoumn(String coloumn) {
        this.coloumn = coloumn;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getRow() {
        return row;
    }

    public String getColoumn() {
        return coloumn;
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

    public void setColoumnNumber(int coloumnNumber) {
        this.coloumnNumber = coloumnNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getResult() {
        return result;
    }

    private void getFullRange(int a){
        for (; a > 0; --a){
            this.possibleValues.add(a);
        }
     }

     private void getDivsors() {
         for (int b = this.size; b > 0; --b) {
             if (b % this.result == 0 || b == 1) {
                 this.possibleValues.add(b);
             }
         }
     }

     private void getFactors(){

        for (int a = this.size; a > 0; --a){
            if (this.result % a == 0){
                this.possibleValues.add(a);
            }
        }

     }

    public METHOD getMethod() {
        return method;
    }

    public Cage getCage() {
        return cage;
    }

    @Override
    public boolean equals(Object obj) {

        if (this.rowNumber == ((Constraint) obj).getRowNumber() &&
       this.coloumnNumber == ((Constraint) obj).getColoumnNumber()){
          return true;
      }

      else{
          return false;
      }
    }
}
