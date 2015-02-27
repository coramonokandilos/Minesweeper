import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {



public final static int NUM_ROWS=20,NUM_COLS=20;
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs= new ArrayList<MSButton>();
public boolean gameOver=false;
public boolean win=false;

public void setup ()
{
    size(600, 600);
    textAlign(CENTER,CENTER);
    // make the manager
    Interactive.make( this );
    buttons=new MSButton[NUM_ROWS][NUM_COLS];
    for(int row=0;row<NUM_ROWS;row++){
        for(int col=0;col<NUM_COLS;col++){
            buttons[row][col] = new MSButton(row,col);
        }
    }     
    setBombs();
}
public void setBombs()
{
    for(int i=1; i<=40; i++)
    {
    int row = (int)(Math.random()*NUM_ROWS);
    int col = (int)(Math.random()*NUM_COLS);
    if(!bombs.contains(buttons[row][col]))
        bombs.add(buttons[row][col]);
    }
}

public void draw ()
{
    if(isWon())
        displayWinningMessage();
}
public boolean isWon()
{
    int countClicked=0;
    int countBomb=0;
    for(int r=0; r < NUM_ROWS; r++)
    {
        for(int c=0; c < NUM_COLS; c++)
        {
            if(buttons[r][c].isClicked())
            countClicked++;
            if(bombs.contains(buttons[r][c]))
            countBomb++;
            if(NUM_ROWS*NUM_COLS==countClicked+countBomb)
                return true;
        }
    }
    return false;
}
public void displayLosingMessage()
{
    gameOver=true;
    for(int r=0; r<NUM_ROWS; r++)
      for(int c=0; c<NUM_COLS; c++)
        if(bombs.contains(buttons[r][c]))
        {
          buttons[r][c].setLabel("B");
          bombs.remove(buttons[r][c]);
          fill(255, 0, 0);
          buttons[10][6].setLabel("Y");
          buttons[10][7].setLabel("O");
          buttons[10][8].setLabel("U");
          buttons[10][9].setLabel(" ");
          buttons[10][10].setLabel("L");
          buttons[10][11].setLabel("O");
          buttons[10][12].setLabel("S");
          buttons[10][13].setLabel("E");
          buttons[10][14].setLabel("!");
        }
}
public void displayWinningMessage()
{
    win=true;
    for(int r=0; r<NUM_ROWS; r++)
      for(int c=0; c<NUM_COLS; c++)
        bombs.remove(buttons[r][c]);
    buttons[10][6].setLabel("Y");
    buttons[10][7].setLabel("O");
    buttons[10][8].setLabel("U");
    buttons[10][9].setLabel(" ");
    buttons[10][10].setLabel("W");
    buttons[10][11].setLabel("I");
    buttons[10][12].setLabel("N");
    buttons[10][13].setLabel("!");
}

public class MSButton
{
    private int r, c;
    private float x,y, width, height;
    private boolean clicked, marked;
    private String label;
    
    public MSButton ( int rr, int cc )
    {
        width = 600/NUM_COLS;
        height = 600/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
        y = r*height;
        label = "";
        marked = clicked = false;
        Interactive.add( this ); // register it with the manager
    }
    public boolean isMarked()
    {
        return marked;
    }
    public boolean isClicked()
    {
        return clicked;
    }
    // called by manager

    public void mousePressed () 
    {
          if(mouseButton == LEFT && gameOver == false && win == false && !isMarked())
                clicked=true;
          if(mouseButton == RIGHT && gameOver == false && win == false && isClicked()==false)
          {
              marked = !marked;
          } 
        else if(bombs.contains(this) && isMarked()==false)
        {
            displayLosingMessage();
        }
        else if(countBombs(r,c) > 0)
        {
            if(isMarked()==false)
            label = "" + countBombs(r, c);
            else if(isMarked()==true)
            clicked=!clicked;
        }
        else
        {
            if(isValid(r,c-1) && buttons[r][c-1].isClicked() == false)
                buttons[r][c-1].mousePressed();
            if(isValid(r,c+1) && buttons[r][c+1].isClicked() == false)
                buttons[r][c+1].mousePressed();
            if(isValid(r-1,c) && buttons[r-1][c].isClicked() == false)
                buttons[r-1][c].mousePressed();
            if(isValid(r+1,c) && buttons[r+1][c].isClicked() == false)
                buttons[r+1][c].mousePressed();
            if(isValid(r-1,c-1) && buttons[r-1][c-1].isClicked() == false)
                buttons[r-1][c-1].mousePressed();
            if(isValid(r-1,c+1) && buttons[r-1][c+1].isClicked() == false)
                buttons[r-1][c+1].mousePressed();
            if(isValid(r+1,c-1) && buttons[r+1][c-1].isClicked() == false)
                buttons[r+1][c-1].mousePressed();
            if(isValid(r+1,c+1) && buttons[r+1][c+1].isClicked() == false)
                buttons[r+1][c+1].mousePressed();
        }
    }

    public void draw () 
    {    
        if (marked)
            fill(0, 0, 255);
        else if(clicked && bombs.contains(this) ) 
            fill(255,0,0);
        else if(clicked)
            fill(255);
        else 
            fill(100);

        rect(x, y, width, height);
        fill(0);
        text(label,x+width/2,y+height/2);
    }
    public void setLabel(String newLabel)
    {
        label = newLabel;
    }
    public boolean isValid(int r, int c)
    {
        if(r>=0 && r<NUM_ROWS && c>=0 && c<NUM_COLS)
            return true;
        return false;
    }
    public int countBombs(int row, int col)
    {
        int numBombs = 0;
        if(isValid(row+1,col) && bombs.contains(buttons[row+1][col]))
            numBombs++;
        if(isValid(row-1,col) && bombs.contains(buttons[row-1][col]))
            numBombs++;
        if(isValid(row,col+1) && bombs.contains(buttons[row][col+1]))
            numBombs++;
        if(isValid(row,col-1) && bombs.contains(buttons[row][col-1]))
            numBombs++;
        if(isValid(row+1,col+1) && bombs.contains(buttons[row+1][col+1]))
            numBombs++;
        if(isValid(row-1,col+1) && bombs.contains(buttons[row-1][col+1]))
            numBombs++;
        if(isValid(row+1,col-1) && bombs.contains(buttons[row+1][col-1]))
            numBombs++;
        if(isValid(row-1,col-1) && bombs.contains(buttons[row-1][col-1]))
            numBombs++;
        return numBombs;
    }
}

public void keyPressed()
{
    gameOver=false;
    win=false;
    for(int r=0; r<NUM_ROWS; r++)
      for(int c=0; c<NUM_COLS; c++)
        {
          bombs.remove(buttons[r][c]);
          buttons[r][c].marked=false;
          buttons[r][c].clicked=false;
          buttons[r][c].setLabel(" ");
        }
    setBombs(); 
  
}

// import de.bezier.guido.*;
// public final static int NUM_ROWS = 20;
// public final static int NUM_COLS = 20;
// public final static int NUM_BOMBS = 5;
// //Declare and initialize NUM_ROWS and NUM_COLS = 20
// private MSButton[][] buttons; //2d array of minesweeper buttons
// private ArrayList <MSButton> bombs = new ArrayList <MSButton>(); //ArrayList of just the minesweeper buttons that are mined

// // 3. Go to line 25 and write the `setBombs()` function. It should generate a random `row` and `col`umn number. Use the `contains()` function to check to see if  `buttons[row][col]` is already in `bombs`. If it isn't then `add` it

// void setup ()
// {
//     size(400, 400);
//     textAlign(CENTER,CENTER);
    
//     // make the manager
//     Interactive.make( this );
    
//     buttons = new MSButton[NUM_ROWS][NUM_COLS];
//     for(int i = 0; i < NUM_ROWS; i++){
//         for(int j = 0; j < NUM_COLS; j++){
//             buttons[i][j] = new MSButton(i, j);
//         }
//     }

//     //declare and initialize buttons
//     setBombs();
// }
// public void setBombs()
// {
//     int numBombs = 15;
//     for(int i = 0; i < numBombs; i++){
//         int row = (int)(Math.random()*NUM_ROWS);
//         int col = (int)(Math.random()*NUM_COLS);
//         if(! bombs.contains(buttons[row][col])){
//             bombs.add(buttons[row][col]);
//         }
        
//     }
// }

// public void draw ()
// {
//     background( 0 );
//     if(isWon())
//         displayWinningMessage();
// }
// public boolean isWon()
// {
//     //your code here
//     return false;
// }
// public void displayLosingMessage()
// {
//     //your code here
// }
// public void displayWinningMessage()
// {
//     //your code here
// }

// public class MSButton
// {
//     private int r, c;
//     private float x,y, width, height;
//     private boolean clicked, marked;
//     private String label;
    
//     public MSButton ( int rr, int cc )
//     {
//         width = 400/NUM_COLS;
//         height = 400/NUM_ROWS;
//         r = rr;
//         c = cc; 
//         x = c*width;
//         y = r*height;
//         label = "";
//         marked = clicked = false;
//         Interactive.add( this ); 
//     }
//     public boolean isMarked()
//     {
//         return marked;
//     }
//     public boolean isClicked()
//     {
//         return clicked;
//     }
    
//     public void mousePressed () 
//     {
//         clicked = true;
//         if(keyPressed == true)
//             marked = !marked;
//         else if(bombs.contains(this))
//             displayLosingMessage();
//         else if(this.countBombs(r, c)>0)
//             this.setLabel(Integer.toString(countBombs(r, c)));
//         else  
//             if(isValid(r, c-1) && buttons[r][c-1].isClicked() == false)
//             {
//                 buttons[r][c-1].mousePressed();
//             }
//             if(isValid(r, c+1) && buttons[r][c+1].isClicked() == false)
//             {
//                 buttons[r][c+1].mousePressed();
//             }
//             if(isValid(r-1, c) && buttons[r-1][c].isClicked() == false)
//             {
//                 buttons[r-1][c].mousePressed();
//             }
//             if(isValid(r+1, c) && buttons[r+1][c].isClicked() == false)
//             {
//                 buttons[r+1][c].mousePressed();
//             }
//             if(isValid(r-1, c-1) && buttons[r-1][c-1].isClicked() == false)
//             {
//                 buttons[r-1][c-1].mousePressed();
//             }
//             if(isValid(r-1, c+1) && buttons[r-1][c+1].isClicked() == false)
//             {
//                 buttons[r-1][c+1].mousePressed();
//             }
//             if(isValid(r+1, c-1) && buttons[r+1][c-1].isClicked() == false)
//             {
//                 buttons[r+1][c-1].mousePressed();
//             }
//             if(isValid(r+1, c+1) && buttons[r+1][c+1].isClicked() == false)
//             {
//                 buttons[r+1][c+1].mousePressed();
//             }
//             //else recursively call `mousePressed` with the valid, unclicked, neighboring buttons 
//     }


//     public void draw () 
//     {    
//         if (marked)
//             fill(0);
//         else if( clicked && bombs.contains(this) ) 
//             fill(255,0,0);
//         else if(clicked)
//             fill( 200 );
//         else 
//             fill( 100 );

//         rect(x, y, width, height);
//         fill(0);
//         text(label,x+width/2,y+height/2);
//     }
//     public void setLabel(String newLabel)
//     {
//         label = newLabel;
//     }
//     public boolean isValid(int r, int c)
//     {
//         if(r < NUM_ROWS && r>=0 && c < NUM_COLS && c>=0){
//             return true;
//         }
//             return false;
//     }
//     public int countBombs(int row, int col)
//     {
//         int numBombs = 0;
//         MSButton [] tiles = new MSButton[8];
//         tiles[0] = buttons[row-1][col];
//         tiles[1] = buttons[row-1][col+1];
//         tiles[2] = buttons[row][col+1];
//         tiles[3] = buttons[row+1][col+1];
//         tiles[4] = buttons[row+1][col];
//         tiles[5] = buttons[row+1][col-1];
//         tiles[6] = buttons[row][col-1];
//         tiles[7] = buttons[row-1][col-1];
//         for(int i =0; i<tiles.length;i++){
//             if(isValid(tiles[i].r, tiles[i].c))
//             {
//                 if(bombs.contains(tiles[i]))
//                 { 
//                     numBombs++;
//                 }
//             }

//        }
//         return numBombs;
//     }
// }



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
