# Avaliable symbols
symbols = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
           "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", 'X', "Y", "Z"]

# Getting user input
while True:
    print(f"Input size, up to {len(symbols)}:")
    size = int(input(">>> "))

    if not len(symbols) >= size >= 0:
        print(f"Please submit a positive value up to {len(symbols)}\n")
        continue
    break

# A small setup
combinations = [symbols[:size]]

# Creating all the combinations
for i in range(1, size + 1):

    # Creating new combinations
    new_combinations = []
    for combination in combinations:
        new_combination = combination.copy()

        # Switching states
        for n in range(i):
            if new_combination[n][0] == "-":
                new_combination[n] = new_combination[n][1:]
            else:
                new_combination[n] = f"-{new_combination[n]}"

        # Adding new combinations to a list
        new_combinations.append(new_combination)
    for new_combination in new_combinations:
        combinations.append(new_combination)

# Reformating
output = "{"
for combination in combinations:
    output += "{"
    for symbol in combination:
        output += f"{symbol},"
    output = output[:-1] + "},"
output = output[:-1] + "}"

# Exception
if size == 0:
    output = "{{}}"

# Printing out the result
print(output)
exit()
