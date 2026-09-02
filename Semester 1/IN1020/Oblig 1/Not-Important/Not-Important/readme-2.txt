Instructions for use:

Step 1: Write in PLU code (datasheet required)
Step 2: Write in what trait of the product you need (datasheet required)
Step 3: Repeat Steps 1-2

PLU datasheet:
405 - Eple
307 - Rundstykke
303 - Appelsin

Traits datasheet:
0 - PLU code
1 - Price

Case 1: If the PLU code in step 1 was not found the program will return "-1" and go back to step 1
Case 2: If the trait on step 2 was not found then the program will return "-2" and return to step 2



Explanation of the code (// - means comments):

BRA start        // When initializing first time skip error printing
reset LDA case1  // When plu was not found jump here and load case1 error code
OUT              // Print out the error code

start INP  // Wait for input (plu code)
STA plu    // Store plu code somewhere in memory

LDA load     // Loading loader (the default one which starts at the start of list)
STA product  // Updating the loader

product LDA  // This is the loader. It loads list data
BRZ reset    // If the list ended and plu not found: reset
SUB plu      // If plu is correct this should return 0 (cuz simple math)
BRZ continue // If the correct plu is found: continue, else: try next plu code

LDA product  // Loading loader for modifications
ADD traits   // modifying - jumping to next plu
STA product  // Updating loader
BRA product  // Going back to checking

back LDA case2  // works in similar way as case1, but for case2
OUT             // Printing out error code for case2

continue INP     // Now step 2, waiting for trait
BRP next         // If the number is positive, go to next check
BRA back         // If number is negative ask for a valid trait number (again)
next SUB traits  // Check if number isn't too big (works cuz math)
BRP back         // If number too big, ask for a valid trait (yet again)

ADD traits   // Reversing the change from the checks
ADD product  // Making it a change in the relative path (steps from the plu)
STA trait    // Updating, but now the other loader

trait LDA  // Same as first loader but it knows where trait is at, not the plu
OUT        // Print out trait (PLU, Price, etc.)
HLT        // Stop the program. Can be replaced with "BRA reset" return to step 1
           // Or change line 0 with "x BRA start" and HLT with "BRA x"

case1 DAT -1  // Output for case1
case2 DAT -2  // Output for case2

load LDA ls   // Default loader (points towards list start
plu DAT 0     // Temporary memory for the plu code

traits DAT 2  // Number that determines the amount of traits each product has
              // This is also the start of where the data can be changed

ls DAT 405  // The database start - this is first data in the database
DAT 7
DAT 307
DAT 4
DAT 303
DAT 10
le DAT 0  // Database end - 0 signifies the end of the database



Explanation of the data structure:

{case1} is the error output from step 1.
{case2} is the error output from step 2.
{load} is the default product loader. This one is required for resets.
{plu} is the most recent plu code.
{traits} is the amount of data each product has. In this case we have a plu code and a price. Therefore, in total: 2. The data of the products is structured in the same way as in Traits datasheet: Whenever products get more traits, this digit should be changed manually.
{ls} or in other words List Start points towards the first data in the database and {le}, aka. List End signifies the end of the list.
That said, the data is heavily dependent on correct syntax and control over the number {traits}. Otherwise, the whole database will break.



Reasons why it is future proof:
- Easy to add more data (if u know where to), but would be awesome if I could do so just by using the machine. Sadly, memory is a very limited and precious a resource.

Reasons why I don't like it:
- PLU is not directly tied to the information about the product.
- More dependent on proper syntax when adding new data. Matrixes - less so.
- Can't flex on how cool and complex the code is.
- Need to explain to others how the data is structured, including normal workers.

Reasons why I like it:
- It is faster which is why I opted for this solution instead of matrixes.
- Easier to code, spares me more time (task says I am lazy... roleplay).
- Requires less space = more data can fit (when comparing to matrixes)