package com.example.myapplication;

import java.util.List;

public class BinaryWordsTree {

    Letter root ;

    public BinaryWordsTree(List<String> wordsList){
        for(String word  : wordsList){
            this.add(word);
        }
    }

    void add(String word){
        Letter cursor = root  ;
        Letter newNode ;
        int lOrR = -1 ;
        int index =0 ;
        boolean stop = false ;
        boolean doNothing = false ;
        if(root!=null && root.val!=word.charAt(0)){
            lOrR*=-1;
        }



        if(root==null){

            newNode = new Letter(word.charAt(0));
            root = newNode ;
            cursor = root ;
        }else{
        //    System.out.println("the word is :"+word);
        //    System.out.println(lOrR==-1? "left" : "right");
            while(!stop){

                if (lOrR==-1){

                    while(cursor.val == word.charAt(index)){
                      cursor = cursor.sameWord ;
                      index++ ;
           //             System.out.println(index+" :going to the left with :"+word.charAt(index)+" current letter: "+cursor.val);

                        if(index==word.length()-1 ){

                          doNothing=true;


                          break;
                      }
                    }
                    if(cursor.val != word.charAt(index)){
               //         System.out.println("switch to the right");
                        lOrR*=-1 ;
                    }
                }else{
                    while(cursor.otherWord!=null  && cursor.val != word.charAt(index)){
                        cursor=cursor.otherWord ;
                      //  System.out.println("going to the right with :"+word.charAt(index)+" current letter: "+cursor.val);
                    }
                    if( cursor.val == word.charAt(index)){
                        lOrR*=-1 ;

                    }
                }
                if((cursor.val=='*' && index==word.length()-1) || doNothing || (cursor.otherWord ==null && cursor.val!=word.charAt(index))){
                    stop=true ;
                }
            }

        }
        if(doNothing) {
                if(cursor.val!=word.charAt(index)){
                    while(cursor.val!=word.charAt(index) && cursor.otherWord!=null){
                        cursor=cursor.otherWord ;
                    }
                   newNode=new Letter(word.charAt(index));
                   cursor.otherWord=newNode ;
                   cursor=cursor.otherWord ;
                    newNode=new Letter('*');
                    cursor.sameWord=newNode ;

                }else{
                    if(cursor.sameWord.val!='*'){
                        newNode=new Letter('*');
                        cursor.otherWord=newNode ;
                    }

                }
        }else{

             if(lOrR==1){
                 newNode = new Letter(word.charAt(index)) ;
                 cursor.otherWord = newNode ;
                 cursor = cursor.otherWord ;
             }


            for(int i=index+1;i<word.length();i++){
                newNode = new Letter(word.charAt(i));

                cursor.sameWord=newNode ;
                cursor=cursor.sameWord ;

            }
            newNode=new Letter('*');
            cursor.sameWord=newNode ;



        }
    }
    void printBwt(Letter root, int nb){
        if(root!=null){
            System.out.println(root.val);
            printBwt(root.sameWord,nb);
            printBwt(root.otherWord,nb);
        }

    }
    String search(String word){
        int index = 0 ;
        Letter cursor = root ;
        boolean wordEnd = false ;
        StringBuilder path= new StringBuilder();
        while(!wordEnd && cursor!=null){
           // System.out.println(index);
            while (cursor!=null && cursor.val != word.charAt(index)){
                path.append('d');
              //  System.out.println(cursor.val +" is different from "+ word.charAt(index));
                cursor=cursor.otherWord ;
            }
          while(cursor!=null && cursor.val==word.charAt(index)){
              path.append('g');
             // System.out.println(cursor.val +" is same as from "+ word.charAt(index));
              cursor=cursor.sameWord ;
              index++;
              if(index==word.length()){
                  wordEnd=true ;
                  break;
              }
          }




        }
        if(cursor!=null && cursor.val == '*')
        return String.valueOf(path);
        else
            return "not found!";

    }

    String pathToWordWithTheGuessedLetterOnly(String path,char c){
        StringBuilder word = new StringBuilder();
        Letter curssor=root ;
        for(int i =0; i <path.length() ; i++){
          if(path.charAt(i)=='d')
              curssor=curssor.otherWord ;
          if(path.charAt(i)=='g'){
              if(Character.toLowerCase(c)==Character.toLowerCase(curssor.val))
              word.append(curssor.val);
              else
                  word.append('_');
              curssor=curssor.sameWord;

          }

        }
        return word.toString();
    }



}
