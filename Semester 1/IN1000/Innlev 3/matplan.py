# Oppgave 4

#

maltider = {"Aleksander Moen": ["brød", "egg", "vafler"],
            "Elina Sommervold": ["egg", "pølser", "pannekaker"],
            "Jakub Budzich": ["brød", "pølser", "bacon med egg"],
            "Lina Rise": ["pølser", "egg", "smørbrød"],
            "Justas Simutis": ["brød", "bacon med egg", "pølser"]}


def beboerne():
    print("Her er navnene til beboerne:")
    for beboer in maltider:   # Printer ut hver beboer
        print(f"- {beboer}")  # Her

    navn = input("Skriv inn en navn til beboer >>> ")  # Input

    if navn not in maltider:  # Hvis beboer finnes ikke i databasen
        print(f"{navn} finnes ikke i databasen.")
    else:  # Hvis beboer finnes i databasen printer ut info om han/hun
        print(f"Her er matplanen til {navn}:\n"
              f"Frokost: {maltider[navn][0]}\n"  # Frokost
              f"Lunsj: {maltider[navn][1]}\n"    # Lunsj
              f"Middag: {maltider[navn][2]}\n")  # Middag


beboerne()  # Kjører funksjonen

# 3: In general
# Jeg ville gjerne bruke lister fordi at jeg er mest vendt til det
# og har mere kontrol på hvordan data kan organiseres og malipureres
# ved bruk av matimatiske og logiske metodene. f.eks.: list[function(var1, var2, ...)]
# Dette har jeg faktisk gjort før når jeg ville bruke samme listen flere gang men plukke ut ulike data.
# Istedet for å kopiere listen lagde jeg en komplisert matematisk funksjon istedet :D
# Og deretter en program som finner disse funksjoner for meg (polynomial interpolation, good for animations)

# Men, dette bruker jeg ikke ofte nå fordi:
# Må forklare det til folk og det tar mye tid.
# Liker ikke hvordan det ser ut.

# 3:
# a) Enten mengde eller en liste, trenger ikke duplikater, men kanskje vi
#    vil gjøre noe med navnene f.eks.: printe ut disse navnene med for løkke.
# b) Ordbok, må knytte navn til poeng summen.
# c) Enten mengde eller en liste, samme grunn som i sta.
# d) Ordbok, knytte allergiene og mat sammen.
