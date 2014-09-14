The game will have three major activities/views:
-	The Home activity will show the home screen and handle the buttons (play, image and difficulty). 
-	The Image activity will show a list of the images which can be used to play.
-	The Play activity will show the game and handle input from the player when he/she plays the game

There will also be two setting activities, these are parts of other activities:
-	The first will be on the home screen and will be used to choose the difficulty
-	The second will be on the game screen and will have options to restart, change difficulty or go back to the home menu.

The project will use several classes which will be implemented. 
-	The game class will get all information about a game (which picture and what difficulty) and create a game to be played. This class will scramble the picture using getSubImage/4 from the java.awt.image library. It will also keep a record of the solution (the order of tiles defined by their id) and the current field and the public function which will calculate the score at the end of the game.
-	The tile class will be a class which defines the tiles of the game. Each tile object will have an ID and an image (a subimage of the entire image).

The app will need to use a small database to store the preferences and game states when the app is not running. This way the player can pick up where he left off if his session is interrupted.

First version of the home screen:

![oms](https://github.com/martward/n-puzzle/raw/master/doc/Home.png)
