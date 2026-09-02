# Oppgave 2

# Temperatur i fahrenheit
temperatur_i_fahrenheit = 100
print(f"Temperatur i Fahrenheit: {temperatur_i_fahrenheit:.2f}")

# Temperatur i celsius
temperatur_i_celsius = (temperatur_i_fahrenheit - 32) * 5/9
print(f"Temperatur i Celsius: {temperatur_i_celsius:.2f}\n")

# Bruker kan nå taste inn tall
ny_temperatur_i_fahrenheit = float(input(f"Temperatur i Fahrenheit: "))
ny_temperatur_i_celsius = (ny_temperatur_i_fahrenheit - 32) * 5/9
print(f"Temperatur i Celsius: {ny_temperatur_i_celsius:.2f}")

# By the way, det er smartere og enklere med funksjoner.
# Men: CTRL C + CTRL V.
