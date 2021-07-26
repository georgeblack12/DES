import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class theActualAppliedProject extends PApplet {

String message="1011100000011001011011110100101011000100101100001100111011010110";
String theKey= "00011011010000010111111111001110100111011010111010011010";
int k=0;
DES code;
public void setup() {
  code=new DES(message,theKey);
}

public void draw() {
  code.djKhaled();
  code.feistalFunction();
   noLoop();
}
class DES {
  //DES variables 
  String message;
  String[] messagePart=new String[2];
  String MessagePart2;
  String theKey;
  String[] messageSplit=new String[8];
  String[] sixBit=new String[8];
  String fortyEightBits;
  String[] messagePlusKey=new String[48];
  String messagePlusKeyTotal;
  String[] newSixBits=new String[8];
  String[] sBox = {"0010", "1100", "0100", "0001", "0111", "1010", "1011", "0110", "1000", 
    "0101", "0011", "1111", "1101", "0000", "1110", "1001", "1110", "1011", "0010", 
    "1100", "0100", "0111", "1101", "0001", "0101", "0000", "1111", "1010", "0011", 
    "1001", "1000", "0110", "0100", "0010", "0001", "1011", "1010", "1101", "0111", 
    "1000", "1111", "1001", "1100", "0101", "0110", "0011", "0000", "1110", "1011", 
    "1000", "1100", "0111", "0001", "1110", "0010", "1101", "0110", 
    "1111", "0000", "1001", "1010", "0100", "0101", "0011"};
  String newSixBitSBox[]=new String[8];
  int n=0;
  String addSixBit;
  String[] permutation={"15", "6", "19", "20", "28", "11", "27", "16", "0", "14", "22", 
    "25", "4", "17", "30", "9", "1", "7", "23", "13", "31", "26", "2", "8", "18", "12", 
    "29", "5", "21", "10", "3", "24"};
  char[] pCharacters=new char[32];
  String pMessage="";
  String[] addMessageParts=new String[32];
  String addMessagePartsTotal;
  String[] keyPart= new String[2];
  String[] keyPartRotations = new String [32];
  String[] keyRotationsTotal= new String[16];
  String[] keyPermutation={"13", "16", "10", "23", "0", "4", "2", "27", "14", "5", "20", 
    "9", "22", "18", "11", "3", "25", "7", "15", "6", "26", "19", "12", "1", "40", "51", 
    "30", "36", "46", "54", "29", "39", "50", "44", "32", "47", "43", "48", "38", "55", 
    "33", "52", "45", "41", "49", "35", "28", "31"};
  String[] endKeyPart =new String[16];
  int t;


  DES(String tempMessage, String tempTheKey) {
    message=tempMessage;
    theKey=tempTheKey;
  }

  public void djKhaled() {
    keyPart[0]=theKey.substring(0, 28);
    keyPart[1]=theKey.substring(28, 56);
    //println("Key Split");
    // println(keyPart[0], keyPart[0].length());
    //println(keyPart[1], keyPart[1].length());

    //do our Rotations on the Key
    for (int i=0; i<=31; i++) {
      if (i==0 || i==1 || i==8 || i==15 && i<=15) {
        keyPartRotations[i]=keyPart[0].substring(1, 28)+keyPart[1].charAt(0);
        keyPart[0]=keyPartRotations[i];
      } else if ((i!=0 ||  i!=1 || i!=8 || i!=15) && i<=15) {
        keyPartRotations[i]=keyPart[0].substring(2, 28)+keyPart[0].substring(0, 2);
        keyPart[0]=keyPartRotations[i];
        } else if ((i==16 || i==17 || i==24 || i==31)) {
          keyPartRotations[i]=keyPart[1].substring(1, 28)+keyPart[1].charAt(0);
         keyPart[1]=keyPartRotations[i];
      } else { 
        keyPartRotations[i]=keyPart[1].substring(2, 28)+keyPart[1].substring(0, 2);
        keyPart[1]=keyPartRotations[i];
      }
    }

    //put both sides together to make start making our sixteen subkeys
    for (int i =0; i<=15; i++) {
      keyRotationsTotal[i]=keyPartRotations[i]+keyPartRotations[i+16];
    }
    for (int i =0; i<=15; i++) {
      endKeyPart[i]="";
    }

    //permutate the keys using the p-2 box to have our 16 subkeys
    // println("our final 16 subkeys we will use");
    for (int j=0; j<=15; j++) {
      for (int i=0; i<=47; i++) {
        endKeyPart[j]=endKeyPart[j]+
          keyRotationsTotal[j].charAt(Integer.parseInt(keyPermutation[i]));
      }
      println(endKeyPart[j], j,endKeyPart[j].length());
    }
  }

  public void feistalFunction() {

    //println("Split the Message");
    messagePart[0]=message.substring(0, 32);
    messagePart[1]=message.substring(32, 64);
    //println(messagePart[0]);
    //println(messagePart[1]);
    for (int j=0; j<=15; j++) {
      //println(messagePart[0], 0);
      //println(messagePart[1], 1);
      int remainder=j%2;
      //  println("split into fours");
      for (int i=0; i<=28; i+=4) {
        messageSplit[i/4]=messagePart[remainder].substring(i, i+4);
        //  println(messageSplit[i/4], j);
      }

      //adding a 1 or 0 to the right side of the first six 4 bits
      for (int i =0; i<=6; i++) {
        if (messageSplit[i+1].charAt(0)== '1') {
          sixBit[i]=messageSplit[i]+'1';
        } else {
          sixBit[i]=messageSplit[i]+'0';
        } 

        //now do it for the last 4 bits
        if (messageSplit[0].charAt(0)=='1') {
          sixBit[7]=messageSplit[7]+'1';
        } else {
          sixBit[7]=messageSplit[7]+'0';
        }
      }
      //adding a 1 or 0 to the left side of the first six 4bits
      for ( int i =1; i<=7; i++) {
        if (sixBit[i-1].charAt(4)=='1') {
          sixBit[i]='1'+sixBit[i];
        } else {
          sixBit[i]='0'+sixBit[i];
        }
      }
      if (sixBit[7].charAt(4)=='1') {
        sixBit[0]='1'+sixBit[0];
      } else {
        sixBit[0]='0'+sixBit[0];
      }

      fortyEightBits=join(sixBit, "");
      // println("48 bits");
      // println(fortyEightBits);
      //println(endKeyPart[k]);

      for (int i=0; i<=47; i+=1) {
        if (fortyEightBits.charAt(i)+endKeyPart[k].charAt(i)=='1'+'0'|| 
          fortyEightBits.charAt(i)+endKeyPart[k].charAt(i)=='0'+'1') {
          messagePlusKey[i]="1";
        } else { 
          messagePlusKey[i]="0";
        }
      }
      //println("messagePlusKey");
      messagePlusKeyTotal=join(messagePlusKey, "");
      //println(messagePlusKeyTotal);

      for (int i=0; i<=messagePlusKeyTotal.length()-6; i+=6) {
        newSixBits[i/6]=messagePlusKeyTotal.substring(i, i+6);
      }
      for (int i=0; i<=7; i++) {
        // println(newSixBits[i]);
        // println(newSixBits[i].charAt(0)+newSixBits[i].substring(5, 6)+
        //newSixBits[i].substring(1, 5));

        newSixBits[i]=newSixBits[i].charAt(0)+newSixBits[i].substring(5, 6)+
          newSixBits[i].substring(1, 5);
        //println(newSixBits[i]);
      }

      for (int i=0; i<=7; i++) {
        newSixBitSBox[i]=sBox[(Integer.parseInt(newSixBits[i].substring(0, 1))*32+
          Integer.parseInt(newSixBits[i].substring(1, 2))*16+
          Integer.parseInt(newSixBits[i].substring(2, 3))*8+
          Integer.parseInt(newSixBits[i].substring(3, 4))*4+
          Integer.parseInt(newSixBits[i].substring(4, 5))*2+
          Integer.parseInt(newSixBits[i].substring(5, 6)))];
        //println(newSixBitSBox[i]);
      }

      addSixBit=join(newSixBitSBox, "");
      //println(addSixBit);
      for (int i=0; i<=addSixBit.length()-1; i++) {
        pCharacters[i]=addSixBit.charAt(Integer.parseInt(permutation[i]));
      }
      for (int i=0; i<=31; i++) {

        pMessage=pMessage+str(pCharacters[i]);
        // println(pMessage, i,pMessage.length());
      }
      //print(pMessage);
      //println(pMessage, pMessage.length(), j);
      if (j==0) {
        for (int i=0; i<=31; i+=1) {
          if (Integer.parseInt(pMessage.substring(i, i+1))+
            Integer.parseInt(messagePart[1].substring(i, i+1))==1) {
            addMessageParts[i]="1";
          } else { 
            addMessageParts[i]="0";
          }
        }
      } else { 
        for (int i=0; i<=31; i+=1) {
          if (Integer.parseInt(pMessage.substring(i, i+1))+
            Integer.parseInt(messagePart[0].substring(i, i+1))==1) {
            addMessageParts[i]="1";
          } else { 
            addMessageParts[i]="0";
          }
        }
      }
      if (remainder==0) {
        t=1;
      } else t=0;

      addMessagePartsTotal=join(addMessageParts, "");
      //println(addMessagePartsTotal, remainder);
      messagePart[t]=addMessagePartsTotal;
      pMessage="";
   println(messagePart[t], k, t);
      k=k+1;
    }
    
    String c = messagePart[0]+messagePart[1];
    println(c);
   
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "theActualAppliedProject" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
