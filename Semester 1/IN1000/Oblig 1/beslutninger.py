# Opgave 2

# Beskrivelse:
# Programmen spør brukeren om han/hun vil ha brus.
# Hvis ja så printer den ut "Her har du en brus!".
# Hvis nei: "Den er grei.",
# ellers: "Det forsto jeg ikke helt."

# Programmet trenger presis input fra brukeren.
# For å gjenta forsøk (i tilfellet programmet forsto ikke)
# må brukeren starte programmet på nytt.

# Spør brukeren om han/hun vil ha brus ved bruk av input().
# Jeg også legger til tekst og formaterrer den for å se bedre ut.
svar = input(f"Vil du ha brus?\n"
             f"(ja/nei) >>> ")
# Svaret lagrer jeg som en variabel

# Bruker if, elif og else for å printe ut spesifike svarene
if svar == "ja":                  # Hvis brukeren vil ha brus
    print("Her har du en brus!")  # printer vi ut "Her har du en brus!"
elif svar == "nei":               # Hvis brukeren vil ikke ha brus
    print("Den er grei.")         # printer vi ut "Den er grei"
else:                                   # Hvis programmet forsto ikke
    print("Det forsto jeg ikke helt.")  # printer vi ut følgende
