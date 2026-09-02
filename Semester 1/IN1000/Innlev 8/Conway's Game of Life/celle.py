class Celle:
    def __init__(self):
        self._status = "doed"         # Om celle er levende eller doed
        self._naboer = []             # Alle naboer rundt celle (1 til hver side)
        self._ant_levende_naboer = 0  # Antall levende naboer rundt celle

    def sett_doed(self) -> None:
        self._status = "doed"  # Celle blir doed

    def sett_levende(self) -> None:
        self._status = "levende"  # Celle blir levende

    def er_levende(self) -> bool:
        if self._status == "doed":  # Sjekker om celle er doed
            return False            # False -> Doed
        return True                 # True -> Levende

    def hent_status_tegn(self) -> str:
        if Celle.er_levende(self):  # Sjekker om celle er levende
            return "O"              # Levende -> Skal tegnes "O"
        return "."                  # Doed -> Skal tegnes "."

    def legg_til_nabo(self, nabo) -> None:
        self._naboer.append(nabo)  # Legger til en nabo (celle)

    def tell_levende_naboer(self) -> None:
        self._ant_levende_naboer = 0  # Starter med 0 levende nabo celler
        for nabo in self._naboer:     # For hver nabo celle gjor en sjekk
            if nabo.er_levende():     # Hvis nabo celle er levende...
                self._ant_levende_naboer += 1  # ... registrerer levende celle

    def oppdater_status(self) -> None:
        if self._ant_levende_naboer == 3:  # Hvis akkurat nokk for reproduksjon -> Levende
            Celle.sett_levende(self)
        elif self._ant_levende_naboer != 2:  # Sjekket 3 allerede, hvis ikke 2 heller da er det enten over eller under populasjon -> Doed
            Celle.sett_doed(self)
