# Opgpave 2

# Programmet gir brukeren mulighet å legge til nye varene og prisene.
# Til slutt printer ut programmet hele ordboken før og etter endringene.

# Definerer dictionary
butikkvarer = {"melk": 14.9,     # Prisen til melk
               "brød": 24.9,     # Prisen til brød
               "yoghurt": 12.9,  # Prisen til yoghurt
               "pizza": 39.9}    # Prisen til pizza

print(butikkvarer)  # Printer ut dictionary med en enkelt print funksjon
print()  # Mellomrom

antall_nye_varer = 2
print(f"Skrive inn informasjon til {antall_nye_varer} nye varer.")
for n in range(antall_nye_varer):  # Antall nye varer vi skal legge til
    varenavn = input("Navnet til nye varet: ")       # Input 1...
    prisen = float(input("Prisen til nye varet: "))  # Input 2...
    butikkvarer.update({varenavn: prisen})           # Legger til inputene til dictionary
print()  # Mellomrom

print(butikkvarer)  # Printer ut dictionary igjen
