# Oppgave 5
# Task: Check if the user has logged in to the website successfully.
# The website is imaginary tho ;c

# Creating a message for a successfull login.
def login_successful():
    print(f"Login successful!")


# Creating a message for an unsuccessfull login.
def login_failed():
    print(f"Login failed!")


# Checking if user has a stable Wi-Fi connection. (For whatever reason, IDK)
def random_wifi_test(user_wifi: str) -> str:
    # don't want to import socket for this so imagine I ping them
    stability_status = "Bad"
    if user_wifi == "Good":
        stability_status = "Good"
    return stability_status


# A function that keeps track of time in seconds
def time_left() -> int:
    return 999


# Special function for debugging and control for admins.
def enable_developer_mode():
    print(f"Developer mode has been enabled.\n"
          f"You now have access to debug tools\n"
          f"and have more control over the data.")


# Website servers (please use your imagination for this)
websiteOnline = False
if not websiteOnline:
    print(f"Hold on, we are turning on the servers.")
    websiteOnline = True
    print(f"Website turned on successfully.")


userHasWifi = False
if random_wifi_test("Good") == "Good":
    userHasWifi = True

# Now checking if the user is in our database.
accountsDatabase = {"Admin": {"password": "Admin", "status": "Admin"},
                    "Test": {"password": "123", "status": "User"},
                    "Joe": {"password": "Mama", "status": "Banned"},
                    "RandomDude68+1": {"password": "69420", "status": "User"},
                    "xXCoolNicknameFrom2010Xx": {"password": "ProGamerYT2003", "status": "User"},
                    "A": {"password": "ABC", "status": "User"},
                    "Bot0": {"password": "000", "status": "Banned"},
                    "Bot1": {"password": "001", "status": "Banned"},
                    "Bot2": {"password": "002", "status": "Banned"}}

username = input(f"Username: ")
usernameInDatabase = False
for accountUsername in accountsDatabase:
    if accountUsername == username:
        usernameInDatabase = True

# These must be set now
accountNotBanned = True
passwordIsCorrect = False
twoStepVerified = False
captchaCompleted = False
notTimedOut = True
Admin = False

# Other checks depend on this value
if usernameInDatabase:

    # Checking if account is banned
    accountStatus = accountsDatabase[username]["status"]
    if accountStatus == "Banned":
        accountNotBanned = False

    # Checking if password is correct
    password = input(f"Password: ")
    if password == accountsDatabase[username]["password"]:
        passwordIsCorrect = True

    # Checking if the two-step verification has been completed
    verificationCode = "8273"
    verification = input(f"Your verification code is: {verificationCode}\n"
                         f"Please type in your verification code to continue: ")
    if verification == verificationCode:
        twoStepVerified = True

    # Checking if user is not a bot, captcha is not random though ;(
    pregeneratedCaptcha = "Zx93Ci"
    captcha = input(f"Please write the following to show that you are not a bot:\n"
                    f"{pregeneratedCaptcha}\n"
                    f">>> ")
    if captcha == pregeneratedCaptcha:
        captchaCompleted = True

    # Checking how much time has passed to see if the user is a grandpa/grandma
    if time_left() <= 0:
        notTimedOut = False

    # Checking if our user is an admin
    if accountsDatabase[username]["status"] == "Admin":
        Admin = True

# Checking if the user has logged in successfully.
# I did this by nesting the if statements (genius I know).
# That is a joke of course.
# I would never nest like that in reality.
# Simply replacing each one with "if not ..." would solve the problem.
# I just thought that this is funnier :D, sorry.
# I like the flipped pyramid though.
if websiteOnline:
    if userHasWifi:
        if usernameInDatabase:
            if accountNotBanned:
                if passwordIsCorrect:
                    if twoStepVerified:
                        if captchaCompleted:
                            if notTimedOut:
                                login_successful()
                                if Admin:
                                    enable_developer_mode()
                            else:
                                login_failed()
                        else:
                            login_failed()
                    else:
                        login_failed()
                else:
                    login_failed()
            else:
                login_failed()
        else:
            login_failed()
    else:
        login_failed()
else:
    login_failed()

# And yes, I did try to come up with as many somewhat useful conditions to meet.
# I was really considering adding stuff like requiring the passport for identity confirmation.
# Anyway, hope someone made a worse program than me (not less skilled, but more annoying).
