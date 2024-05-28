package com.example.myapplication;

public class Letter {
    char val;
    Letter sameWord ;
    Letter otherWord ;

    public Letter (char val){
        this.val=val;
        this.sameWord= null ;
        this.otherWord= null ;
    }
}
