# Could have replaced that class with a dictionary though
class Clause:
    def __init__(self):
        self.positive = set()
        self.negative = set()

    @staticmethod
    def combine(clause_a, clause_b):

        # Merging clauses (union only)
        merge_clause = Clause()
        merge_clause.positive = clause_a.positive.union(clause_b.positive)
        merge_clause.negative = clause_a.negative.union(clause_b.negative)

        # Removing complementary (if one exists)
        new_clause = None
        for character in merge_clause.positive:
            if character in merge_clause.negative:

                # If we already had a new clause from before meaning that there are
                # at least 2 complementaries, e.g. the result is always a tautology.
                # Therefore, we have to disregard the whole thing
                if new_clause:
                    return None

                # Creating new clause
                new_clause = Clause()
                new_clause.positive = merge_clause.positive.copy()
                new_clause.negative = merge_clause.negative.copy()
                new_clause.positive.remove(character)
                new_clause.negative.remove(character)

        # Returning new clause
        return new_clause


# Welcome the user
print("--- SAT Solver ---")

# Getting user input
while True:
    print("Write down your clause set below, please :)\n"
          "For rules and/or example clause sets submit 'help'")
    user_input = input(">>> ")
    print()

    # Help user to understand how the program works
    if user_input.lower() == "help":
        print("Atomic formulae are denoted with lower case symbols: p, q, r, etc.\n"
              "To denote a negated form use '-' in front of a symbol: -p, -q, -r, etc.\n"
              "Separate atomic formulae into clauses using curly brackets '{', '}' and comma ','\n"
              "Here are some example inputs of a set in clause form:\n"
              "1. {{p}}\n"
              "2. {{q}, {-q}}\n"
              "3. {{q, p, -r}, {-p}, {r}, {-q}}\n")
        continue
    break

# Splitting user data
clause_set = (user_input[1:-1]                 # Removing front and back curly brackets
              .replace(" ", "")    # Removing blank spaces
              .replace("--", "")   # Removing double negations (--p into p)
              .split(","))                     # Splitting everything up (formating later)

# Separating into a proper form (not really validating syntax tho)
next_clause_set, next_clause, positive = set(), None, True
for part in clause_set:
    for symbol in part:

        # Creating a new clause for symbols
        if symbol == "{":
            next_clause = Clause()

        # Finishing single clause and adding it
        elif symbol == "}":

            # Checking for duplicates
            add = True
            for clause in next_clause_set:
                if next_clause.positive == clause.positive and next_clause.negative == clause.negative:
                    add = False

            # Also, checking if there are any tautologies (We discard those)
            for atom in next_clause.positive:
                if atom in next_clause.negative:
                    add = False

            # Finally, adding the clause (unless skipped)
            if add:
                next_clause_set.add(next_clause)

        # Determening whether the next symbol is positive or negative
        elif symbol == "-":
            positive = False

        # Adding symbol to the clause
        elif positive:
            next_clause.positive.add(symbol)

        # Adding symbol to the clause and update assumption of next symbol state
        else:
            next_clause.negative.add(symbol)
            positive = True

# Replacing with the new one
clause_set = next_clause_set

# Analyzing...
while True:

    # Checking if we found an empty clause
    for clause in clause_set:
        if not clause.positive and not clause.negative:

            # Empty clause found
            print("This clause set is unsatisfiable")
            exit()

    # If not, combine some clauses and see then
    next_clause_set = clause_set.copy()
    for clause1 in clause_set:
        for clause2 in clause_set:

            # Ignoring duplicates part 1
            if clause1 != clause2:
                combined_clause = Clause.combine(clause1, clause2)  # Combining clauses

                # Ignoring duplicates part 2
                if combined_clause:
                    add = False
                    for clause in next_clause_set:
                        if combined_clause.positive == clause.positive and combined_clause.negative == clause.negative:
                            add = True

                    # Adding new clause
                    if not add:
                        next_clause_set.add(combined_clause)

    # No changes done, no sense in going to the next iteration, couldn't prove unsatisfiability
    if clause_set == next_clause_set:
        print("This clause set is satisfiable")

        # Creating an interpretation
        atoms = Clause()
        for clause in clause_set:
            if len(clause.positive) == 1 and not clause.negative:
                atoms.positive.add(list(clause.positive)[0])
            elif len(clause.negative) == 1 and not clause.positive:
                atoms.negative.add(list(clause.negative)[0])

        # If some atoms are irrelevant, we choose to not depend on
        # them and therefore make them False in our interpretation.
        for clause in clause_set:
            for atom in clause.positive:
                if atom not in atoms.positive:
                    atoms.negative.add(atom)
            for atom in clause.negative:
                if atom not in atoms.negative:
                    atoms.positive.add(atom)

        # Printing out the result
        print("Here is an interpretation that does:")
        columns = 3  # Aesthetic choice

        # Positive interpretation evaluations
        n = 0
        for atom in atoms.positive:
            if not n % columns:
                print(f"v_I({atom}) = T", end="")
            elif (n + 1) % columns:
                print(f"   v_I({atom}) = T", end="")
            else:
                print(f"   v_I({atom}) = T")
            n += 1

        # Aesthetic prep for next part
        if n % columns:
            print()

        # Negative interpretation evaluations
        n = 0
        for atom in atoms.negative:
            if not n % columns:
                print(f"v_I({atom}) = F", end="")
            elif (n + 1) % columns:
                print(f"   v_I({atom}) = F", end="")
            else:
                print(f"   v_I({atom}) = F")
            n += 1

        # Process finished... on new line (because why not)
        if n % columns:
            print()
        exit()  # Done

    # Updading set for iteration:
    clause_set = next_clause_set
