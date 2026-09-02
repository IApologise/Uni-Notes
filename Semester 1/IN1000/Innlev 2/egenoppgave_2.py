# Oppgave 5.
# Task: Create a game the player can not ever win.
# We do this via Game Theory (math & logic).
# Game: Nim.

# Default rules
presets = {"default": {"sticks": 21, "remove_min": 1, "remove_max": 3},
           "custom": {"sticks": 0, "remove_min": 0, "remove_max": 0}}

# Letting player choose the mode
mode = None
while mode != "pvp" and mode != "pve":
    mode = input(f"\n"
                 f"Choose your opponent:\n"
                 f"PvP - Player vs Player.\n"
                 f"PvE - Player vs Environment.\n"
                 f">>> ")
    mode = mode.lower()

# Letting players choose custom rules (optional).
rules = None
while rules not in presets:
    rules = input(f"\n"
                  f"Choose one of these rules:\n"
                  f"Default\n"
                  f"Custom\n"
                  f">>> ")
    rules = rules.lower()

# Selecting rule / creating own rule.
rules = presets[f"{rules}"]
if rules == presets["custom"]:
    while rules["sticks"] < 1:
        rules["sticks"] = int(input(f"\n"
                                    f"Choose the amount of sticks you want there to be\n"
                                    f"(More than 0): "))
    while rules["remove_max"] < 1:
        rules["remove_max"] = int(input(f"Choose the maximum amount of sticks that a player can remove\n"
                                        f"(More than 0): "))
    rules["remove_min"] = rules["remove_max"]  # Triggering a while loop on purpose.
    while abs(rules["remove_min"]) >= rules["remove_max"]:
        rules["remove_min"] = int(input(f"Choose the minimum amount of sticks that a player can remove\n"
                                        f"(When converted to absolute value it must be less than {rules["remove_max"]}): "))
# Allowing some crazy numbers, hopefully this will help you win :)

# Letting players choose their nicknames.
players = {"player 1": None,
           "player 2": None}
if mode == "pvp":
    players["player 1"] = input(f"\n"
                                f"What is your nickname player 1?\n"
                                f">>> ").capitalize()  # Player 1 nickname.
elif mode == "pve":
    players["player 1"] = input(f"\n"
                                f"What will be the nickname of the player 1 (bot)?\n"
                                f">>> ").capitalize()  # Bot (Player 1) nickname.
players["player 2"] = input(f"What is your nickname player 2?\n"
                            f">>> ").capitalize()  # Player 2 nickname.

# Adding a record of the sticks on the board to prevent stalemates.
# Also, a threshold, when we consider game a stalemate.
# For example in chess if the same position occurs 3 times.
max_repetitions = 3  # I will be using same technique here (simple and effective).
past_positions = []  # And this will be the record.
surrender = False  # Adding an option for bot to surrender

# The game starts!
On = True
print(f"\n"
      f"There are {rules["sticks"]} sticks on the board.")
while On:  # Checking if game is over.

    position = [rules["sticks"]]
    for player in range(1, 3):

        # Bot making decision
        if player == 1 and mode == "pve":
            remove = rules["remove_max"]  # Some stalemates are hard to get out of though.
            if not surrender:
                if rules["remove_min"] > 0:  # Outsmarting opponent.
                    modulo = (rules["remove_min"] + rules["remove_max"])
                    remove = rules["sticks"] % modulo
                elif rules["remove_min"] < 0:  # Outsmarting smart opponent.
                    modulo = (rules["remove_max"])
                    remove = rules["sticks"] % modulo - 1
                else:  # Sometimes we can't do the things we want to do, so we try our best.
                    remove = rules["remove_min"]
            print(f"{players[f"player {player}"]} decides to take away >>> \033[3m\033[0;32m{remove}\033[0m")

        # Detecting pvp stalemates
        elif mode == "pvp" and surrender:
            print(f"\n"
                  f"Looks like it is a stalemate!\n"
                  f"Reason: Draw by repetition.\n"
                  f"Good game, well played.")
            On = False
            break

        else:  # Player making decision
            remove = int(input(f"{players[f"player {player}"]} decides to take away >>> "))

        # Checking if the rules have been broken.
        if remove < rules["remove_min"]:
            print(f"{players[f"player {player}"]} can not remove {remove} amount of sticks.")
            remove = rules["remove_min"]
            print(f"So {players[f"player {player}"]} decided to remove {remove} amount of sticks instead.")
        elif remove > rules["remove_max"]:
            print(f"{players[f"player {player}"]} can not remove {remove} amount of sticks.")
            remove = rules["remove_max"]
            print(f"So {players[f"player {player}"]} decided to remove {remove} amount of sticks instead.")
        rules["sticks"] -= remove
        position.append(rules["sticks"])  # Adding results to record

        # Checking if the game is done.
        if rules["sticks"] < 0:
            rules["sticks"] = 0

        # Summarizing the player move.
        print(f"\n"
              f"{players[f"player {player}"]} has removed {remove} sticks leaving {rules["sticks"]} sticks.")

        # Finalizing.
        if rules["sticks"] == 0:
            print(f"{players[f"player {player}"]} removed the last stick and won the game!")
            On = False
            break

    # Adding record to the records list
    past_positions.append(position)
    if past_positions.count(position) >= max_repetitions:  # Detecting a stalemate
        print(past_positions)
        surrender = True
