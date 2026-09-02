from random import randint
from celle import Celle


class Rutenett:
    def __init__(self, rader: int, kolonner: int):
        self._ant_rader = rader        # Antall rader som skal visualiseres (Kan skje en problem hvis for mange rader hvor den blir til en ekstra kolonne)
        self._ant_kolonner = kolonner  # Antall kolonner som skal visualiseres
        self._rutenett = []            # Tom liste, Skal bli til matrix

        Rutenett._lag_tomt_rutenett(self)  # Lager en tom rutenett

    def _lag_tomt_rutenett(self) -> None:
        for rad in range(self._ant_rader):  # Copy paste rader for å lage rutenettet
            tom_rad = Rutenett._lag_tom_rad(self)
            self._rutenett.append(tom_rad)

    def _lag_tom_rad(self) -> list:
        tom_rad = []
        for kolonne in range(self._ant_kolonner):  # Lager en liste av riktig lengde (antall kolonner i en rad)
            tom_rad.append(None)
        return tom_rad

    def fyll_med_tilfeldige_celler(self) -> None:
        for kolonne in range(self._ant_kolonner):        # For hver kolonne
            for rad in range(self._ant_rader):           # For hver rad i kolonne
                Rutenett.lag_celle(self, rad, kolonne)   # Lager og registrerer en tilfeldig celle

    def lag_celle(self, rad, kol):
        celle = Celle()                   # Lager en celle
        if randint(0, 2) == 0:      # Sjekker om blir levende
            celle.sett_levende()          # Ja -> Sette til levende
        self._rutenett[rad][kol] = celle  # Setter celle i sinn plass

    def hent_celle(self, rad: int, kol: int) -> Celle | None:
        if self._ant_rader >= rad >= 0 and self._ant_kolonner >= kol >= 0:
            return self._rutenett[rad][kol]  # Returnerer celle
        return None  # Hvis fant ikke -> None

    def tegn_rutenett(self) -> None:
        for n in range(10):  # Tommer terminalen slik at tegnet er lett synlig
            print()

        # Printer ut celler
        for rad in range(self._ant_rader):
            for kolonne in range(self._ant_kolonner):
                print("", self._rutenett[rad][kolonne].hent_status_tegn(), end=" ")  # Weird...
            print()  # Ny kolonne

    def _sett_naboer(self, rad, kol) -> None:

        # X: Posisjon i en kolonne (horizontal posisjon)
        # Y: Posisjon i en rad (vertikal posisjon)

        # Hvilke naboer (vertikalt) som finnes
        from_x, to_x = kol - 1, kol + 2  # Vanlig sok
        if kol < 1:
            from_x = 0  # Ingen naboer over cellen
        elif kol >= self._ant_kolonner - 1:
            to_x = self._ant_kolonner  # Ingen naboer under cellen

        # Samme med rader (horizontal), kunne ha laget funksjonen istedet, but whatever
        from_y, to_y = rad - 1, rad + 2  # Vanlig sok
        if rad < 1:
            from_y = 0  # Ingen naboer til venstre fra cellen
        elif rad >= self._ant_rader - 1:
            to_y = self._ant_rader  # Ingen naboer til høyre fra cellen

        # Legger til naboer til cellen
        for y in range(from_y, to_y):
            for x in range(from_x, to_x):
                if self._rutenett[rad][kol] != self._rutenett[y][x]:  # selve cellen telles ikke som sinn nabo
                    self._rutenett[rad][kol].legg_til_nabo(self._rutenett[y][x])  # Legger til en nabo til cellen

                # Jeg synes dette er bedre fordi kortere og enklere: self._rutenett[y][x].legg_til_nabo(self)
                # Ideen er slik: Legg til seg selv som en nabo til alle naboer
                # Men dette er ikke intruksen til hvordan denne funksjonen skal operere
                # Instruken er slik: Legge til alle naboer til seg selv

    # Ma jeg forklare dette?
    # Bare for a vare sikker: setter en nabo til hver celle
    def koble_celler(self) -> None:
        for rad in range(self._ant_rader):
            for kolonne in range(self._ant_kolonner):
                Rutenett._sett_naboer(self, rad, kolonne)  # Setter naboer for hver celle

    # Nesten det samme som koble_celler
    def hent_alle_celler(self) -> list:
        new_list = []
        for rad in self._rutenett:
            for celle in rad:
                new_list.append(celle)
        return new_list

    def antall_levende(self) -> int:
        levende_celler = 0
        for rad in self._rutenett:
            for celle in rad:
                if celle.er_levende():  # Sjekker om celle er levende
                    levende_celler += 1
        return levende_celler  # Returnerer total antall levende celler i et rutenett
