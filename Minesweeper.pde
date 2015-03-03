
import de.bezier.guido.*;
public final static int NUM_ROWS=20,NUM_COLS=20;
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs= new ArrayList<MSButton>();
public boolean gameOver=false;
public boolean win=false;

void setup ()
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
