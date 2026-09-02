from verden import Verden


def hovedprogram():

    # Definerer
    kolonner = 0
    rader = 0

    # Kolonner
    while True:
        try:
            kolonner = int(input("Antall kolonner (lengde): "))
        except ValueError:
            print("\nLengde må være et naturlig tall.")
            continue
        break

    # Antall rader
    while True:
        try:
            rader = int(input("Antall rader (høyde): "))
        except ValueError:
            print("\nHøyde må være et naturlig tall.")
            continue
        break

    # Lager program
    CGoL = Verden(rader, kolonner)  # CGoL - Conway's Game of Life

    # Kjører program
    while True:
        CGoL.tegn()  # Opdaterer display
        while True:  # Check if input is either empty or "q"
            user_input = input("Trykk 'ENTER' for å fortsette eller skriv inn 'q' for å avslutte\n"
                               ">>> ")
            if user_input == "q":
                exit()  # Avslutter python fil
            elif not user_input:
                break  # avslutter denne while løkken og fortsetter videre (linje 42)
        CGoL.oppdatering()


# starte hovedprogrammet
hovedprogram()
