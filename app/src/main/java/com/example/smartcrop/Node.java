package com.example.smartcrop;

public class Node {

    private String date;
    private String time;
    private  String n1;
    private  String n2;
    private  String n3;
    private  String n4;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public String getN1() {
        if(this.n1==null || this.n1=="OFF")
        {
            this.n1="OFF";
        }
        else
        {
            this.n1="ON";
        }
        return n1;
    }

    public void setN1(String n1) {
        this.n1 = n1;
    }

    public String getN2() {
        if(this.n2==null || this.n2=="OFF")
        {
            this.n2="OFF";
        }
        else
        {
            this.n2="ON";
        }
        return n2;
    }

    public void setN2(String n2) {
        this.n2 = n2;
    }

    public String getN3() {
        if(this.n3==null || this.n3=="OFF")
        {
            this.n3="OFF";
        }
        else
        {
            this.n3="ON";
        }
        return n3;
    }

    public void setN3(String n3) {
        this.n3 = n3;
    }

    public String getN4() {
        if(this.n4==null || this.n4=="OFF")
        {
            this.n4="OFF";
        }
        else
        {
            this.n4="ON";
        }
        return n4;
    }
    public void setN4(String n4) {
        this.n4 = n4;
    }
}
