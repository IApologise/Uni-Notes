# Oppgave 1

# Denne programmet printer ut noen tall, regner ut summen og produktet fra listen.
# Programmet også spør brukeren å oppgi 4 navn og sjekker om navnet mitt er med.

tall = [3, 6, 5]  # Lager en liste med 3 tilfeldige tall
tall.append(8)    # Legger til en tilfeldig tall til listen

print(f"{tall[0]}\n"   # Første elementet i listen
      f"{tall[2]}\n")  # Tredje elementet i listen

navn = []  # Lager en tom liste
antall_nye_navn = 4  # Antall nye navn

print(f"Kan du oppgi {antall_nye_navn} navn?")      # Spør brukeren å oppgi 4 navn
for et_navn in range(1, antall_nye_navn + 1):       # Lager en for løkke (fordi vi er lat)
    ny_navn = input(f"Navn nummer {et_navn} >>> ")  # Lagrer bruker input
    navn.append(ny_navn)                            # Legger til navnet til listen
print()  # Liker å ha mellomrom

mitt_navn = "Justas"         # Dette er navnet mitt
if mitt_navn in navn:        # Sjekker om navnet mitt er i listen
    print("Du husket meg!")  # Nice :)
else:                        # Ellers...
    print("Glemte du meg?")  # Not nice ;(
print()  # Liker å ha mellomrom

sum = sum(tall)              # Regner ut summen av tallene i listen
print(f"Summen er: {sum}.")  # Printer ut svaret

produkt = 1  # Common factor
for n in tall:  # Lager en for løkke
    produkt *= n  # Regner ut produktet av tallene i listen
print(f"Produktet er: {produkt}.")  # Printer ut svaret
