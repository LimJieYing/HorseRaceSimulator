# Horse Race Similator


## Part I

A Textual based Simulator which runs in the terminal. To run the program, run the file test.java. Firstly, a new object of the class Race must be created. Upon creation, the parameters it requires(in order) are distance(int) and number of lanes(int) or distance(int), unit(String) and number of lanes(int). Next, horse objects must be created and upon creation the parameters it requires are horseSymbol(char), horseName(String) and horseConfidence(double). The confidence must be a value between 0 to 1.0.

Then the horses must be added to the race class by calling the method addHorse. The values passed to addHorse are the horse object(which was previously created) and the laneNumber. The lane number must be less than or equal to the specified number of lanes when the Race class is created. If a horse is not added to a lane, The lane will be empty. Lastly, the method startRace can be called to start the race. It can be called multiple times to to start a race in succession to the previous race.

## Part II

A textual based Simulator which runs on a graphical user interface frame. The main method is contained in the file RaceGUI.java for the programme to run. First, run the file RaceGUI.java and a new window will open. To view the previous Race Results, go to the menu bar and click file, then click “Load Previous Results”.  Next,  the details of the track must be filled in. The fields are number of lanes, Length of track (int), unit of length (“m” or “metres”, “yd” or “yards”) and color of race. The optional fields are unit of length and color of race with default values of  “m” and white respectively. After the “Create Race” button is clicked, a list of lanes will appear for users to fill in horse details. The details to fill up are horseSymbol(char), horseName(String), horseConfidence(double, a value between 0 to 1.0), horse breed and horse accessory. To leave a lane empty, do not fill up the fields in that particular lane. To submit, click on the “Add Horses” button.

A new Window will appear and the Horse’s statistics will be displayed. If no users would want to bet, the race could be started immediately by clicking the “Start Race” button in green. If users would like to bet, the number of users must be set and the program will prompt the users to enter their names. Once the Set Users button is clicked, The user can then select their name, horse and amount to bet then click the button “Place bet”. Users will start off with $20 and after the race, the new balance of each user will be displayed below the horse statistics. To run the race multiple times with the same horses with updated confidence, click the “Start Race” button again after the current race has concluded.
