The game will have three major activities/views:
-	The Image activity will show a list of the images which can be used to play.
-	The Play activity will show the game and handle input from the player when he/she plays the game

There will also be two setting activities, these are parts of other activities:
-	The first will be on the home screen and will be used to choose the difficulty
-	The second will be on the game screen and will have options to restart, change difficulty or go back to the home menu.

The project will use several classes which will be implemented. 
-	The game class will get all information about a game (which picture and what difficulty) and create a game to be played. This class will scramble the picture using getSubImage/4 from the java.awt.image library. It will also keep a record of the solution (the order of tiles defined by their id) and the current field and the public function which will calculate the score at the end of the game.
-	The tile class will be a class which defines the tiles of the game. Each tile object will have an ID and an image (a subimage of the entire image).

Some public functions which will be implemented are:
- scramble_image(Image i), which will return a list of image objects which will then be used to assign an image to each tile.
- randomize_puzzle(int i) which ill return a randomized list of integers the size of the number of tiles. This will be used to create the playing field.
- swap_tiles(int i) which will return nothing, but swap a tile to the empty cell and update the information in the game object.



First version of the home screen/image picker:

![oms](https://github.com/martward/n-puzzle/raw/master/doc/Imga_picker.png)

First version of the game screen:

![oms](https://github.com/martward/n-puzzle/raw/master/doc/Game.png)

First version of the winning/end screen:

![oms](https://github.com/martward/n-puzzle/raw/master/doc/Finish.png)
