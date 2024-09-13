import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split


class KNeigborsClassUE:
    # Construtor
    def __init__(self, k=3, p=2.0):
        self.k = k  # K, numero de vizinhos
        self.p = p  # P, parametro de distancia
        self.X_treino = None
        self.y_treino = None

    # Usado para construir o modelo.
    # Recebe X e y e converte para arrays Numpy
    def fit(self, X, y):
        self.X_treino = np.array(X)
        self.y_treino = np.array(y)

    # Método privado
    # Calcula a distancia Minkowski
    def _min_kowski(self, x1, x2):
        distancia = np.power(np.sum(np.abs(x1 - x2) ** self.p), 1 / self.p)
        return distancia

    # Aplica o modelo e devolve as etiquetas previstas para o conjunto fornecido
    def predict(self, X):
        X_teste = np.array(X)
        previsoes = []

        for x_teste in X_teste:
            # Calcula as distâncias entre x_teste e todos os pontos de treino
            distancias = [self._min_kowski(x_teste, x_treino) for x_treino in self.X_treino]

            # Obtém os índices dos k vizinhos mais próximos
            k_neighbors_indices = np.argsort(distancias)[:self.k]

            # Obtém as etiquetas correspondentes aos vizinhos mais próximos
            k_neighbors_labels = self.y_treino[k_neighbors_indices]

            # Determina a etiqueta mais comum entre os vizinhos mais próximos
            unicas_labels, counts = np.unique(k_neighbors_labels, return_counts=True)
            previstas_label = unicas_labels[np.argmax(counts)]
            previsoes.append(previstas_label)
        
        return np.array(previsoes)
    
    # Calcula a exatidão do modelo
    def score(self, X, y):
        X_teste = np.array(X)
        y_teste = np.array(y)

        previsoes = self.predict(X_teste)

        acertos = 0
        total_exemplos = len(y_teste)
        for i in range(total_exemplos):
            if previsoes[i] == y_teste[i]:
                acertos += 1
        
        return "{:.8f}".format(acertos/total_exemplos)


# Main
ficheiro = pd.read_csv("numericos/iris.csv")

X = ficheiro.values[:, 0:-1]
y = ficheiro.values[:, -1]

X_treino, X_teste, y_treino, y_teste = train_test_split(X, y,test_size=0.25, random_state=3)

knn = KNeigborsClassUE(3,2)
knn.fit(X_treino, y_treino)

exatidao = knn.score(X_teste, y_teste)
print("Exatidão:", exatidao)