# Oppgave 1

# Beskrivelse:
# I denne programmet skal jeg besvare oppgave 1 fra oblig 1
# Disse delene skal markeres som "Steg {n}" fra 1 til 7

# Denne programmet skal:
# - Printe ut masse forskjellig
# - Få input fra brukeren
# - Regne ut enkelte aritmetiske ligninger.
# - Slå sammen tekst på flere måter

# Steg 1
# Har leser du fra filen variabler.py

# Steg 2
print("Hei Student!")  # Printer ut "Hei Student!" til konsolen

# Steg 3
# Får en input fra brukeren
navn = input(f"Hva er navnet ditt?\n"  # Hopper til ny linje med "\n"
             f" >>> ")
print(f"Hei {navn}")  # printer ut "Hei {bruker input}"
# Jeg formaterer fil ved bruk av --> f"" bak apostrofene
# Det gir meg mer kontroll og jeg kan skrive inn variabler direkte
# som tekst ved bruk av krølparantes slik: f"{variabel}"

# Steg 4
heltall_1 = 5   # Definerer en variabel og gir den en ny verdi
heltall_2 = -3  # Samme her
print(f"{heltall_1}\n"  # Printer ut heltall_1 og hopper til ny linje med "\n"
      f"{heltall_2}")   # Printer ut heltall_2

# Steg 5
# Regner ut differansen
differanse = heltall_1 - heltall_2  # Definerer en variabel og regner ut dets verdi
print(f"Differanse: {differanse}")  # Printer ut differansen

# Steg 6
# Venter på bruker input
ny_navn = input(f"Hva er nye navnet ditt?\n"
                f">>> ")
sammen = navn + ny_navn  # Slår sammen nye navnet med det forrige ved bruk av "+"
print(sammen)  # Printer ut svaret

# Steg 7
sammen = f"{navn} og {ny_navn}"  # Lager og formaterer en ny string. Dette er en elegant måte å slå sammen streng på
print(sammen)  # Jeg tviler på at denne delen skulle ikke være obligatorisk. Steg 7 kan også tolkes på flere måter
