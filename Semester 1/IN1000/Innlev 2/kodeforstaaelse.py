# Oppgave 4

a = input("Tast inn et heltall! ")
b = int(a)
if b < 10:
    print(b + "Hei!")

# Programmet kommer ikke til å fungere pga. linje 6.
# Grunnen til dette er fordi at man kan ikke bruke addisjon mellom tekst og tall.
# Jeg for eksempel aner ikke hvor mange er 5 + Hei
# Denne type error heter TypeError.
