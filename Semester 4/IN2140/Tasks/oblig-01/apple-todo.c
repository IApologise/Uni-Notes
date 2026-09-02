/*
 * Add the include files that you need. "man" can help you find them.
 * You will probably need stdio.h for printf and fprintf
 */

/*
 * #include <stdio.h>
 * #include <stdlib.h>
 * #include <string.h>
 * I do not need those lol
 */

#include "apple-todo.h"

/*
 * Find the requirements for these functions in the assignment text.
 */
int locateworm( char* buffer )
{
    /*
     * I am confused by how easy the task is, it is suspicious.
     * There are no similar letters between "worm" and "apple",
     * so by simply locating where the first "w" is we locate
     * the worm, and for its end we locate last "m". While we
     * are looking for the last "m" we override with spaces.
     */

    // First iteration prep
    int i = 0;
    char letter = buffer[i];
    while (letter != '\0') {

        // Locating the worm
        if (letter == 'w') {
            return i;
        }
        // Next iteration prep
        i++;
        letter = buffer[i];
    }
    // No worm found
    return -1;
}

int removeworm( char* apple )
{
    // Setup
    int start = locateworm(apple);  // Reusing the previous method for simplicity
    if (start == -1) {
        return 0;  // No worm to cut out in the first place
    }
    // First iteration prep
    int i = start + 1;
    char letter = apple[i];
    while (letter != '\0') {

        // Checking if worm was eradicated
        if (letter != 'm' && apple[i - 1] == 'm') {
            break;  // Killing two birds with one stone:
            // 1. The '...m\0' case, and 2. The missing return at
            // the end, which should never happen in the first place.
            // But the CLion IDE is satisfied now :)
        }
        // Continue cutting the worm out
        apple[i - 1] = ' ';

        // Next iteration prep
        i++;
        letter = apple[i];
    }
    // Last cut and we are out
    apple[i - 1] = ' ';
    return i - start;
}
