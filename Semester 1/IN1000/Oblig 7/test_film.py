from film import Film


def test_film():
    # __init__
    # Opprett to film-objekter med tittel og produksjonsår du velger selv
    print("Oppretter to filmer")
    # <fyll ut og fjern # på print-setningen>
    Film1 = Film("The King's Man", 2021)
    Film2 = Film("Pixels", 2015)
    print()

    # hent_tittel
    # Skriv ut tittel på de to filmene
    print("Skriver ut titler på to filmer")
    # <fyll ut og fjern # på print-setningen>
    print(f"Film1: {Film1.hent_tittel()}\n"
          f"Film2: {Film2.hent_tittel()}")
    print()

    # ny_skuespiller
    # Legg til to skuespillere og deres roller for en av filmene, skriv ut alt om filmen
    print("Legger til to skuespillere")
    # <fyll ut og fjern # på print-setningen>
    Film1.ny_skuespiller("Ralph Fiennes", "Orlando Oxford")
    Film1.ny_skuespiller("Gemma Arterton", "Polly Wilkins")
    Film1.skriv_ut_film()

    # Prøv å legge inn en av skuespillerne igjen, med en ny rolle, og sjekk at rollen ikke blir endret
    print("Tester ulovlig innlegging av skuespiller")
    # <fyll ut og fjern # på print-setningen>
    Film1.ny_skuespiller("Ralph Fiennes", "Orlando Oxford")
    print()

    # skriv_ut_film
    # Skriv ut all informasjon om begge filmer du har lagt inn
    print("Skriver ut all info om to filmer:")
    # <fyll ut og fjern # på print-setningen>
    Film1.skriv_ut_film()
    Film2.skriv_ut_film()

    # hent_alle_skuespiller_navn
    # Skriv ut skuespillernes navn for den filmen som har to
    print("Henter og skriver ut alle skuespillernavn for en film:")
    # <fyll ut og fjern # på print-setningen>
    for skuespiller in Film1.hent_skuespiller_navn():
        print(skuespiller)
    print()

    # sjekk_periode
    # Sjekk om en film du har lagt inn er i en periode du velger
    # (velg periode som skal gi True og sjekk at dette blir resultatet)
    print("Sjekker at en film er i oppgitt periode")
    # <fyll ut og fjern # på print-setningen>
    if Film1.sjekk_periode(2020, 2022):
        print("Passed")
    print()

    # Sjekk om en film er i en periode som skal gi False
    # (velg samme årstall til begge argumenter og sjekk resultat er False)
    print("Sjekker at en film ikke kan være produsert før og etter samme år")
    # <fyll ut og fjern # på print-setningen>
    if not Film1.sjekk_periode(2021, 2022):
        print("Passed")
    print()

    # sjekk_tittel
    # Sjekk om en film har en tittel som starter på en streng som du selv velger
    print("Sjekker om starten på en films tittel kjennes igjen")
    # <fyll ut og fjern # på print-setningen>
    if Film1.sjekk_tittel("The King"):
        print("Passed")
    print()

    # __str__
    # Skriv ut film-objekt med print
    print("Skriver ut en film med print (test av __str__)")
    # <fyll ut og fjern # på print-setningen>
    print(Film1)

    # test __eq__ (frivillig)
    print("tester __eq__ med to ulike filmer:")
    # <fyll ut og fjern # på print-setningen>
    if not Film1 == Film2:
        print("Passed")
    print("\nTester __eq__ med to like filmer:")
    # <fyll ut og fjern # på print-setningen>
    Film3 = Film("The King's Man", 2021)
    if Film1 == Film3:
        print("Passed")


test_film()
