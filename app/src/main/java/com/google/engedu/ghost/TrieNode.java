package com.google.engedu.ghost;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        HashMap<Character,TrieNode> temp =children;
        for(int i=0;i<s.length();i++){
            if(!temp.containsKey(s.charAt(i))){
                temp.put(s.charAt(i),new TrieNode());
            }
            if(i+1==s.length()){
                temp.get(s.charAt(i)).isWord=true;
            }
            temp = temp.get(s.charAt(i)).children;
        }

    }

    public boolean isWord(String s)
    {
        TrieNode temp = searchNode(s);
        if(temp==null)
            return false;
        else
            return temp.isWord;
    }
    public TrieNode searchNode(String s){
        TrieNode temp =this;
        for(int i=0;i<s.length();i++){
            if(!temp.children.containsKey(s.charAt(i))){
                return null;
            }
            temp=temp.children.get(s.charAt(i));
        }
        return temp;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode temp =searchNode(s);
            if(temp!=null){
            while(!temp.isWord){
                for( char ch:temp.children.keySet()){
                    temp=temp.children.get(ch);
                    s+=ch;
                    break;
                    }
                }
                return  s;
            }
        else
         return null;
    }

    public String getGoodWordStartingWith(String s) {
        TrieNode temp = searchNode(s);
        Random random=new Random();
        ArrayList<Character>charWithoutWord=new ArrayList<>();
        ArrayList<Character> charWord= new ArrayList<>();
//        Log.v("Searched node",temp.children.keySet().toString());
        if(temp!=null){
            for(;;) {
                for (char ch : temp.children.keySet()) {
                    if (temp.children.get(ch).isWord) {
                        charWord.add(ch);
                    } else {
                        charWithoutWord.add(ch);
                    }
                }
                if(charWithoutWord.size()>=1){
                    char c =charWithoutWord.get(random.nextInt(charWithoutWord.size()));
                    s+=c;
                    temp = temp.children.get(c);
                }
                else
                {
                    s+=charWord.get(random.nextInt(charWord.size()));
                    break;
                }
                charWithoutWord.clear();
                charWord.clear();
            }
            return  s;
        }
        else
        return null;
    }
}
