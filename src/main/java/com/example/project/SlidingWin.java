package com.example.project;

import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;

public class SlidingWin {

    public int i,j,win,k=0,minValue,index=0,bestWin=0,bestWinCount,max;
    int SumofMaxtemp1,SumofMintemp1,Sumofhumidity1, SumofMaxtemp2,SumofMintemp2,Sumofhumidity2;
    int meanMaxtemp,meanMintemp,meanHumidity;
    boolean t=false;
    int[]winMin=new int[21]; // contains index of 8 windows of 21 (7x3) positions
    int[]winRow=new int[8];  // 1 position value of 8 windows
    // 8 windows initializing
    public int[][] w1=new int[7][3],w2=new int[7][3],w3=new int[7][3],w4=new int[7][3],
            w5=new int[7][3],w6=new int[7][3],w7=new int[7][3],w8=new int[7][3];
    // 8 Euclidean distance windows
    int[][] ED1=new int[7][3],ED2=new int[7][3],ED3=new int[7][3],ED4=new int[7][3],
            ED5=new int[7][3],ED6=new int[7][3],ED7=new int[7][3],ED8=new int[7][3];
    // 8 windows in 1 array
    public int[][][] w ={w1,w2,w3,w4,w5,w6,w7,w8};
    // 8 Euclidean distance windows in 1 array
    int[][][] ED ={ED1,ED2,ED3,ED4,ED5,ED6,ED7,ED8};
    public int[][] c7days,p14days;
    // variation vector
    public int[][] vc7days= new int[6][3],vp7days= new int[6][3];
    // final vector
    public int[][] fc7days=new int[7][3];

    public SlidingWin(int[][] c7day, int[][] p14day) {   // constructor
        c7days = c7day;
        p14days=p14day;

        // finding euclidean distance
        for(win=0;win<8;win++){
            for(i=0;i<7;i++){
                for(j=0;j<3;j++){
                  w[win][i][j] = p14days[i+win][j];
                  ED[win][i][j]= (int) Math.abs( Math.sqrt(Math.pow((w[win][i][j] - c7days[i][j]),2.0)));
                }
            }
        }
        // 1 position value of 8 windows
        for(i=0;i<7;i++){
            for(j=0;j<3;j++){
                for(win=0;win<8;win++){
                    winRow[win]=ED[win][i][j]; // storing 1 position elements of all 8 windows (total 21 position)
                }
         //finding minimum 1 position of 8 windows
                minValue=winRow[0];   // taking 1st value as min
                for(int i=1;i<8;i++){             // 8 windows gives 8 values
                    if(winRow[i] < minValue){
                        minValue = winRow[i];
                        index=i; t=true;
                    }
                }
                if(!t)   {  index=0; }
                winMin[k]=index; // window no: is the index which has minimum value at specified position
                k++;  t=false;
            }
        }
        // key-> index (window no:)
        // value-> count of max in windows (to know similarity)
        // mapped together in hashmap
        HashMap<Integer,Integer> MostOccur = new HashMap<>(); // key-value pair assigns
        for(i=0;i<winMin.length;i++){   // 21 positions
            bestWinCount=0;
            if(!(MostOccur.containsKey(winMin[i])))
            {
                for(j=i+1;j<winMin.length;j++){
                    if(winMin[i]==winMin[j]) { bestWinCount++; }
                }
                MostOccur.put(winMin[i],bestWinCount);
            }
        }
        // finding max repeated similarity
        j=0;
        max=MostOccur.get(j);
        for (; j<MostOccur.size(); j++) {
            if(MostOccur.get(j)>=max) { max=MostOccur.get(j); bestWin=j; } // finding max repeated similarity

        }
        // computing variation vc7 and vp7 days (6X1) size
        for(i=1;i<7;i++){
            for(j=0;j<3;j++){
                vc7days[i-1][j]=c7day[i][j]-c7day[i-1][j];
                vp7days[i-1][j]=w[bestWin][i][j]-w[bestWin][i-1][j];
            }
        }
        for(j=0;j<3;j++){
            for(i=0;i<6;i++){
                if(j==0)
                   { SumofMaxtemp1 +=vc7days[i][j];  SumofMaxtemp2 +=vp7days[i][j];  }
                else if (j==1)
                   { SumofMintemp1 +=vc7days[i][j];  SumofMintemp2 +=vp7days[i][j];  }
                else if(j==2)
                   { Sumofhumidity1 +=vc7days[i][j];  Sumofhumidity2 +=vp7days[i][j];  }
            }
        }
        meanMaxtemp = (SumofMaxtemp1+SumofMaxtemp2)/2;
        meanMintemp = (SumofMintemp1+SumofMintemp2)/2;
        meanHumidity = (Sumofhumidity1+Sumofhumidity2)/2;

        for(j=0;j<3;j++){
            for(i=0;i<7;i++){
                if(j==0)
                { fc7days[i][j] = c7day[i][j]+meanMaxtemp; }
                else if (j==1)
                { fc7days[i][j] = c7day[i][j]+meanMintemp;  }
                else if(j==2)
                { fc7days[i][j] = c7day[i][j]+meanHumidity;  }
            }
        }
        Log.d("c7",""+ Arrays.deepToString(c7days));
        Log.d("c7",""+ Arrays.deepToString(fc7days));

    }
}
//[[30, 30, 65], [29, 29, 67], [30, 30, 67], [30, 30, 68], [30, 30, 68], [29, 29, 70], [30, 30, 72]]