# ASCII-ART

Gets a picture and outputs that picture in ASCII art - drawn from ASCII characters.

 Uses Object Oriented Programming

Implementation details:
brightnessLevel() - returns array that each value in index i represent the number of brightness of the character
at index i in charset. There is a match at the index between the arrays.
linearStretch() - keeps the order the same way and preforms a linear stretch on the given brightness level of
characters by calculating the given formula.
convertImageToAscii() - checks for each sub image what the average of brightness and then checks by for loop
what the value of the closest brightness of char. The function keeps the index of this value and then inserts
to the new array the character at the same index at the array of characters.

Files description:
/ascii_art/img_to_char/BrightnessImgCharMatcher.java - The class implements the conversion of an image to ASCII image
according to brightness level.
/ascii_art/img_to_char/Driver.java - The class implements the main function.

Used the HashMap from Collection that uses as a cache for storing the brightness level in order to
optimize the algorithm. I save in the map the brightness level for each sub image, so each time we need it, we
dont need to calculate ones again the brightness level, but just take it from the cache map.
