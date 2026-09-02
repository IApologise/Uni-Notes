# Oppgave 3.4

# Beskrivelse:
# Denne oppgaven skulle gjøre alt det samme som i meny.py
# Men, programmene skulle se ulikt ut.
# Og denne løsningen ser verre ut (in my opinion)
# ... men enklere.

# Meny
hovedretter = ["biff", "torsk", "salat"]  # "salat" er vegetar
tilbehorer = ["gulrøtter", "bearnaise"]   # "gulrøtter" er grønnsak

# Be brukeren velge hovedrett og tilbehør, antar nesten perfekt syntaks
hovedrett = input(f"Velg en hovedrett ({", ".join(hovedretter)}): ").lower()
tilbehor = input(f"Velg ett tilbehør ({", ".join(tilbehorer)}): ").lower()

# Sjekker om brukeren har gjort en riktig valg
vegetar_hovedrett = hovedrett == "salat"
gronnsak_tilbehor = tilbehor == "gulrøtter"

# Kritiserer brukeren
if vegetar_hovedrett and gronnsak_tilbehor:  # For mye grønnsaker!
    print("Du har valgt et vegetarmåltid.")
elif not gronnsak_tilbehor and not vegetar_hovedrett:  # Ikke nok grønnsaker!
    print("Du spiser ikke nok grønnsaker!")
else:
    print(f"Du har valgt {hovedrett} med {tilbehor}.")  # Bra :)
