import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split

class NBayesClasseUE:
    # Construtor
    def __init__(self, alpha=1.0):
        self.alpha = alpha
        self.classes = {}   # Guarda o nome da classe e um contador
        self.atributo = {}  # Guarda cada valor de atributo e a sua contagem
        self.valores_atributo = {}  # Guarda o numero de valores diferentes que cada atributo tem
        
    def fit(self, X, y):
        for idx in range(len(X)):
            instancia = X[idx]
            etiqueta = y[idx]
            
            # Verifica se a etiqueta já existe no dicionario
            if etiqueta not in self.classes:
                self.classes[etiqueta] = 0
                
            self.classes[etiqueta] += 1
            
            i = 0
            # Adiciona no dicionario a contagem de ocorrencia de cada valor de atributo
            for atrib in instancia:
                chave_atributo = f'Atributo_{i}_{atrib}'
                
                if etiqueta not in self.atributo:
                    self.atributo[etiqueta] = {}
                
                if chave_atributo not in self.atributo[etiqueta]:
                    self.atributo[etiqueta][chave_atributo] = 0
                               
                self.atributo[etiqueta][chave_atributo] +=1
                
                if i not in self.valores_atributo:
                    self.valores_atributo[i] = set()

                self.valores_atributo[i].add(atrib)

                i += 1
        
    def predict(self, X):
        previsoes = []
        
        for instancias_teste in X:
            melhores_probabilidades = -1
            classe_predita = None
            for label in self.classes.keys():
                prob = (self.classes[label] + self.alpha) / (sum(self.classes.values()) + self.alpha*len(self.classes))

                i = 0

                for atrib in instancias_teste:
                    chave_atributo = f'Atributo_{i}_{atrib}'
                    
                    if chave_atributo in self.atributo[label]:
                        prob *= (self.atributo[label][chave_atributo]+ self.alpha) / (self.classes[label] + self.alpha * len(self.valores_atributo[i]))
                    #Caso apareça um valor de atributo no conjunto de teste que não tenha aparecido no conjunto de treino
                    else:
                        prob*= (0 + self.alpha) / (self.classes[label] + self.alpha * len(self.valores_atributo[i]))
                    i += 1
                   
                if prob > melhores_probabilidades:
                    melhores_probabilidades = prob
                    classe_predita = label

            previsoes.append(classe_predita)
        
        return previsoes
    
    # Calcula a exatidão do modelo
    def score(self, X, y):
        y_teste = np.array(y)
        
        previsoes = self.predict(X)
        
        acertos = 0
        total_exemplos = len(y_teste)
        for i in range(total_exemplos):
            if previsoes[i] == y_teste[i]:
                acertos += 1

        return "{:.5f}".format(acertos/total_exemplos)
    
    
#Main
ficheiro = pd.read_csv("nominais/weather-nominal.csv") #bc-nominal weather-nominal contact-lenses
X = ficheiro.values[:, 0:-1]
y = ficheiro.values[:, -1]

X_treino, X_teste, y_treino, y_teste = train_test_split(X, y,test_size=0.25, random_state=3)

nb_classificador = NBayesClasseUE(alpha=3.0)

nb_classificador.fit(X_treino, y_treino)

exatidao = nb_classificador.score(X_teste, y_teste)
print("Exatidão:", exatidao)