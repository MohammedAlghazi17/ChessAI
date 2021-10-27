import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

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
  private Boolean checkWhiteOponent(int newX, int newY){   /* a method called checkWhiteOponent that takes as an argument the coordinates
                                                of a position and checks if there is an opponent’s piece present on the square*/
   Boolean oponent;
   Component c1 = chessBoard.findComponentAt(newX, newY);
   JLabel awaitingPiece = (JLabel)c1;
   String tmp1 = awaitingPiece.getIcon().toString();
   if(((tmp1.contains("Black")))){
     oponent = true;
     System.out.println("opa");
     if(tmp1.contains("King")){
       oponent = false;
       System.out.println("opb"); // only a checkmate can kill the king, therefore no pieces are coded to kill the king
     }
   }
   else{
     oponent = false;
     System.out.println("opc");
   }
   return oponent;
 }

 private Boolean checkBlackOponent(int newX, int newY){   /* a method called checkBlackOponent that takes as an argument the coordinates
                                               of a position and checks if there is an opponent’s piece present on the square*/
   Boolean oponent;
   Component c1 = chessBoard.findComponentAt(newX, newY);
   JLabel awaitingPiece = (JLabel)c1;
   String tmp1 = awaitingPiece.getIcon().toString();
   if(((tmp1.contains("White")))){
     oponent = true;
     if(tmp1.contains("King")){
       oponent = false; // only a checkmate can kill the king, therefore no pieces are coded to kill the king
     }
   }
   else{
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
        if (chessPiece == null) return;
         chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
     }
	//Mouse released method
    public void mouseReleased(MouseEvent e) {
        if(chessPiece == null) return;

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

		Boolean blackTurn = false;

    if(whiteTurn){
  		if(pieceName.contains("White") && !(xMovement == 0 && yMovement == 0)){
  			blackTurn = true; //
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
              }
              else {
                validMove = false;
              }
            }
            else {
              if (checkBlackOponent(e.getX(), e.getY())) {   /* a method called checkBlackOponent that takes as an argument the coordinates
                                                            of a position and checks if there is an opponent’s piece present on the square*/
                validMove = true;

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
									} else {
										validMove = false;
									}
								} else {


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
						} else {
							if (piecePresent(e.getX(), (e.getY()))) {
								if (pieceName.contains("White")) {
									if (checkWhiteOponent(e.getX(), e.getY())) {  /* a method called checkWhiteOponent that takes as an argument the coordinates
                                                                of a position and checks if there is an opponent’s piece present on the square*/
										validMove = true;
									} else {
										validMove = false;
									}
								} else {
									if (checkBlackOponent(e.getX(), e.getY())) {   /* a method called checkBlackOponent that takes as an argument the coordinates
                                                                of a position and checks if there is an opponent’s piece present on the square*/
                    validMove = true;

									} else {
										validMove = false;
									}
								}
							} else {
								validMove = true;
							}
						}
					} else {
						validMove = false;
					}
				}
			}//End Rook

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


                  }
                } else { //moving BlackQueen
                  if (checkBlackOponent(e.getX(), e.getY())) {  /* a method called checkBlackOponent that takes as an argument the coordinates
                                                                of a position and checks if there is an opponent’s piece present on the square*/
                    validMove = true;

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
          } else {
            // this here is checking if its enemy piece, or if it is a free space
            if (piecePresent(e.getX(), (e.getY()))) {
              if (pieceName.contains("White")) {
                if (checkWhiteOponent(e.getX(), e.getY())) {  /* a method called checkWhiteOponent that takes as an argument the coordinates
                                                              of a position and checks if there is an opponent’s piece present on the square*/
                  validMove = true;

                }
              } else {
                if (checkBlackOponent(e.getX(), e.getY())) {  /* a method called checkBlackOponent that takes as an argument the coordinates
                                                              of a position and checks if there is an opponent’s piece present on the square*/
                  validMove = true;

                }
              }
            } else {
              validMove = true;
            }
          }
        }//ending Rook movement as a queen
      }
    }
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










if(!validMove){
   int location=0;
   if(startY ==0){
     location = startX;
   }
   else{
     location  = (startY*8)+startX;
   }
   String pieceLocation = pieceName+".png";
   pieces = new JLabel(new ImageIcon(pieceLocation));
   panels = (JPanel)chessBoard.getComponent(location);
     panels.add(pieces);
 }
 else{

   if(progression){
     int location = 0 + (e.getX()/75);
     if(c instanceof JLabel){
       Container parent = c.getParent();
       parent.remove(0);
       pieces = new JLabel(new ImageIcon("BlackQueen.png"));
       parent = (JPanel)chessBoard.getComponent(location);
       parent.add(pieces);
     }

   }
   else if(success){
     int location = 56 + (e.getX()/75);
     if (c instanceof JLabel){
       Container parent = c.getParent();
       parent.remove(0);
       pieces = new JLabel( new ImageIcon("WhiteQueen.png") );
       parent = (JPanel)chessBoard.getComponent(location);
       parent.add(pieces);
     }
     else{
       Container parent = (Container)c;
       pieces = new JLabel( new ImageIcon("WhiteQueen.png") );
       parent = (JPanel)chessBoard.getComponent(location);
       parent.add(pieces);
     }
   }
   else{
     if(c instanceof JLabel){
       Container parent = c.getParent();
       parent.remove(0);
       parent.add( chessPiece );
     }
     else{
       Container parent = (Container)c;
       parent.add( chessPiece );
     }
     chessPiece.setVisible(true);
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
        JFrame frame = new ChessProject();
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE );
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo( null );
        frame.setVisible(true);
     }
}
