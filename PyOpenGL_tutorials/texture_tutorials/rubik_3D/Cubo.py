import pygame
import random
from pygame.locals import *

from OpenGL.GL import *
from OpenGL.GLU import *

vertices = (
    ( 1, -1, -1), ( 1,  1, -1), (-1,  1, -1), (-1, -1, -1),
    ( 1, -1,  1), ( 1,  1,  1), (-1, -1,  1), (-1,  1,  1)
)
cores = ((1, 0, 0), (0, 1, 0), (1, 0.5, 0), (1, 1, 0), (1, 1, 1), (0, 0, 1))

class Cubo():
    def __init__(self, id, N, escala):
        self.N = N
        self.escala = escala
        self.localizacao_inicial = [*id]
        self.localizacao_atual = [*id]
        self.rotacao = [[1 if i==j else 0 for i in range(3)] for j in range(3)]

    def isAffected(self, eixo, pedaco, direcao):
        return self.localizacao_atual[eixo] == pedaco

    def update(self, eixo, pedaco, direcao):

        if not self.isAffected(eixo, pedaco, direcao):
            return

        i, j = (eixo+1) % 3, (eixo+2) % 3
        for k in range(3):
            self.rotacao[k][i], self.rotacao[k][j] = -self.rotacao[k][j]*direcao, self.rotacao[k][i]*direcao

        self.localizacao_atual[i], self.localizacao_atual[j] = (
            self.localizacao_atual[j] if direcao < 0 else self.N - 1 - self.localizacao_atual[j],
            self.localizacao_atual[i] if direcao > 0 else self.N - 1 - self.localizacao_atual[i] )

    def transformeMat(self):
        scalaA = [[s*self.escala for s in a] for a in self.rotacao]  
        scalaT = [(p-(self.N-1)/2)*2.1*self.escala for p in self.localizacao_atual] 
        return [*scalaA[0], 0, *scalaA[1], 0, *scalaA[2], 0, *scalaT, 1]

    def desenhar(self, col, superf, vert, animacao, angulo, eixo, pedaco, direcao):

        glPushMatrix()
        if animacao and self.isAffected(eixo, pedaco, direcao):
            glRotatef( angulo*direcao, *[1 if i==eixo else 0 for i in range(3)] )
        glMultMatrixf( self.transformeMat() )

        glBegin(GL_QUADS)
        for i in range(len(superf)):
            glColor3fv(cores[i])
            for j in superf[i]:
                glVertex3fv(vertices[j])
        glEnd()

        glPopMatrix()
