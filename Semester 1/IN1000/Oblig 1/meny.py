# Oppgave 3.1 - 3.3

# Beskrivelse:
# I denne oppgaven skulle en meny lages.
# Deretter må brukeren velge tilbehør.
# Programmet skal sørge for balansert diet som beskrevet i oppgaven.
# Og det gjør dette programmet.

# Definerer en klasse som heter mat
class Mat:
    def __init__(self, navn: str, gronnsak: bool):  # Lager konstruktøren (er ganske lik funksjonene)
        self.navn = navn  # Matens navn
        self.gronnsak = gronnsak  # Er maten en grønnsak , True/False (boolean)

# Når man lager en konstruktøren fungerer den nesten som en funksjon.
# __init__ definerer og lager selve objektet.
# For å definere dette objektet trenger vi "navn" og "gronnsak".
# Disse verdiene må være av en spesifikk type som er spesifisert slik "{variabel}: {type}"
# Så lagrer vi verdiene som en del av Mat


# Definerer en meny
class Meny:
    
    # Konstruktøren
    def __init__(self):
        self.meny = {"hovedretter": [],        # En liste av alt som er en hovedrett
                     "grønnsaktilbehør": [],  # En liste av alt som er en grønnsaktilbehør
                     "saustilbehør": []}       # En liste av alt som er en saustilbehør
        self.vegetarian = []  # En liste med alt som er vegetarian

    # Her lager jeg en funksjon knyttet til en Meny for å legge til både hovedretter og tilbehør
    def legge_til(self, kategori: str, *args: Mat) -> None:  # Slik fungerer funksjonen:
        for arg in args:                                     # Inputene er kategori som vi vil legge den til
            self.meny[kategori].append(arg)                  # og *args er alt som blir knyttet til kategorien
            if arg.gronnsak:                                 # Så, vi leger dem inn i en kategori definert i menyen
                self.vegetarian.append(arg)                  # og sjekker om legemet er vegetarian :)

    # En funksjon for å vise menyen til brukeren
    def vis(self) -> None:

        # Definerer en funksjon som gjør det enklere å printe ut.
        def vis_kategori(kategori: str):             # Slik fungerer den:
            print(f"{kategori.capitalize()}:")       # Input til funksjonen "kategori" er kategorien vi
            for mat in self.meny[kategori]:          # vil printe ut. Vi printer ut kategori og deretter
                print(f"- {mat.navn.capitalize()}")  # printer ut alt som var en del av kategorien

        # Printer ut menyen
        vis_kategori("hovedretter")       # Her er en funksjon som printer ut hovedretter
        vis_kategori("grønnsaktilbehør")  # Her er en funksjon som printer ut grønnsakstilbehør
        vis_kategori("saustilbehør")      # Her er en funksjon som printer ut Saustilbehør


# Lager hovedretter
Biff = Mat("biff", False)    # Lager en definisjon av biff
Torsk = Mat("torsk", False)  # Lager en definisjon av torsk
Salat = Mat("salat", True)   # Lager en definisjon av salat

# Lager grønnsakstilbehør
Gulrotter = Mat("gulrøtter", True)  # Lager en definisjon av gulrøtter

# Lager saustilbehør
Bernaise = Mat("bernaise", False)   # Lager en definisjon av bernaise

# Lager en meny og legger mat til menyen
Meny = Meny()
Meny.legge_til("hovedretter", Biff, Torsk, Salat)  # Legger til alle hovedrettene
Meny.legge_til("grønnsaktilbehør", Gulrotter)      # Legger til alle grønnsakstilbehør
Meny.legge_til("saustilbehør", Bernaise)           # Legger til alle saustilbehør

# Viser en meny til brukeren
Meny.vis()

# Lar brukeren velge en hovedrett og tilbehør (antar nesten perfekt syntaks)
hovedrett = input(f"\n"  # Liker å ha mellomrom :)
                  f"Velg hovedretten >>> ").lower()
tilbehor = input("Velg tilbehør >>> ").lower()

print()  # Igjen, liker å ha mellomrom :)
# Kritiserer brukeren
if hovedrett not in Meny.vegetarian and tilbehor not in Meny.vegetarian:  # Hvis brukeren spiser ikke nok grønnsaker.
    print("Du spiser ikke nok grønnsaker!")                               # Brukeren må spise sunt >:(
elif hovedrett in Meny.vegetarian and tilbehor in Meny.vegetarian:  # hvis brukeren spiser for mye grønnsaker.
    print("Du har valgt et vegetarmåltid")                          # Ingen liker vegetarianer >:(
else:                                                   # Hvis brukeren spiser sunt :)
    print(f"Du har valgt {hovedrett} med {tilbehor}.")  # "Perfectly balanced, as all things should be" - Thanos from Avengers: Infinity War
