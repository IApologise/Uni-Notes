class Film:
    def __init__(self, tittel: str, produksjons_ar: int):
        self.tittel = tittel
        self.produksjons_ar = produksjons_ar
        self.skuespillere = {}

    def hent_tittel(self) -> str:
        return self.tittel  # Tittel :)

    def ny_skuespiller(self, navn: str, rolle: str) -> None:
        try:  # Sjekker om skuespiller finnes i ordboken
            if rolle.lower() == self.skuespillere[navn].lower():  # Sjekker om
                print("Skuespiller finnes allerede i ordboken.")  # Feilmelding
                return  # Avslutte
        except KeyError:  # Hvis skuespiller finnes ikke i ordboken:
            self.skuespillere.update({navn: rolle})  # Legger til skuespiller og rolle
        # Note: Alt dette kan også starte slik: "if navn in self.skuespillere:" ...
        #       Da trenger man ikke except i dette tilfellet, men slik er det fortere
        #       Optimization is important, even when it is not needed.

    def hent_skuespiller_navn(self) -> list[str]:
        skuespillere = self.skuespillere.keys()  # Får navner til skuespillere
        skuespillere = list(skuespillere)  # Gjør om til en liste
        return skuespillere
        # Dette passer også: "return list(self.skuespillere.keys())", alt på 1 linje

    def skriv_ut_film(self) -> None:
        print(self)  # Nevermind, that was easy

    def sjekk_periode(self, ar_1: int, ar_2: int) -> bool:
        if ar_2 > self.produksjons_ar > ar_1:  # Mellom -> ar_1 og ar_2 er ikke inkludert
            return True
        return False

    def sjekk_tittel(self, tittel_start: str) -> bool:
        if tittel_start == self.tittel[:len(tittel_start)]:  # Making these two same length (hard I know)
            return True
        return False

    # Cmon I have to remake "skriv_ut_film()"? Not cool.
    # Now I had to remake the original function because of it.
    # You know how hard it is?
    def __str__(self) -> str:
        string = f"{self.tittel}({self.produksjons_ar}). Medvirkende:"
        if len(self.skuespillere) < 1:  # Sjekker om den har ikke skuespillere
            string += "\nIngen."  # Ingen skuespillere ;(
        for skuespiller in self.skuespillere:  # En linje for hver skuespiller
            string += f"\n{skuespiller} som {self.skuespillere[skuespiller]},"  # Tekst
        string = string[:-1]  # Sletter komma fra siste linje (eller punktum hvis ingen skuespillere)
        string += ".\n"  # Legger til punktum til siste linje
        return string

    def __eq__(self, annen) -> bool:
        if self.tittel == annen.tittel and self.produksjons_ar == annen.produksjons_ar:
            return True
        return False
