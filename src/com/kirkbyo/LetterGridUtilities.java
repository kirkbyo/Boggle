package com.kirkbyo;

/**
 * Created by ozziekirkby on 2016-12-03.
 */
public class LetterGridUtilities {
     public int amountOfPossibilities(int rows, int columns) {
         if (rows == columns) {
             switch (rows) {
                 case 3: return 9521; // 3x3
                 case 4: return 283400; // 4x4
                 case 5: return 1502417; // 5x5
                 case 6: return 4113192; // 6x6
                 default: return 1;
             }
         } else if ((rows == 3 || rows == 4) && (columns == 3 || columns == 4)) { // 4x3
             return 60984;
         } else if ((rows == 5 || rows == 4) && (columns == 5 || columns == 4)) { // 5x4
             return 678600;
         } else if ((rows == 5 || rows == 6) && (columns == 5 || columns == 6)) { // 6x5
             return 2510808;
         } else if ((rows == 6 || rows == 3) && (columns == 6 || columns == 3)) { // 6x3
             return 320228;
         } else if ((rows == 5 || rows == 3) && (columns == 5 || columns == 3)) { // 5x3
             return 170357;
         } else if ((rows == 4 || rows == 6) && (columns == 4 || columns == 6)) { // 6x4
             return 1174676;
         }
         return 1;
     }
}
