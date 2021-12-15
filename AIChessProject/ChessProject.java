import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Stack;
import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Box;
import javax.swing.BoxLayout;

/*
	Mohammed Alghazi
  BSHCDA4 Specialization, Data Analytics
  X18208495
*/

public class ChessProject extends JFrame implements MouseListener, MouseMotionListener {
    JLayeredPane layeredPane;
    JPanel chessBoard;
    JLabel chessPiece;
    int xAdjustment;
    int yAdjustment;
	int startX;
	int startY;
	int initialX;
	int initialY;
	JPanel panels;
	JLabel pieces;
  Boolean whiteTurn; // if white piece moves first
  Boolean blackTurn;
  Boolean win;
    String winner;
    Boolean white2Move;
    int moveCounter;
    AIAgent agent;
    Boolean agentwins;
    Stack temporary;
    static int selected;


    public ChessProject(){
        Dimension boardSize = new Dimension(600, 600);

        whiteTurn = true; // white piece has to move first
        blackTurn = false; // black piece cant move first


        //  Use a Layered Pane for this application
        layeredPane = new JLayeredPane();
        getContentPane().add(layeredPane);
        layeredPane.setPreferredSize(boardSize);
        layeredPane.addMouseListener(this);
        layeredPane.addMouseMotionListener(this);

        //Add a chess board to the Layered Pane
        chessBoard = new JPanel();
        layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
        chessBoard.setLayout( new GridLayout(8, 8) );
        chessBoard.setPreferredSize( boardSize );
        chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

        for (int i = 0; i < 64; i++) {
            JPanel square = new JPanel( new BorderLayout() );
            chessBoard.add( square );

            int row = (i / 8) % 2;
            if (row == 0)
                square.setBackground( i % 2 == 0 ? Color.white : Color.gray );
            else
                square.setBackground( i % 2 == 0 ? Color.gray : Color.white );
        }

        // Setting up the Initial Chess board.
		for(int i=8;i < 16; i++){
       		pieces = new JLabel( new ImageIcon("WhitePawn.png") );
			panels = (JPanel)chessBoard.getComponent(i);
	        panels.add(pieces);
		}
		pieces = new JLabel( new ImageIcon("WhiteRook.png") );
		panels = (JPanel)chessBoard.getComponent(0);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteKnight.png") );
		panels = (JPanel)chessBoard.getComponent(1);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteKnight.png") );
		panels = (JPanel)chessBoard.getComponent(6);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteBishup.png") );
		panels = (JPanel)chessBoard.getComponent(2);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteBishup.png") );
		panels = (JPanel)chessBoard.getComponent(5);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteKing.png") );
		panels = (JPanel)chessBoard.getComponent(3);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteQueen.png") );
		panels = (JPanel)chessBoard.getComponent(4);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteRook.png") );
		panels = (JPanel)chessBoard.getComponent(7);
	    panels.add(pieces);
		for(int i=48;i < 56; i++){
       		pieces = new JLabel( new ImageIcon("BlackPawn.png") );
			panels = (JPanel)chessBoard.getComponent(i);
	        panels.add(pieces);
		}
		pieces = new JLabel( new ImageIcon("BlackRook.png") );
		panels = (JPanel)chessBoard.getComponent(56);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackKnight.png") );
		panels = (JPanel)chessBoard.getComponent(57);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackKnight.png") );
		panels = (JPanel)chessBoard.getComponent(62);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackBishup.png") );
		panels = (JPanel)chessBoard.getComponent(58);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackBishup.png") );
		panels = (JPanel)chessBoard.getComponent(61);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackKing.png") );
		panels = (JPanel)chessBoard.getComponent(59);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackQueen.png") );
		panels = (JPanel)chessBoard.getComponent(60);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackRook.png") );
		panels = (JPanel)chessBoard.getComponent(63);
	    panels.add(pieces);
      moveCounter =0;
      white2Move = true;
      agent = new AIAgent();
      agentwins = false;
      temporary = new Stack();

    }

    /*
  Method to check were a Black Pawn can move to. There are two main conditions here. Either the Black Pawn is in
  its starting position in which case it can move either one or two squares or it has already moved and the it can only
  one square down the board. The Pawn can also take an opponent piece in a diagonal movement. and if it makes it to the
  bottom of the board it turns into a Queen (this should be handled where the move is actually being made and not in this
  method).
*/
private Stack getWhitePawnSquares(int x, int y, String piece){
  Square startingSquare = new Square(x, y, piece);
  Stack moves = new Stack();

  Move validM, validM2, validM3, validM4;
  int tmpx1 = x+1;
  int tmpx2 = x-1;
  int tmpy1 = y+1;
  int tmpy2 = y+2;

  // all the possible pawn moves

    Square tmp = new Square(x, tmpy1, piece); // first move is downwards
    Square tmp1 = new Square(tmpx1, tmpy1, piece); //second move is diagonally right
    Square tmp2 = new Square(tmpx2, tmpy1, piece); // third more is diagonally left
    Square tmp3 = new Square(x, tmpy2, piece); // can move two boxes on the first go
  if (y == 1) {
      validM2 = new Move(startingSquare, tmp3);
      if (!piecePresent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 75) + 20))) && !piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
          moves.push(validM2);
      } else {
          if (!(tmpx1 > 7)) {
              if (piecePresent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20)))) {
                  if (checkWhiteOponent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20)))) {
                      moves.push(validM2);
                  }
              }
          }

          if (!(tmpx2 < 0)) {
              if (piecePresent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20)))) {
                  if (checkWhiteOponent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20)))) {
                      moves.push(validM2);
                  }
              }
          }
      }
  }
  if (!(tmpy1 > 7)) {
      validM = new Move(startingSquare, tmp);
      if (!piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
          moves.push(validM);
      }if (!(tmpx1 > 7)) {
          validM3 = new Move(startingSquare, tmp1);
          if (piecePresent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20)))) {
              if (checkWhiteOponent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20)))) {
                  moves.push(validM3);
              }
          }
      }if (!(tmpx2 < 0)) {
          validM4 = new Move(startingSquare, tmp2);
          if (piecePresent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20)))) {
              if (checkWhiteOponent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20)))) {
                  moves.push(validM4);
              }
          }
      }
  }
  return moves;
}





/*
  Method to check if there is a BlackKing in the surrounding squares of a given Square.
  The method should return true if there is no King in any of the squares surrounding
  the square that was submitted to the method. The method checks the grid below:


                                         _|_____________|_________|_____________|_
                                          |             |         |             |
                                          |(x-75, y-75) |(x, y-75)|(x+75, y-75) |
                                         _|_____________|_________|_____________|_
                                          |             |         |             |
                                          |(x-75, y)    | (x, y)  |(x+75, y)    |
                                         _|_____________|_________|_____________|_
                                          |             |         |             |
                                          |(x-75, y+75) |(x, y+75)|(x+75, y+75) |
                                         _|_____________|_________|_____________|_
                                          |             |         |             |


*/
private Boolean checkSurroundingSquares(Square s){
  Boolean possible = false;
  int x = s.getXC()*75;
  int y = s.getYC()*75;
  if(!((GetPieceName((x+75), y).contains("BlackKing"))
  ||(GetPieceName((x-75), y).contains("BlackKing"))
  ||(GetPieceName(x,(y+75)).contains("BlackKing"))
  ||(GetPieceName((x), (y-75)).contains("BlackKing"))
  ||(GetPieceName((x+75),(y+75)).contains("BlackKing"))
  ||(GetPieceName((x-75),(y+75)).contains("BlackKing"))
  ||(GetPieceName((x+75),(y-75)).contains("BlackKing"))
  ||(GetPieceName((x-75), (y-75)).contains("BlackKing")))){
      possible = true;
  }
  return possible;
}

/*
  The getKingSquares method takes as an input any coordinates from a square and returns a stack of all the possible
  valid moves that the WhiteKing can move to.

  So lets consider how the King can move. The King can essentially move in any direction as long as there is not another
  king in an adajacent square to were the king lands. Additionally, the King can only move one square at a time.

  To support this method we will also create a helper method called checkSurroundingSquares(Square s){, see above
  that returns a Boolean value to let us know if a supplied square will be adajacent to another square with a
  BlackKing present. Essentially if we consider that the board is a set of squares with coordinates for each square (x, y), this
  allows us to identify the possible squares that we should be investigating, see below;

                                       _|___________|_________|___________|_
                                        |           |         |           |
                                        |(x-1, y-1) |(x, y-1) |(x+1, y-1) |
                                       _|___________|_________|___________|_
                                        |           |         |           |
                                        |(x-1, y)   | (x, y)  |(x+1, y)   |
                                       _|___________|_________|___________|_
                                        |           |         |           |
                                        |(x-1, y+1) | (x, y+1)|(x+1, y+1) |
                                       _|___________|_________|___________|_
                                        |           |         |           |


  This shows us that for a single square with coordinates of (x, y) we need to check eight other potential squares.
  Remember we only need to check squares and contsruct moves if the movement (Piece on a Square --> a new Square) is
  going to be placing the piece back on the board, if we are not taking our own piece and if the resulting landing square
  is not adjacent to the enemy King.
*/
private Stack getKingSquares(int x, int y, String piece){
  Square startingSquare = new Square(x, y, piece);
  Stack moves = new Stack();
  Move validM, validM2, validM3, validM4;
  int tmpx1 = x+1;
  int tmpx2 = x-1;
  int tmpy1 = y+1;
  int tmpy2 = y-1;

/*
  If we consider the grid above, we can create three different columes to check.
    - if x increases by one square, using the variable tmpx1 (x+1)
    - if x decreases by one square, using the variable tmpx2 (x-1)
    - or if x stays the same.
*/
  if(!((tmpx1 > 7))){
    /*
      This is the first condition where we will be working with the column where x increases.
      If we consider x increasing, we need to make sure that we don't fall off the board, so we use
      a condition here to check that the new value of x (tmpx1) is not greater than 7.

      From the grid above we can see in this column that there are three possible squares for us to check in
      this column:
      - were y decreases, y-1
      - were y increases, y+1
      - or were y stays the same

      The first step is to construct three new Squares for each of these possibilities.
      As the unchanged y value is already a location on the board we don't need to check the location and can simply
      make a call to checkSurroundingSquares for this new Square.

      If checkSurroundingSquares returns a positive value we jump inside the condition below:
        - firstly we create a new Move, which takes the starting square and the landing square that we have just checked with
          checkSurroundingSquares.
        - Next we need to figure out if there is a piece present on the square and if so make sure
          that the piece is an opponents piece.
        - Once we make sure that we are either moving to an empty square or we are taking our opponents piece we can push this
          possible move onto our stack of possible moves called "moves".

      This process is followed again for the other temporary squares created.

      After we check for all possoble squares on this column, we repeat the process for the other columns as identified above
      in the grid.
    */
    Square tmp = new Square(tmpx1, y, piece);
    Square tmp1 = new Square(tmpx1, tmpy1, piece);
    Square tmp2 = new Square(tmpx1, tmpy2, piece);
    if(checkSurroundingSquares(tmp)){
      validM = new Move(startingSquare, tmp);
      if(!piecePresent(((tmp.getXC()*75)+20), (((tmp.getYC()*75)+20)))){
        moves.push(validM);
      }
      else{
        if(checkWhiteOponent(((tmp.getXC()*75)+20), (((tmp.getYC()*75)+20)))){
          moves.push(validM);
        }
      }
    }
    if(!(tmpy1 > 7)){
      if(checkSurroundingSquares(tmp1)){
        validM2 = new Move(startingSquare, tmp1);
        if(!piecePresent(((tmp1.getXC()*75)+20), (((tmp1.getYC()*75)+20)))){
          moves.push(validM2);
        }
        else{
          if(checkWhiteOponent(((tmp1.getXC()*75)+20), (((tmp1.getYC()*75)+20)))){
            moves.push(validM2);
          }
        }
      }
    }
    if(!(tmpy2 < 0)){
      if(checkSurroundingSquares(tmp2)){
        validM3 = new Move(startingSquare, tmp2);
        if(!piecePresent(((tmp2.getXC()*75)+20), (((tmp2.getYC()*75)+20)))){
          moves.push(validM3);
        }
        else{
          System.out.println("The values that we are going to be looking at are : "+((tmp2.getXC()*75)+20)+" and the y value is : "+((tmp2.getYC()*75)+20));
          if(checkWhiteOponent(((tmp2.getXC()*75)+20), (((tmp2.getYC()*75)+20)))){
            moves.push(validM3);
          }
        }
      }
    }
  }
  if(!((tmpx2 < 0))){
    Square tmp3 = new Square(tmpx2, y, piece);
    Square tmp4 = new Square(tmpx2, tmpy1, piece);
    Square tmp5 = new Square(tmpx2, tmpy2, piece);
    if(checkSurroundingSquares(tmp3)){
      validM = new Move(startingSquare, tmp3);
      if(!piecePresent(((tmp3.getXC()*75)+20), (((tmp3.getYC()*75)+20)))){
        moves.push(validM);
      }
      else{
        if(checkWhiteOponent(((tmp3.getXC()*75)+20), (((tmp3.getYC()*75)+20)))){
          moves.push(validM);
        }
      }
    }
    if(!(tmpy1 > 7)){
      if(checkSurroundingSquares(tmp4)){
        validM2 = new Move(startingSquare, tmp4);
        if(!piecePresent(((tmp4.getXC()*75)+20), (((tmp4.getYC()*75)+20)))){
          moves.push(validM2);
        }
        else{
          if(checkWhiteOponent(((tmp4.getXC()*75)+20), (((tmp4.getYC()*75)+20)))){
            moves.push(validM2);
          }
        }
      }
    }
    if(!(tmpy2 < 0)){
      if(checkSurroundingSquares(tmp5)){
        validM3 = new Move(startingSquare, tmp5);
        if(!piecePresent(((tmp5.getXC()*75)+20), (((tmp5.getYC()*75)+20)))){
          moves.push(validM3);
        }
        else{
          if(checkWhiteOponent(((tmp5.getXC()*75)+20), (((tmp5.getYC()*75)+20)))){
            moves.push(validM3);
          }
        }
      }
    }
  }
  Square tmp7 = new Square(x, tmpy1, piece);
  Square tmp8 = new Square(x, tmpy2, piece);
  if(!(tmpy1 > 7)){
    if(checkSurroundingSquares(tmp7)){
      validM2 = new Move(startingSquare, tmp7);
      if(!piecePresent(((tmp7.getXC()*75)+20), (((tmp7.getYC()*75)+20)))){
        moves.push(validM2);
      }
      else{
        if(checkWhiteOponent(((tmp7.getXC()*75)+20), (((tmp7.getYC()*75)+20)))){
          moves.push(validM2);
        }
      }
    }
  }
  if(!(tmpy2 < 0)){
    if(checkSurroundingSquares(tmp8)){
      validM3 = new Move(startingSquare, tmp8);
      if(!piecePresent(((tmp8.getXC()*75)+20), (((tmp8.getYC()*75)+20)))){
        moves.push(validM3);
      }
      else{
        if(checkWhiteOponent(((tmp8.getXC()*75)+20), (((tmp8.getYC()*75)+20)))){
          moves.push(validM3);
        }
      }
    }
  }
  return moves;
} // end of the method getKingSquares()


/*
    Method to return all the possible moves that a Queen can make
*/
private Stack getQueenMoves(int x, int y, String piece){
  Stack completeMoves = new Stack();
  Stack tmpMoves = new Stack();
  Move tmp;
  /*
      The Queen is a pretty easy piece to figure out if you have completed the
      Bishop and the Rook movements. Either the Queen is going to move like a
      Bishop or its going to move like a Rook, so all we have to do is make a call to both of these
      methods.
  */
  tmpMoves = getRookMoves(x, y, piece);
  while(!tmpMoves.empty()){
    tmp = (Move)tmpMoves.pop();
    completeMoves.push(tmp);
  }
  tmpMoves = getBishopMoves(x, y, piece);
  while(!tmpMoves.empty()){
    tmp = (Move)tmpMoves.pop();
    completeMoves.push(tmp);
  }
  return completeMoves;
}

/*
  Method to return all the squares that a Rook can move to. The Rook can either move in an x direction or
  in a y direction as long as there is nothing in the way and it can take its opponents piece but not its
  own piece. As seen in the below grid the Rook can either move in a horizontal direction (x changing value)
  or in a vertical movement (y changing direction)

                            _|_____________|___________|_________|___________|___________|_
                             |             |           |         |           |           |
                             |             |           |(x, y-N) |           |           |
                            _|_____________|___________|_________|___________|___________|_
                             |             |           |         |           |           |
                             |             |           |(x, y-2) |           |           |
                            _|_____________|___________|_________|___________|___________|_
                             |             |           |         |           |           |
                             |             |           |(x, y-1) |           |           |
                            _|_____________|___________|_________|___________|___________|_
                             |             |           |         |           |           |
                             | (x-N, y)    |(x-1, y)   | (x, y)  |(x+1, y)   |(x+N, y)   |
                            _|_____________|___________|_________|___________|___________|_
                             |             |           |         |           |           |
                             |             |           | (x, y+1)|           |           |
                            _|_____________|___________|_________|___________|___________|_
                             |             |           |         |           |           |
                             |             |           |(x, y+2) |           |           |
                            _|_____________|___________|_________|___________|___________|_
                             |             |           |         |           |           |
                             |             |           |(x, y+N) |           |           |
                            _|_____________|___________|_________|___________|___________|_
                             |             |           |         |           |           |
*/
private Stack getRookMoves(int x, int y, String piece){
  Square startingSquare = new Square(x, y, piece);
  Stack moves = new Stack();
  Move validM, validM2, validM3, validM4;
  /*
    There are four possible directions that the Rook can move to:
      - the x value is increasing
      - the x value is decreasing
      - the y value is increasing
      - the y value is decreasing

    Each of these movements should be catered for. The loop guard is set to incriment up to the maximun number of squares.
    On each iteration of the first loop we are adding the value of i to the current x coordinate.
    We make sure that the new potential square is going to be on the board and if it is we create a new square and a new potential
    move (originating square, new square).If there are no pieces present on the potential square we simply add it to the Stack
    of potential moves.
    If there is a piece on the square we need to check if its an opponent piece. If it is an opponent piece its a valid move, but we
    must break out of the loop using the Java break keyword as we can't jump over the piece and search for squares. If its not
    an opponent piece we simply break out of the loop.

    This cycle needs to happen four times for each of the possible directions of the Rook.
  */
  for(int i=1;i < 8;i++){
    int tmpx = x+i;
    int tmpy = y;
    if(!(tmpx > 7 || tmpx < 0)){
      Square tmp = new Square(tmpx, tmpy, piece);
      validM = new Move(startingSquare, tmp);
      if(!piecePresent(((tmp.getXC()*75)+20), (((tmp.getYC()*75)+20)))){
        moves.push(validM);
      }
      else{
        if(checkWhiteOponent(((tmp.getXC()*75)+20), ((tmp.getYC()*75)+20))){
          moves.push(validM);
          break;
        }
        else{
          break;
        }
      }
    }
  }//end of the loop with x increasing and Y doing nothing...
  for(int j=1;j < 8;j++){
    int tmpx1 = x-j;
    int tmpy1 = y;
    if(!(tmpx1 > 7 || tmpx1 < 0)){
      Square tmp2 = new Square(tmpx1, tmpy1, piece);
      validM2 = new Move(startingSquare, tmp2);
      if(!piecePresent(((tmp2.getXC()*75)+20), (((tmp2.getYC()*75)+20)))){
        moves.push(validM2);
      }
      else{
        if(checkWhiteOponent(((tmp2.getXC()*75)+20), ((tmp2.getYC()*75)+20))){
          moves.push(validM2);
          break;
        }
        else{
          break;
        }
      }
    }
  }//end of the loop with x increasing and Y doing nothing...
  for(int k=1;k < 8;k++){
    int tmpx3 = x;
    int tmpy3 = y+k;
    if(!(tmpy3 > 7 || tmpy3 < 0)){
      Square tmp3 = new Square(tmpx3, tmpy3, piece);
      validM3 = new Move(startingSquare, tmp3);
      if(!piecePresent(((tmp3.getXC()*75)+20), (((tmp3.getYC()*75)+20)))){
        moves.push(validM3);
      }
      else{
        if(checkWhiteOponent(((tmp3.getXC()*75)+20), ((tmp3.getYC()*75)+20))){
          moves.push(validM3);
          break;
        }
        else{
          break;
        }
      }
    }
  }//end of the loop with x increasing and Y doing nothing...
  for(int l=1;l < 8;l++){
    int tmpx4 = x;
    int tmpy4 = y-l;
    if(!(tmpy4 > 7 || tmpy4 < 0)){
      Square tmp4 = new Square(tmpx4, tmpy4, piece);
      validM4 = new Move(startingSquare, tmp4);
      if(!piecePresent(((tmp4.getXC()*75)+20), (((tmp4.getYC()*75)+20)))){
        moves.push(validM4);
      }
      else{
        if(checkWhiteOponent(((tmp4.getXC()*75)+20), ((tmp4.getYC()*75)+20))){
          moves.push(validM4);
          break;
        }
        else{
          break;
        }
      }
    }
  }//end of the loop with x increasing and Y doing nothing...
  return moves;
}// end of get Rook Moves.

/*
  Method to return all the squares that a Bishop can move to. As seen in the below grid, the Bishop
  can move in a diagonal moement. There are essentially four different directions from a single
  square that the Bishop can move along. The Bishop can move any distance along this diagonal
  as long as there is nothing in the way. The Bishop can also take an opponent piece but cannot take its
  own piece.


                              _|_____________|___________|_________|___________|___________|_
                               |             |           |         |           |           |
                               |             |           |         |           |           |
                              _|_____________|___________|_________|___________|___________|_
                               |             |           |         |           |           |
                               | (x-N, y-N)  |           |         |           |(x+N, y-N) |
                              _|_____________|___________|_________|___________|___________|_
                               |             |           |         |           |           |
                               |             | (x-1, y-1)|         | (x+1, y-1)|           |
                              _|_____________|___________|_________|___________|___________|_
                               |             |           |         |           |           |
                               |             |           | (x, y)  |           |           |
                              _|_____________|___________|_________|___________|___________|_
                               |             |           |         |           |           |
                               |             |(x-1, y+1) |         | (x+1, y+1)|           |
                              _|_____________|___________|_________|___________|___________|_
                               |             |           |         |           |           |
                               |(x-N, y+N)   |           |         |           |(x+N, y+N) |
                              _|_____________|___________|_________|___________|___________|_
                               |             |           |         |           |           |
                               |             |           |         |           |           |
                              _|_____________|___________|_________|___________|___________|_
                               |             |           |         |           |           |

*/
private Stack getBishopMoves(int x, int y, String piece){
  Square startingSquare = new Square(x, y, piece);
  Stack moves = new Stack();
  Move validM, validM2, validM3, validM4;
  /*
    The Bishop can move along any diagonal until it hits an enemy piece or its own piece
    it cannot jump over its own piece. We need to use four different loops to go through the possible movements
    to identify possible squares to move to. The temporary squares, i.e. the values of x and y must change by the
    same amount on each iteration of each of the loops.

    If the new values of x and y are on the board, we create a new square and a new move (from the original square to the new
    square). We then check if there is a piece present on the new square:
    - if not we add the move as a possible new move
    - if there is a piece we make sure that we can capture our opponents piece and we cannot take our own piece
      and then we break out of the loop

    This process is repeated for each of the other three possible diagonals that the Bishop can travel along.

  */
  for(int i=1;i < 8;i++){
    int tmpx = x+i;
    int tmpy = y+i;
    if(!(tmpx > 7 || tmpx < 0 || tmpy > 7 || tmpy < 0)){
      Square tmp = new Square(tmpx, tmpy, piece);
      validM = new Move(startingSquare, tmp);
      if(!piecePresent(((tmp.getXC()*75)+20), (((tmp.getYC()*75)+20)))){
        moves.push(validM);
      }
      else{
        if(checkWhiteOponent(((tmp.getXC()*75)+20), ((tmp.getYC()*75)+20))){
          moves.push(validM);
          break;
        }
        else{
          break;
        }
      }
    }
  } // end of the first for Loop
  for(int k=1;k < 8;k++){
    int tmpk = x+k;
    int tmpy2 = y-k;
    if(!(tmpk > 7 || tmpk < 0 || tmpy2 > 7 || tmpy2 < 0)){
      Square tmpK1 = new Square(tmpk, tmpy2, piece);
      validM2 = new Move(startingSquare, tmpK1);
      if(!piecePresent(((tmpK1.getXC()*75)+20), (((tmpK1.getYC()*75)+20)))){
        moves.push(validM2);
      }
      else{
        if(checkWhiteOponent(((tmpK1.getXC()*75)+20), ((tmpK1.getYC()*75)+20))){
          moves.push(validM2);
          break;
        }
        else{
          break;
        }
      }
    }
  } //end of second loop.
  for(int l=1;l < 8;l++){
    int tmpL2 = x-l;
    int tmpy3 = y+l;
    if(!(tmpL2 > 7 || tmpL2 < 0 || tmpy3 > 7 || tmpy3 < 0)){
      Square tmpLMov2 = new Square(tmpL2, tmpy3, piece);
      validM3 = new Move(startingSquare, tmpLMov2);
      if(!piecePresent(((tmpLMov2.getXC()*75)+20), (((tmpLMov2.getYC()*75)+20)))){
        moves.push(validM3);
      }
      else{
        if(checkWhiteOponent(((tmpLMov2.getXC()*75)+20), ((tmpLMov2.getYC()*75)+20))){
          moves.push(validM3);
          break;
        }
        else{
          break;
        }
      }
    }
  }// end of the third loop
  for(int n=1;n < 8;n++){
    int tmpN2 = x-n;
    int tmpy4 = y-n;
    if(!(tmpN2 > 7 || tmpN2 < 0 || tmpy4 > 7 || tmpy4 < 0)){
      Square tmpNmov2 = new Square(tmpN2, tmpy4, piece);
      validM4 = new Move(startingSquare, tmpNmov2);
      if(!piecePresent(((tmpNmov2.getXC()*75)+20), (((tmpNmov2.getYC()*75)+20)))){
        moves.push(validM4);
      }
      else{
        if(checkWhiteOponent(((tmpNmov2.getXC()*75)+20), ((tmpNmov2.getYC()*75)+20))){
          moves.push(validM4);
          break;
        }
        else{
          break;
        }
      }
    }
  }// end of the last loop
  return moves;
}


/*
    Method fo return all the squares that a Knight can attack. The knight is possibly the simplest piece
    to get possible movements from. The Knight can essentially move in an L direction from any square on the
    board as long as the landing square is on the board and we can take an opponents piece but not our own piece.
*/
private Stack getKnightMoves(int x, int y, String piece){
  Square startingSquare = new Square(x, y, piece);
  Stack moves = new Stack();
  Stack attackingMove = new Stack();
  Square s = new Square(x+1, y+2, piece);
  moves.push(s);
  Square s1 = new Square(x+1, y-2, piece);
  moves.push(s1);
  Square s2 = new Square(x-1, y+2, piece);
  moves.push(s2);
  Square s3 = new Square(x-1, y-2, piece);
  moves.push(s3);
  Square s4 = new Square(x+2, y+1, piece);
  moves.push(s4);
  Square s5 = new Square(x+2, y-1, piece);
  moves.push(s5);
  Square s6 = new Square(x-2, y+1, piece);
  moves.push(s6);
  Square s7 = new Square(x-2, y-1, piece);
  moves.push(s7);

  for(int i=0;i < 8;i++){
    Square tmp = (Square)moves.pop();
    Move tmpmove = new Move(startingSquare, tmp);
    if((tmp.getXC() < 0)||(tmp.getXC() > 7)||(tmp.getYC() < 0)||(tmp.getYC() > 7)){

    }
    else if(piecePresent(((tmp.getXC()*75)+20), (((tmp.getYC()*75)+20)))){
      if(piece.contains("White")){
        if(checkWhiteOponent(((tmp.getXC()*75)+20), ((tmp.getYC()*75)+20))){
          attackingMove.push(tmpmove);
        }
      }
    }
    else{
      attackingMove.push(tmpmove);
    }
  }
  return attackingMove;
}


/*
        Method to colour a stack of Squares
*/
private void colorSquares(Stack squares){
  Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 3);
  while(!squares.empty()){
    Square s = (Square)squares.pop();
    int location = s.getXC() + ((s.getYC())*8);
    JPanel panel = (JPanel)chessBoard.getComponent(location);
    panel.setBorder(greenBorder);
  }
}

/*
    Method to get the landing square of a bunch of moves...
*/
private void getLandingSquares(Stack found){
  Move tmp;
  Square landing;
  Stack squares = new Stack();
  while(!found.empty()){
    tmp = (Move)found.pop();
    landing = (Square)tmp.getLanding();
    squares.push(landing);
  }
  colorSquares(squares);
}


/*
  Method to find all the White Pieces.
*/
  private Stack findWhitePieces(){
    Stack squares = new Stack();
    String icon;
    int x;
    int y;
    String pieceName;
    for(int i=0;i < 600;i+=75){
      for(int j=0;j < 600;j+=75){
        y = i/75;
        x=j/75;
        Component tmp = chessBoard.findComponentAt(j, i);
        if(tmp instanceof JLabel){
          chessPiece = (JLabel)tmp;
          icon = chessPiece.getIcon().toString();
          pieceName = icon.substring(0, (icon.length()-4));
          if(pieceName.contains("White")){
            Square stmp = new Square(x, y, pieceName);
            squares.push(stmp);
          }
        }
      }
    }
    return squares;
  }
  // this method is looking for all the black pieces
  private Stack findBlackPieces() {
    Stack squares = new Stack();
    String icon;
    int x;
    int y;
    String pieceName;
    for (int i = 0; i < 600; i += 75) {
        for (int j = 0; j < 600; j += 75) {
            y = i / 75;
            x = j / 75;
            Component tmp = chessBoard.findComponentAt(j, i);
            if (tmp instanceof JLabel) {
                chessPiece = (JLabel) tmp;
                icon = chessPiece.getIcon().toString();
                pieceName = icon.substring(0, (icon.length() - 4));
                if (pieceName.contains("Black")) {
                    Square stmp = new Square(x, y, pieceName);
                    squares.push(stmp);
                }
            }
        }
    }
    return squares;
}
	/*
		This method checks if there is a piece present on a particular square.
	*/


	/*
		This is a method to check if a piece is a Black piece.
	*/

  private void resetBorders(){
    Border empty = BorderFactory.createEmptyBorder();
    for(int i=0;i < 64;i++){
      JPanel tmppanel = (JPanel)chessBoard.getComponent(i);
      tmppanel.setBorder(empty);
    }
  }

  /*
    The method printStack takes in a Stack of Moves and prints out all possible moves.
  */
private void printStack(Stack input){
  Move m;
  Square s, l;
  while(!input.empty()){
    m = (Move)input.pop();
    s = (Square)m.getStart();
    l = (Square)m.getLanding();
    System.out.println("The possible move that was found is : ("+s.getXC()+" , "+s.getYC()+"), landing at ("+l.getXC()+" , "+l.getYC()+")");
  }
}

  private void makeAIMove(){
    /*
      When the AI Agent decides on a move, a red border shows the square from where the move started and the
      landing square of the move.
    */
    resetBorders();
    layeredPane.validate();
    layeredPane.repaint();
    Stack white = findWhitePieces();
    Stack blackpieces = findBlackPieces();
    Stack completeMoves = new Stack();
    Move tmp;
    while(!white.empty()){
      Square s = (Square)white.pop();
      String tmpString = s.getName();
      Stack tmpMoves = new Stack();
      Stack temporary = new Stack();
      /*
          We need to identify all the possible moves that can be made by the AI Opponent
      */
      if(tmpString.contains("Knight")){
        tmpMoves = getKnightMoves(s.getXC(), s.getYC(), s.getName());
      }
      else if(tmpString.contains("Bishop")){
        tmpMoves = getBishopMoves(s.getXC(), s.getYC(), s.getName());
      }
      else if(tmpString.contains("Pawn")){
        tmpMoves = getWhitePawnSquares(s.getXC(), s.getYC(), s.getName());
      }
      else if(tmpString.contains("Rook")){
        tmpMoves = getRookMoves(s.getXC(), s.getYC(), s.getName());
      }
      else if(tmpString.contains("Queen")){
        tmpMoves = getQueenMoves(s.getXC(), s.getYC(), s.getName());
      }
      else if(tmpString.contains("King")){
        tmpMoves = getKingSquares(s.getXC(), s.getYC(), s.getName());
        if(tmpMoves.isEmpty()){
          System.out.println("game over");
        }
      }

      while(!tmpMoves.empty()){
        tmp = (Move)tmpMoves.pop();
        completeMoves.push(tmp);
      }
    }
    temporary = (Stack)completeMoves.clone();
    getLandingSquares(temporary);
    printStack(temporary);
/*
  So now we should have a copy of all the possible moves to make in our Stack called completeMoves
*/
  if(completeMoves.size() == 0){
/*
      In Chess if you cannot make a valid move but you are not in Check this state is referred to
      as a Stale Mate
*/

  JOptionPane.showMessageDialog(null, "Cogratulations, you have placed the AI component in a Stale Mate Position");
  System.exit(0);

  }
  else{
    /*
      Okay, so we can make a move now. We have a stack of all possible moves and need to call the correct agent to select
      one of these moves. Lets print out the possible moves to the standard output to view what the options are for
      White. Later when you are finished the continuous assessment you don't need to have such information being printed
      out to the standard output.
    */
     
      Stack testing = new Stack(); // testing is the list of all the white AI moves
      while(!completeMoves.empty()){
        Move tmpMove = (Move)completeMoves.pop();
        Square s1 = (Square)tmpMove.getStart();
        Square s2 = (Square)tmpMove.getLanding();
        System.out.println("The "+s1.getName()+" can move from ("+s1.getXC()+", "+s1.getYC()+") to the following square: ("+s2.getXC()+", "+s2.getYC()+")");
        testing.push(tmpMove);
      }
    Move selectedMove = null;
       Border redBorder = BorderFactory.createLineBorder(Color.RED, 3); // this below is the buttons for the AI Agent menu functionality
       if (selected ==0){
        selectedMove = agent.randomMove(testing);
       }
       else if(selected ==1){
         selectedMove = agent.nextBestMove(testing, blackpieces);
       }
    
     
       Square startingPoint = (Square)selectedMove.getStart();
       Square landingPoint = (Square)selectedMove.getLanding();
       int startX1 = (startingPoint.getXC()*75)+20;
       int startY1 = (startingPoint.getYC()*75)+20;
       int landingX1 = (landingPoint.getXC()*75)+20;
       int landingY1 = (landingPoint.getYC()*75)+20;
       System.out.println("-------- Move "+startingPoint.getName()+" ("+startingPoint.getXC()+", "+startingPoint.getYC()+") to ("+landingPoint.getXC()+", "+landingPoint.getYC()+")");

       Component c  = (JLabel)chessBoard.findComponentAt(startX1, startY1);
       Container parent = c.getParent();
       parent.remove(c);
       int panelID = (startingPoint.getYC() * 8)+startingPoint.getXC();
       panels = (JPanel)chessBoard.getComponent(panelID);
       panels.setBorder(redBorder);
       parent.validate();

       Component l = chessBoard.findComponentAt(landingX1, landingY1);
       if(l instanceof JLabel){
          Container parentlanding = l.getParent();
          JLabel awaitingName = (JLabel)l;
          String agentCaptured = awaitingName.getIcon().toString();
          if(agentCaptured.contains("King")){
            agentwins = true;
          }
          parentlanding.remove(l);
          parentlanding.validate();
          pieces = new JLabel( new ImageIcon(startingPoint.getName()+".png") );
          int landingPanelID = (landingPoint.getYC()*8)+landingPoint.getXC();
          panels = (JPanel)chessBoard.getComponent(landingPanelID);
          panels.add(pieces);
          panels.setBorder(redBorder);
          layeredPane.validate();
          layeredPane.repaint();

          if(agentwins){
            JOptionPane.showMessageDialog(null, "The AI Agent has won!");
            System.exit(0);
          }
        
         //if the AI pawn makes it across the board promote him to white queen on a capture move
         if (startingPoint.getName().contains("Pawn") && landingPoint.getYC() == 7) {
          parentlanding.remove(0);
          pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
          landingPanelID = (landingPoint.getYC() * 8) + landingPoint.getXC();
          panels = (JPanel) chessBoard.getComponent(landingPanelID);
          panels.add(pieces);
      }

  
        
    } else {
      pieces = new JLabel(new ImageIcon(startingPoint.getName() + ".png"));
      int landingPanelID = (landingPoint.getYC() * 8) + landingPoint.getXC();
      panels = (JPanel) chessBoard.getComponent(landingPanelID);
      panels.add(pieces);
      panels.setBorder(redBorder);
      layeredPane.validate();
      layeredPane.repaint();
    //if the AI pawn makes it across the board to an empty square, make it queen
    if (c instanceof JLabel) {
      if (startingPoint.getName().contains("Pawn") && landingPoint.getYC() == 7) {
          Component x = (JLabel) chessBoard.findComponentAt(landingX1, landingY1);
          Container parentX = x.getParent();
          parentX.remove(x);
          pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
          panels = (JPanel) chessBoard.getComponent(landingPanelID);
          panels.add(pieces);
      }
  }

}
white2Move = false;
}
}


	/*
		This method checks if there is a piece present on a particular square.
	*/
	private Boolean piecePresent(int x, int y){
		Component c = chessBoard.findComponentAt(x, y);
		if(c instanceof JPanel){
			return false;
		}
		else{
			return true;
		}
	}

	/*
		This is a method to check if a piece is a Black piece.
	*/
  private Boolean checkWhiteOponent(int newX, int newY){   // a method called checkWhiteOponent that takes as an argument the coordinates of a position and checks if there is an opponent’s piece present on the square
   Boolean oponent;
   Component c1 = chessBoard.findComponentAt(newX, newY);
   JLabel awaitingPiece = (JLabel)c1;
   String tmp1 = awaitingPiece.getIcon().toString();
   if (((tmp1.contains("Black")))) {
    oponent = true;
} else {
    oponent = false;
}
return oponent;
}

 private Boolean checkBlackOponent(int newX, int newY){   /* a method called checkBlackOponent that takes as an argument the coordinates
                                               of a position and checks if there is an opponent’s piece present on the square*/
   Boolean oponent;
   Component c1 = chessBoard.findComponentAt(newX, newY);
   JLabel awaitingPiece = (JLabel)c1;
   String tmp1 = awaitingPiece.getIcon().toString();
   if (((tmp1.contains("White")))) {
            oponent = true;
        } else {
            oponent = false;
        }
        return oponent;
    }

 private String GetPieceName(int newX, int newY){    // created a method called GetPieceName
   Component c = chessBoard.findComponentAt(newX, newY); // the method get pieceName looks for Components on the chessBoard at these coordinates(int newX, int newY)//
   if(c instanceof JLabel){  // if c is a picture then run this code inside the brackets
     JLabel awaitingPiece = (JLabel) c;  // it casts Component into a JLabel
     String tmp1 = awaitingPiece.getIcon().toString(); // converts the icon text into a string
     return tmp1; // returns tmp1
   }
   else{
     return "";
   }
 }

 //

 private Boolean IsKingAdjacent(int newX, int newY){ // this method makes sure that we cannot move the king directly into a piece that is going to attack it
    if((piecePresent(newX, newY+75)&& GetPieceName(newX, newY+75).contains("King")||(piecePresent(newX, newY-75)&& GetPieceName(newX, newY-75).contains("King")))){
      return  true;  // if a piece is present and only if it is a king piece then return true
      // the code below calculates all the directions
    }
    else if((piecePresent(newX+75,newY)&& GetPieceName(newX+75, newY).contains("King")||(piecePresent(newX-75, newY)&& GetPieceName(newX-75, newY).contains("King")))){
      return true;
    }
    else if((piecePresent(newX-75, newY+75)&& GetPieceName(newX-75, newY+75).contains("King")||(piecePresent(newX+75, newY-75)&& GetPieceName(newX+75, newY-75).contains("King")))){
      return  true;
    }
    else if((piecePresent(newX-75, newY-75)&& GetPieceName(newX-75, newY-75).contains("King")||(piecePresent(newX+75, newY+75)&& GetPieceName(newX+75, newY+75).contains("King")))){
      return  true;
    }
    return false;
  }



	/*
		This method is called when we press the Mouse. So we need to find out what piece we have
		selected. We may also not have selected a piece!
	*/
  //Mouse pressed method
    public void mousePressed(MouseEvent e){
    chessPiece = null;
    Component c =  chessBoard.findComponentAt(e.getX(), e.getY());
    if (c instanceof JPanel)
		return;

    Point parentLocation = c.getParent().getLocation();
    xAdjustment = parentLocation.x - e.getX();
    yAdjustment = parentLocation.y - e.getY();
    chessPiece = (JLabel)c;
		initialX = e.getX();
		initialY = e.getY();
		startX = (e.getX()/75);
		startY = (e.getY()/75);
    chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
    chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
    layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);
    }
	//Mouse dragged method
    public void mouseDragged(MouseEvent me) {
    if (chessPiece == null) {
    return;
    }
    chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
     }
	//Mouse released method
    public void mouseReleased(MouseEvent e) {
    if(chessPiece == null) return;{
    }

    chessPiece.setVisible(false);
		Boolean success =false;
		Boolean progression =false;
    Component c =  chessBoard.findComponentAt(e.getX(), e.getY());
		String tmp = chessPiece.getIcon().toString();
		String pieceName = tmp.substring(0, (tmp.length()-4));
		Boolean validMove = false;

		int landingX = (e.getX()/75);
		int landingY = (e.getY()/75);
		int xMovement = Math.abs((e.getX()/75)-startX);
		int yMovement = Math.abs((e.getY()/75)-startY);
		System.out.println("----------------------------");
		System.out.println("The piece that is being moved is :"+pieceName);
		System.out.println("The starting coordinates are : "+"("+startX+","+startY+")");
		System.out.println("The xMovement is : "+xMovement);
		System.out.println("The yMovement is : "+yMovement);
		System.out.println("The starting coordinates are : "+"("+landingX+","+landingY+")");
		System.out.println("----------------------------");
  
		Boolean blackTurn = true;

    if(whiteTurn){
  		if(pieceName.contains("White") && !(xMovement == 0 && yMovement == 0)){
  			blackTurn = true; 
  		}
  	}
  	else{
  		if(pieceName.contains("Black") && !(xMovement == 0 && yMovement == 0)){
  			blackTurn = true;
  		}
  	}
     if(blackTurn){






		/*
			The only piece that has been enabled to move is a White Pawn...but we should really have this is a separate
			method somewhere...how would this work.

			So a Pawn is able to move two squares forward one its first go but only one square after that.
			The Pawn is the only piece that cannot move backwards in chess...so be careful when committing
			a pawn forward. A Pawn is able to take any of the opponent’s pieces but they have to be one
			square forward and one square over, i.e. in a diagonal direction from the Pawns original position.
			If a Pawn makes it to the top of the other side, the Pawn can turn into any other piece, for
			demonstration purposes the Pawn here turns into a Queen.
		*/
    //Black Pawn
    if(pieceName.equals("BlackPawn")){ /*Here we are saying if the piece is a “Black Pawn do something */
      if(startY == 6){                     /*if the pawn is making its first move, the pawn can either move one or two squares in the Y direction as long as we are moving up the board,
                                      and if there is no movement in the x direction */
   if(((yMovement==1)||(yMovement==2))&&(startY > landingY)&& (xMovement == 0)){ /* restrictions are being added toensure that the piece acts like a pawn.
                                                                                      Two variables have been added xMovement and yMovement which
                                                                                      give the coordinates of were the piece is being dropped onto the board*/
     if(yMovement==2){
       if((!piecePresent(e.getX(), e.getY()))&&(!piecePresent(e.getX(), (e.getY()+75)))){  /* we are calling a method called piecePresent and passing in
                                                                                            some coordinates. The code is being executed in the mouseReleased(MouseEvent e) method and we
                                                                                            can capture the location of the position using the e.getX() and e.getY() methods*/
         validMove=true;
       }
     }
     else{
       if(!piecePresent(e.getX(), e.getY())){
         validMove = true;
       }
     }
   }
   else if((yMovement == 1)&&(startY > landingY)&& (xMovement ==1)){
     if(piecePresent(e.getX(), e.getY())){
       if(checkBlackOponent(e.getX(),e.getY())){   /* a method called checkBlackOponent that takes as an argument the coordinates
                                                     of a position and checks if there is an opponent’s piece present on the square*/
         validMove=true;
         if (GetPieceName(e.getX(), e.getY()).contains("King")) {
          winner = "The Black Player has won the game!";
        }
       }
     }
   }
 }
 else{ // this is where the pawn is making all subqueen moves
   if(((yMovement==1))&&(startY > landingY)&&(xMovement ==0)){
     if(!piecePresent(e.getX(), e.getY())){
       validMove = true;
       if(landingY==0){
         progression=true;
       }
     }
   }
   else if((yMovement ==1)&&(startY > landingY)&& (xMovement ==1)){
     if(piecePresent(e.getX(), e.getY())){
       if(checkBlackOponent(e.getX(),e.getY())){   /* a method called checkBlackOponent that takes as an argument the coordinates
                                                     of a position and checks if there is an opponent’s piece present on the square*/
         validMove=true;
         if(landingY==0){
           progression=true;
         }
         if (GetPieceName(e.getX(), e.getY()).contains("King")) {
          winner = "The Black Player has won the game!";
        }
       }
     }
   }

 else{
   if(progression){
     int location = 0 + (e.getX()/75);
     if (c instanceof JLabel){
       Container parent = c.getParent();
       parent.remove(0);
       pieces = new JLabel( new ImageIcon("BlackQueen.png"));
       parent = (JPanel)chessBoard.getComponent(location);
       parent.add(pieces);
     }
   }
   else if(success){

   }
 }
    			}//End Black Pawn


  }        //White Pawn
    		else if (pieceName.equals("WhitePawn")) {
    				if (startY == 1) {
    					//1 or 2 Square move
    					if (((xMovement == 0)) && ((yMovement == 1) || ((yMovement) == 2))) {
    						if (yMovement == 2) {
    							if ((!piecePresent(e.getX(), (e.getY()))) && (!piecePresent(e.getX(), (e.getY() - 75)))) {
    								validMove = true;
    							}
    						}
    						else if ((!piecePresent(e.getX(), (e.getY())))) {
    							validMove = true;
    						}
    					}
    					//Checking for opponent
    					else if ((piecePresent(e.getX(), e.getY())) && (xMovement == yMovement) && (xMovement == 1) && (startY < landingY)) {
    						if (checkWhiteOponent(e.getX(), e.getY())) {
    							validMove = true;
    							if (startY == 6) {
    								success = true;
    							}
                  if (GetPieceName(e.getX(), e.getY()).contains("King")) {
                    winner = "The White AI has won!";
                }
    						}
    					}
    				}
    				else if ((startX - 1 >= 0) || (startX + 1 <= 7)) {
    					if ((piecePresent(e.getX(), e.getY())) && (xMovement == yMovement) && (xMovement == 1)) {
    						if (checkWhiteOponent(e.getX(), e.getY())) {  /* a method called checkWhiteOponent that takes as an argument the coordinates
                                                              of a position and checks if there is an opponent’s piece present on the square*/
    							validMove = true;
    							if (startY == 6) {
    								success = true;
    							}
                  if (GetPieceName(e.getX(), e.getY()).contains("King")) {
                    winner = "The White AI has won!";
                }
    						}
    					}
    					else if (!piecePresent(e.getX(), (e.getY()))) {
    						if (((xMovement == 0)) && ((e.getY() / 75) - startY) == 1) {
    							if (startY == 6) {
    								success = true;
    							}
    							validMove = true;
    						}
    					}
    				}
    			}//End White Pawn
          // Knight
    else if (pieceName.contains("Knight")) {
      if (((landingX < 0) || (landingX > 7)) || ((landingY < 0) || landingY > 7)) {
        validMove = false;
      } else {
        // L shaped movements for the knight
        if (((landingX == startX + 1) && (landingY == startY + 2)) || ((landingX == startX - 1) && (landingY == startY + 2)) || ((landingX ==
            startX + 2) && (landingY == startY + 1)) || ((landingX == startX - 2) && (landingY == startY + 1)) || ((landingX == startX + 1) &&
            (landingY == startY - 2)) || ((landingX == startX - 1) && (landingY == startY - 2)) || ((landingX == startX + 2) && (landingY ==
            startY - 1)) || ((landingX == startX - 2) && (landingY == startY - 1))) {
          if (piecePresent(e.getX(), (e.getY()))) {
            //Checking if you can take an opponent piece
            if (pieceName.contains("White")) {
              if (checkWhiteOponent(e.getX(), e.getY())) { /* a method called checkWhiteOponent that takes as an argument the coordinates
                                                            of a position and checks if there is an opponent’s piece present on the square*/
                validMove = true;
                if (GetPieceName(e.getX(), e.getY()).contains("King")) {
                  winner = "The White AI has won the game!";
              }
              }
              
            
            }
            else {
              if (checkBlackOponent(e.getX(), e.getY())) {   /* a method called checkBlackOponent that takes as an argument the coordinates
                                                            of a position and checks if there is an opponent’s piece present on the square*/
                validMove = true;
                if (GetPieceName(e.getX(), e.getY()).contains("King")) {
                  winner = "The Black Player has won the game!";
              }

              }
              else {
                validMove = false;
              }
            }
          }
          else {
            validMove = true;
          }
        }
        else {
          validMove = false;
        }
      }
    }//End Knight
    //start of Bishop

    /*
      * The Bishop can move along any diagonal until it hits an enemy piece or its
      * own piece it cannot jump over its own piece. We need to use four different
      * loops to go through the possible movements to identify possible squares to
      * move to. The temporary squares, i.e. the values of x and y must change by the
      * same amount on each iteration of each of the loops. */

    if (pieceName.contains("Bishup")) {
				//Checking if there is a piece in the bishops path
				Boolean inTheWay = false;
				int distance = Math.abs(startX - landingX);
				if (((landingX < 0) || (landingX > 7)) || ((landingY < 0) || (landingY > 7))) {
					validMove = false;
				} else {
					validMove = true;
					// Bishops diagonal movements
					if (Math.abs(startX - landingX) == Math.abs(startY - landingY)) {
						if ((startX - landingX < 0) && (startY - landingY < 0)) {
							for (int i = 0; i < distance; i++) {
								if (piecePresent((initialX + (i * 75)), (initialY + (i * 75)))) {
									inTheWay = true;
								}
							}
						} else if ((startX - landingX < 0) && (startY - landingY > 0)) {
							for (int i = 0; i < distance; i++) {
								if (piecePresent((initialX + (i * 75)), (initialY - (i * 75)))) {
									inTheWay = true;
								}
							}
						} else if ((startX - landingX > 0) && (startY - landingY > 0)) {
							for (int i = 0; i < distance; i++) {
								if (piecePresent((initialX - (i * 75)), (initialY - (i * 75)))) {
									inTheWay = true;
								}
							}
						} else if ((startX - landingX > 0) && (startY - landingY < 0)) {
							for (int i = 0; i < distance; i++) {
								if (piecePresent((initialX - (i * 75)), (initialY + (i * 75)))) {
									inTheWay = true;
								}
							}
						}

						if (inTheWay) {
							validMove = false;
						} else {
							if (piecePresent(e.getX(), (e.getY()))) {
								if (pieceName.contains("White")) {
									if (checkWhiteOponent(e.getX(), e.getY())) {       /* a method called checkWhiteOponent that takes as an argument the coordinates
                                                                of a position and checks if there is an opponent’s piece present on the square*/
										validMove = true;
                    if (GetPieceName(e.getX(), e.getY()).contains("King")) {
                      winner = "The White AI has won the game!";
                  }
									} else {
										validMove = false;
									}
                } else if (checkBlackOponent(e.getX(), e.getY())) {
                  validMove = true;
                  if (GetPieceName(e.getX(), e.getY()).contains("King")) {
                      winner = "The Black Player has won the game!";
                  }


								}
							}
							else {
								validMove = true;
							}
						}
					}
					else {
						validMove = false;
					}
				}
			}//End Bishop
// The Rook is a piece that moves in either a horizontal or vertical movement. It can move any
//number of squares but cannot pass through a piece. Like all Chess pieces it can take an opponent
//piece but not its own piece.
// once the rook moves in one direction it can jump or cross other pieces
      if (pieceName.contains("Rook")) {
				Boolean intheway = false;
				if (((landingX < 0) || (landingX > 7)) || ((landingY < 0) || (landingY > 7))) {
					validMove = false;
				} else {
					//Rooks linear movements
					if (((Math.abs(startX - landingX) != 0) && (Math.abs(startY - landingY) == 0)) || ((Math.abs(startX - landingX) == 0) && (Math.abs(landingY - startY) != 0))) {
						if (Math.abs(startX - landingX) != 0) {
							int xMovementRook = Math.abs(startX - landingX);
							if (startX - landingX > 0) {
								for (int i = 0; i < xMovement; i++) {
									if (piecePresent(initialX - (i * 75), e.getY())) {
										intheway = true;
										break;
									} else {
										intheway = false;
									}
								}
							} else {
								for (int i = 0; i < xMovement; i++) {
									if (piecePresent(initialX + (i * 75), e.getY())) {
										intheway = true;
										break;
									} else {
										intheway = false;
									}
								}
							}
						} else {
							int yMovementRook = Math.abs(startY - landingY);
							if (startY - landingY > 0) {
								for (int i = 0; i < yMovement; i++) {
									if (piecePresent(e.getX(), initialY - (i * 75))) {
										intheway = true;
										break;
									} else {
										intheway = false;
									}
								}
							} else {
								for (int i = 0; i < yMovement; i++) {
									if (piecePresent(e.getX(), initialY + (i * 75))) {
										intheway = true;
										break;
									} else {
										intheway = false;
									}
								}
							}
						}

            if (intheway) {
              validMove = false;
          } else if (piecePresent(e.getX(), (e.getY()))) {
              if (pieceName.contains("White")) {
                  if (checkWhiteOponent(e.getX(), e.getY())) {
                      validMove = true;

                      if (GetPieceName(e.getX(), e.getY()).contains("King")) {
                          winner = "The White AI has won the game!";
                      }
                  } else {
                      validMove = false;
                  }
              } else if (checkBlackOponent(e.getX(), e.getY())) {
                  validMove = true;
                  if (GetPieceName(e.getX(), e.getY()).contains("King")) {
                      winner = "The Black Player has won the game!";
                  }
              } else {
                  validMove = false;
              }
          } else {
              validMove = true;
          }
      } else {
          validMove = false;
      }
       } }//End Rook

      if(pieceName.contains("Queen")){
      boolean inTheWay = false;
      if (((landingX < 0) || (landingX > 7)) || ((landingY < 0) || (landingY > 7))) {
        validMove = false;
      }
      else{
        //queen can either move like a Bishop or a Rook
        //this is to move as Bishop
        if (xMovement == yMovement) {
            if ((startX - landingX < 0) && (startY - landingY < 0)) {
              for (int i = 0; i < xMovement; i++) {
                if (piecePresent((initialX + (i * 75)), (initialY + (i * 75)))) {
                  inTheWay = true;
                }
              }
            } else if ((startX - landingX < 0) && (startY - landingY > 0)) {
              for (int i = 0; i < xMovement; i++) {
                if (piecePresent((initialX + (i * 75)), (initialY - (i * 75)))) {
                  inTheWay = true;
                }
              }
            } else if ((startX - landingX > 0) && (startY - landingY > 0)) {
              for (int i = 0; i < xMovement; i++) {
                if (piecePresent((initialX - (i * 75)), (initialY - (i * 75)))) {
                  inTheWay = true;
                }
              }
            } else if ((startX - landingX > 0) && (startY - landingY < 0)) {
              for (int i = 0; i < xMovement; i++) {
                if (piecePresent((initialX - (i * 75)), (initialY + (i * 75)))) {
                  inTheWay = true;
                }
              }
            }

            if (inTheWay) {
              validMove = false;
            } else {
              // checking if its an enemy piece
              // if not, is it a  free space
              if (piecePresent(e.getX(), (e.getY()))) {
                if (pieceName.contains("White")) { //moving WhiteQueen
                  if (checkWhiteOponent(e.getX(), e.getY())) {  /* a method called checkWhiteOponent that takes as an argument the coordinates
                                                                of a position and checks if there is an opponent’s piece present on the square*/
                    validMove = true;
                    if (GetPieceName(e.getX(), e.getY()).contains("King")) {
                      winner = "The White AI has won the game!";
                  }


                  }
                } else { //moving BlackQueen
                  if (checkBlackOponent(e.getX(), e.getY())) {  /* a method called checkBlackOponent that takes as an argument the coordinates
                                                                of a position and checks if there is an opponent’s piece present on the square*/
                    validMove = true;
                    if (GetPieceName(e.getX(), e.getY()).contains("King")) {
                      winner = "The Black Player has won the game!";
                  }

                  }
                }
              } else {
                validMove = true;
              }
            }
          }// end of bishop movemnents from a queen perspective

          else if (((xMovement != 0) && (yMovement == 0)) || ((xMovement == 0) && (yMovement != 0))) {
          //now the queen is moving like a Rook
          if (xMovement != 0) {
            //  movement along the x axis
            if (startX - landingX > 0) {
              // to move in left direction, the value has to be > 0
              for (int i = 0; i < xMovement; i++) {
                if (piecePresent(initialX - (i * 75), e.getY())) {
                  inTheWay = true;
                  break;
                } else {
                  inTheWay = false;
                }
              }
            } else {
              for (int i = 0; i < xMovement; i++) {
                if (piecePresent(initialX + (i * 75), e.getY())) {
                  inTheWay = true;
                  break;
                } else {
                  inTheWay = false;
                }
              }
            }
          }
          else {
            //  movement along the y axis
            if (startY - landingY > 0) {
              // for the queen to move in left direction it can but  only if the value is > 0
              for (int i = 0; i < yMovement; i++) {
                if (piecePresent(e.getX(), initialY - (i * 75))) {
                  inTheWay = true;
                  break;
                } else {
                  inTheWay = false;
                }
              }
            } else {
              for (int i = 0; i < yMovement; i++) {
                if (piecePresent(e.getX(), initialY + (i * 75))) {
                  inTheWay = true;
                  break;
                } else {
                  inTheWay = false;
                }
              }
            }
          }

          if (inTheWay) {
            validMove = false;
        } else if (piecePresent(e.getX(), (e.getY()))) {
            if (pieceName.contains("White")) {
                if (checkWhiteOponent(e.getX(), e.getY())) {
                    validMove = true;
                    if (GetPieceName(e.getX(), e.getY()).contains("King")) {
                        winner = "Sorry, Better luck next time. The White AI has won the game!";
                    }
                }
            } else if (checkBlackOponent(e.getX(), e.getY())) {
                validMove = true;
                if (GetPieceName(e.getX(), e.getY()).contains("King")) {
                    winner = "The Black Player has won the game!";
                }
            }
        } else {
            validMove = true;
        }
    }
}}
    // End of Queen


    //King movement

    /* If moving king piececheck if piece is moving within boundaries of board if no, validMove = false
        elseif king is making any of these valid moves when check if theres a piece where its going to move to
        if youre moving a white king, check if theres an opponent piece where its going to move toif yes, then take itotherwise invalid move
        else if youre a black king, check if theres an opponent piece where its going to move to if yes, then take it
        otherwise invalid move otherwise if theres no piece where its moving to, its a valid move*/



  if(pieceName.contains("King")){
    if ((landingX < 0) ||(landingX >7) || (landingY < 0) || (landingY > 7)){ // checling to see if the piece is present on the board
      validMove = false;
  }  else{

    if (((xMovement == 1) && (yMovement == 1)) || // check if ther king is moving diagonal
        ((xMovement == 1) && (yMovement == 0)) || // moves left or right
        ((xMovement == 0) && (yMovement == 1))) { // moves upwards or downwards
          if(!(IsKingAdjacent(e.getX(),e.getY()))){ // if there is no king


        if(piecePresent(e.getX(),e.getY())){
          System.out.println("stepz");
            if(pieceName.contains("White")){
              System.out.println("stepw");
              if(checkWhiteOponent(getX(),e.getY())){  /* a method called checkWhiteOponent that takes as an argument the coordinates
                                                            of a position and checks if there is an opponent’s piece present on the square*/
                validMove = true;
                System.out.println("stepone");
              }
              else{
                validMove = false;
                System.out.println("steptwo");}

            }
            else if (pieceName.contains("Black")){
              if(checkBlackOponent(e.getX(),e.getY())){   /* a method called checkBlackOponent that takes as an argument the coordinates
                                                            of a position and checks if there is an opponent’s piece present on the square*/
                validMove = true;
              }
            } else validMove = false;
          } else validMove = true;
        }
      }
    }
}
}

if (!validMove) {
  int location = 0;
  if (startY == 0) {
      location = startX;
  } else {
      location = (startY * 8) + startX;
  }
  String pieceLocation = pieceName + ".png";
  pieces = new JLabel(new ImageIcon(pieceLocation));
  panels = (JPanel) chessBoard.getComponent(location);
  panels.add(pieces);
} else {
  if (white2Move) {
      white2Move = false;
  } else {
      white2Move = true;
  }

  if (progression) {
      int location = 0 + (e.getX() / 75);
      if (c instanceof JLabel) {
          Container parent = c.getParent();
          parent.remove(0);
          pieces = new JLabel(new ImageIcon("BlackQueen.png"));
          parent = (JPanel) chessBoard.getComponent(location);
          parent.add(pieces);
      } else {
          Container parent = (Container) c;
          pieces = new JLabel(new ImageIcon("BlackQueen.png"));
          parent = (JPanel) chessBoard.getComponent(location);
          parent.add(pieces);
      }
      if (winner != null) {
          JOptionPane.showMessageDialog(null, winner);
          System.exit(0);
      }
  } else if (success) {
      int location = 56 + (e.getX() / 75);
      if (c instanceof JLabel) {
          Container parent = c.getParent();
          parent.remove(0);
          pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
          parent = (JPanel) chessBoard.getComponent(location);
          parent.add(pieces);
      } else {
          Container parent = (Container) c;
          pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
          parent = (JPanel) chessBoard.getComponent(location);
          parent.add(pieces);
      }
  } else { 
    if (pieceName.contains("Black")){  // i can only move black pieces
      if (c instanceof JLabel) {
          Container parent = c.getParent();
          parent.remove(0);
          parent.add(chessPiece);
          chessPiece.setVisible(true);
      } else {
          Container parent = (Container) c;
          parent.add(chessPiece);
          chessPiece.setVisible(true);
      }
      chessPiece.setVisible(true);
      if (winner != null) {
          JOptionPane.showMessageDialog(null, winner);
          System.exit(0);
      }
      makeAIMove(); // ai is only called when we move a black piece
    } else {
      int location = 0;
      if (startY == 0) {
        location = startX;
      } else {
      location = (startY * 8) + startX;
      }
      String pieceLocation = pieceName + ".png";
      pieces = new JLabel(new ImageIcon(pieceLocation));
      panels = (JPanel) chessBoard.getComponent(location);
      panels.add(pieces);
    }
  }
   
   whiteTurn = !whiteTurn;
   blackTurn = false;
 }
}






    public void mouseClicked(MouseEvent e) {
    }
    public void mouseMoved(MouseEvent e) {
   }
    public void mouseEntered(MouseEvent e){

    }
    public void mouseExited(MouseEvent e) {

    }

	/*
		Main method that gets the ball moving.
	*/
  public static void main(String[] args) {
    ChessProject frame = new ChessProject();
    frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE );
    frame.pack();
    frame.setResizable(true);
    frame.setLocationRelativeTo( null );
    frame.setVisible(true);
    Object[] options = {"Random Moves","Best Next Move","Based on Opponents Moves"};
    int n = JOptionPane.showOptionDialog(frame,"Lets play some Chess, choose your AI opponent","Introduction to AI Continuous Assessment", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
    System.out.println("The selected variable is : "+n);
    selected = n;
    frame.makeAIMove();
     }
}
