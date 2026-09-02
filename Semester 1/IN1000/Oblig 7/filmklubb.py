from film import Film

# Note: Jeg tenker ikke at det var mye som skal komenteres
#       Det er ganske åpenbart hva som skjer


class Filmklubb:
    def __init__(self):
        self.filmer = []  # Heter "self.filmer" istedet for "self._filmer"

    def les_filmer_fra_fil(self, filnavn: str) -> None:
        fil = open(filnavn, "r")  # Åpner filen (bare lese)
        for linje in fil:
            linje = linje.strip("\n").split(";")  # Sletter \n på slutten og deler opp
            linje[1] = int(linje[1])
            ny_film = Film(*linje)
            self.filmer.append(ny_film)

    def skriv_ut_alle_filmer(self) -> None:
        for film in self.filmer:
            film.skriv_ut_film()

    def registrer_film(self) -> None:
        # Antar perfekt syntaks
        tittel = input(f"Tittel til filmen: ")
        produksjons_ar = int(input("Produksjons ar: "))
        ny_film = Film(tittel, produksjons_ar)
        if ny_film in self.filmer:
            print("Filmen er allerede registrert.\n")
            return
        print()
        self.filmer.append(ny_film)

    def finn_film_tittel(self, tittel: str) -> Film | None:
        for film in self.filmer:
            if film.sjekk_tittel(tittel):
                return film
        return None

    @staticmethod
    def legg_til_skuespillere(film: Film) -> None:
        while True:
            navn = input("Skuespillerens navn: ")
            if not navn.strip(" "):  # Sjekker om den er tom
                break
            rolle = input("Skuespillerens rolle: ")
            print()
            film.ny_skuespiller(navn, rolle)

    def finn_filmer_periode(self, ar_1: int, ar_2: int) -> list[Film]:
        filmer = []
        for film in self.filmer:
            if film.sjekk_periode(ar_1, ar_2):
                filmer.append(film)
        return filmer
