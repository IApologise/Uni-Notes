# P.S.
# The code could have been way more flexible.
# It is really practical, and now I will use it
# to verify my answers because why not?

# Probably going to make it way better in the future.
# If there will be more tasks like that that is.

r1 = "fh"
r2 = "hh"

# Instructions
# The sequence of functions is represented by a sequence of letters
# Parantheses are not required, neither is the x at the end

# Here are some examples:
# f -> f(x),   g -> g(x),   h -> h(x)
# fg -> f(g(x)),   gh -> g(h(x))
# fgh -> f(g(h(x))))

# Setup
input_string = str(input("\nWrite here: "))
strings = [input_string]

# In case the termination never happens
limit = 1_000_000

# Checking every possibility
while limit > 0:
    new_strings = []

    # Checking two possibilities f(g(x)) and g(h(x))
    for string in strings:
        new_string1 = string.replace("fg", r1)  # Replacing f(g(x)) with r1
        new_string2 = string.replace("gh", r2)  # Replacing g(h(x)) with r2

        # Idea: If the reduction was performed, then
        # we add the reduced form of the function.
        # If we could not reduce it in any way,
        # then we keep it since it is one of the
        # possible reductions possible and wait for
        # the rest of the paths to finish.

        if string == new_string1 and string == new_string2:
            new_strings.append(string)  # No new paths found, keep the previous in list
        if string != new_string1:
            new_strings.append(new_string1)  # New reduction path found, add to list
        if string != new_string2:
            new_strings.append(new_string2)  # New reduction path found, add to list

    # Termination check, no changes -> terminate
    if strings == new_strings:
        break
    strings = list(set(new_strings))

    # In case it never terminates
    limit -= 1

# Printing results
print(f"\nTotal possibilities: {len(strings)}")
for string in range(len(strings)):
    print(f"{string + 1}: {strings[string]}")
