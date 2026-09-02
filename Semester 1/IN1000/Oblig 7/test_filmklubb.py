from film import Film
from filmklubb import Filmklubb


def testprogram():
    # __init__
    # Opprett Filmklubb-objekt
    print("oppretter Filmklubb-objekt")
    # <fyll ut og fjern # på print-setningen>
    Filmklubb1 = Filmklubb()
    print()

    # les_filmer_fra_fil
    # Les inn filmer fra filen filmer.txt
    print("Leser filmer fra fil")
    # <fyll ut og fjern # på print-setningen>
    Filmklubb1.les_filmer_fra_fil("filmer.txt")
    print()

    # skriv_ut_alle_filmer
    # Skriv ut all info om alle filmer, sjekk at alt er lest fra fil
    # <fyll ut>
    print("Skriver ut all info om alle filmer")
    Filmklubb1.skriv_ut_alle_filmer()

    # registrer_film
    print("Registrerer ny film")
    # Legg inn en ny film med tittel og produksjonsår som leses fra terminal
    # <fyll ut og fjern # på print-setningen>
    Filmklubb1.registrer_film()
    # Skriv ut all info om alle filmer og sjekk at ny film ble registrert
    # <fyll ut>
    Filmklubb1.skriv_ut_alle_filmer()
    print()

    # Hvis __eq__ er implementert i Film og testes i registrer_film:
    print("Prøver å registrere film som allerede finnes")
    # <fyll ut og fjern # på print-setningen>
    Filmklubb1.registrer_film()

    # finn_film_tittel
    print("Leter etter film med (start på) usannsynlig tittel")
    # Kall på metoden med et argument som ingen titler starter med
    # Bruk print eller assert for å sjekke at resultatet er som forventet
    print(Filmklubb1.finn_film_tittel("som ingen titler starter med"))
    # <fyll ut og fjern # på print-setningen>
    print()

    print("Leter etter film med (start på) tittel 'Hidden '")
    # Kall på metoden med argument "Hidden "
    # Bruk print eller assert for å sjekke at resultatet er som forventet
    # <fyll ut og fjern # på print-setningen>
    print(Filmklubb1.finn_film_tittel("Hidden "))

    # legg_til_skuespillere
    print("Legg til skuespillere for en film")
    # kall metoden på film-objektet du fikk returnert fra finn_film_tittel
    # (navn og rolle for to skuespillere du velger selv leses fra terminal)
    # <fyll ut og fjern # på print-setningen>
    Filmklubb1.legg_til_skuespillere(Filmklubb1.finn_film_tittel("Hidden "))
    print()
    # SKriv ut all info om alle filmer og sjekk at resultatet er som forventet
    # <fyll ut>
    Filmklubb1.skriv_ut_alle_filmer()

    # finn_film_periode
    # Kall på metoden med argumentene etter=2000 og før=2024
    print("Leter ett filmer produsert etter 2000 og før 2024:")
    # Skriv ut titlene på filmer som returneres (bruk hent_tittel).
    # Kontroller at resultatene er som forventet
    # <fyll ut og fjern # på print-setningen>
    for film in Filmklubb1.finn_filmer_periode(2000, 2024):
        print(film.hent_tittel())
    print()

    # Kall på finn_film_periode med argumentene etter=2020 og før=2020
    print("Leter etter filmer produsert etter 2020 og før 2020:")
    # Kontroller at resultatet er som forventet (tom liste) med assert (evt skriv ut)
    # <fyll ut og fjern # på print-setningen>
    assert Filmklubb1.finn_filmer_periode(2020, 2020) == []
    print(Filmklubb1.finn_filmer_periode(2020, 2020))
    print()

    # Skriv ut all info om alle filmer og sjekk at resultatet er som forventet
    # <fyll ut>
    Filmklubb1.skriv_ut_alle_filmer()


testprogram()
