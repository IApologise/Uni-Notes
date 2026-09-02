# Oppgave 3

# Programmet beregner prisen til billettene avhengig av alderen

def billettprisen(alder: int = 0):
    if not alder:  # Sjekker om vi allerede vet alderen til brukeren (jeg er lat)
        alder = int(input("Hva er alderen din?\n"
                          ">>> "))  # Kjøpers alder
    billettpris = 0  # Prisen til biletten (Why?)

    if alder <= 17:
        billettpris = 30  # Prisen til en barnebillett
    elif alder > 17:
        billettpris = 50  # Prisen til en vanlige bilett
    elif alder >= 63:
        billettpris = 35  # Prisen til en pensjonsbillett

    print(f"\n"  # Mellomrom
          f"Billett prisen er: {billettpris}")


billettprisen(15)  # Test 1
billettprisen(31)  # Test 2
billettprisen(63)  # Test 3

# 1. Hvis inputen er ikke en heltall kan det gå galt.
# 2. Hvis alderen er negativ er det litt rart, men teknisk sett er alt ok.
# 3. Hvis alderen er 63 eller mer blir prisen 50 uansett fordi at den
#    første elif-en skal gjennomføres først og det som tilfredsstiller
#    den andre elif-en også tilfredsstiller den første elif-en.
# 4. La oss anta at alt er programmert riktig. Hvorfor definerer vi billettprisen
#    som 0 når den skal endres uansett avhengig av alderen og blir aldri 0.
#    Vi kan ha det som 35 istedet og slette "else:" for eksempel.
