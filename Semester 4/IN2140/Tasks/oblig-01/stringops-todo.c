/*
 * Add the include files that you need. "man" can help you find them.
 * You will probably need stdio.h for printf and fprintf
 */
// #include <stdio.h>   Wrong again

#include "stringops-todo.h"

#include <stddef.h>
#include <string.h>

/*
 * Find the requirements for these functions in the assignment text.
 */
int   stringsum( char *s )
{
    // Setup
    int sum = 0;

    // First iteration prep
    unsigned short i = 0;
    char letter = s[i];
    while (letter != '\0') {

        // Counting
        if ('Z' >= letter && letter >= 'A') {
            sum += letter - 'A' + 1;
        } else if ('z' >= letter && letter >= 'a') {
            sum += letter - 'a' + 1;
        } else if (letter != ' ') {  // Spaces allowed, but not counting
            return -1;  // Illegal character -> exit
        }
        // Next iteration prep
        i++;
        letter = s[i];
    }
    // Returning total count
    return sum;
}

int   distance_between( char *s, char c )
{
    // Setup
    int start = -1;
    int end = -2;

    // First iteration prep
    int i = 0;
    char letter = s[i];
    while (letter != '\0') {

        // Measuring end points
        if (letter == c) {
            if (start == -1) {
                start = i;
            } end = i;
        }
        // Next iteration prep
        i++;
        letter = s[i];
    }
    // Calculating and returning distance
    return end - start;
}

char* string_between( char *s, char c )
{
    // Setting up for the thing
    unsigned short start = -1;  // Sacrificing half a bit of memory
    unsigned short end = 0;

    // First iteration prep
    unsigned short i = 0;
    char letter = s[i];
    while (letter != '\0') {

        // Doing the thing
        if (letter == c) {
            end = i;
            if (start == 65535) {  // 65535 = (unsigned short) -1
                start = i;
                end++;  // Formally, end = start + 1.
                        // But, end++ is identical here (and shorter)
            }
        }  // Next iteration prep
        i++;
        letter = s[i];
    }

    // Returning stuff
    if (start == 65535) {
        return NULL;  // What happened to my boy nullptr?
    } s[end] = '\0';
    memmove(s, &s[start + 1], end - start);
    return s;
}

int  stringsum2( char *s, int *res )
{
    // I can also be lazy sometimes you know.
    return ((*res = stringsum(s)) > 0) - 1;
}


