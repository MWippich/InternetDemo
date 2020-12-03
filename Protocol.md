The As soon as two clients have connected to the server they will both receive information about the game map
according to SETUP. After this the game will proceed with the clients in turn sending (CLIENT -> SERVER) and receiving
(SERVER -> CLIENT) information until the game ends. The first client to connect to
the server will be player 1 in the game.

### SETUP
First the client receives a UTF string containing the x coordinates of walls on the game map.
When this information has been received the client sends a byte back to the server to notify it that it is ready to
accept the next peice of information. the server then sends another UTF string with the y coordinates of the walls on
the game map. After the information has been received the client send another byte to the server to notify that the data
has been received.

The coordinate strings are sent in the format of a list of form "[x1, x2, x3, ... , xn]".

### CLIENT -> SERVER
First the client recieves a byte from the server notifying that it is ready to receive data.
Once this has been received the client can start sending the player input.
First it sends a byte (0 or 1) representng whether the player wants to attack or not.
Again the server sends a byte when it is ready to receive the next piece of data.
Then the client will send a UTF string signifying the direction the player wants to move in
("up", "down", "left", "right" or any other string meaning no movement")


After the client has sent its data to the server the server will perform the game logic after which it will start sending
the game data back to the client.

### SERVER -> CLIENT

The client first receives the first players x position. I then pings the server with one byte to signal it is ready to
receive the next piece of data.

This repeats for player 1's y position, player 2's x and then y position, player 1's number of bombs and player 2 number
of bombs.

After this a UTF string will be sent containing the position of fire on the game map. This information is in the form of
a list of the form "[x1, y1, x2, y2, ... xn, yn]" where xk and yk represents the k:th fires x and y coordinates on the
game map. After this another byte is sent back to the server.

This is repeated for inactive (pickupable) bombs and active (about-to-explode) bombs.

Finally a string with the text to be shown when the game is over is sent. Containing the outcome of the game, or "None"
if no one has won yet. ("Player 1 won!", "Player 2 won!", "The game ended in a tie.", "None").

When this is finished the client should start sending new input data to the server again.