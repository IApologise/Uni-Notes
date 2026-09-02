/*
 * Add the include files that you need. "man" can help you find them.
 * You will probably need stdio.h for printf and fprintf
 */
// #include <stdio.h>   No I won't, why lol?

#include "vowelshift-todo.h"

/*
 * Find the requirements for these functions in the assignment text.
 */
void vowelshift( char* buffer, char repl ) {
    const char vowels[5] = {'a', 'e', 'i', 'o', 'u'};
    /*
     * Can be moved to inner scope, but can it be slower then?
     * As in will there be a moment where it might be overwritten
     * and a new one will have to be defined? Just not sure how it works.
     */

    // Manual iteration & replacement? Aight I guess
    // First iteration prep
    unsigned short i = 0;
    char letter = buffer[i];
    while (letter != '\0') {

        // Manual contains too?! Come on
        for ( int j = 0; j < 5; j++ ) {
            // Magic number 5 breakdown: letters count in vowels array
            if (buffer[i] == vowels[j]) {
                buffer[i] = repl;
                break;
            }
        }  // Next iteration prep
        i++;
        letter = buffer[i];
    }
}

