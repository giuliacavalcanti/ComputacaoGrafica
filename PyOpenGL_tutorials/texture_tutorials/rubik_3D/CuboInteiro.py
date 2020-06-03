import pygame
import random
from pygame.locals import *

from OpenGL.GL import *
from OpenGL.GLU import *
from Cubo import Cubo


vertices = (
    ( 1, -1, -1), ( 1,  1, -1), (-1,  1, -1), (-1, -1, -1),
    ( 1, -1,  1), ( 1,  1,  1), (-1, -1,  1), (-1,  1,  1)
)
superficies = ((0, 1, 2, 3), (3, 2, 7, 6), (6, 7, 5, 4), (4, 5, 1, 0), (1, 5, 7, 2), (4, 0, 3, 6))
cores = ((1, 0, 0), (0, 1, 0), (1, 0.5, 0), (1, 1, 0), (1, 1, 1), (0, 0, 1))

class CuboInteiro():
    def __init__(self, N, escala):
        self.N = N
        cr = range(self.N)
        self.cubos = [Cubo((x, y, z), self.N, escala) for x in cr for y in cr for z in cr]

    def mainloop(self):

        rotacao_cubo_keys  = { K_UP: (-1, 0), K_DOWN: (1, 0), K_LEFT: (0, -1), K_RIGHT: (0, 1)}
        rotacao_parte_keys = {
            K_1: (0, 0, 1), K_2: (0, 1, 1), K_3: (0, 2, 1), K_4: (1, 0, 1), K_5: (1, 1, 1),
            K_6: (1, 2, 1), K_7: (2, 0, 1), K_8: (2, 1, 1), K_9: (2, 2, 1),
            K_F1: (0, 0, -1), K_F2: (0, 1, -1), K_F3: (0, 2, -1), K_F4: (1, 0, -1), K_F5: (1, 1, -1),
            K_F6: (1, 2, -1), K_F7: (2, 0, -1), K_F8: (2, 1, -1), K_F9: (2, 2, -1),
        }  

        angulo_x, angulo_y, rotacao_cubo = 0, 0, (0, 0)
        animacao, animacao_angulo, animacao_velocidade = False, 0, 5
        acao = (0, 0, 0)
        while True:

            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    quit()
                if event.type == KEYDOWN:
                    if event.key in rotacao_cubo_keys:
                        rotacao_cubo = rotacao_cubo_keys[event.key]
                    if not animacao and event.key in rotacao_parte_keys:
                        animacao, acao = True, rotacao_parte_keys[event.key]
                if event.type == KEYUP:
                    if event.key in rotacao_cubo_keys:
                        rotacao_cubo = (0, 0)

            angulo_x += rotacao_cubo[0]*2
            angulo_y += rotacao_cubo[1]*2

            glMatrixMode(GL_MODELVIEW)
            glLoadIdentity()
            glTranslatef(0, 0, -40)
            glRotatef(angulo_y, 0, 1, 0)
            glRotatef(angulo_x, 1, 0, 0)

            glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT)

            if animacao:
                if animacao_angulo >= 90:
                    for cubo in self.cubos:
                        cubo.update(*acao)
                    animacao, animacao_angulo = False, 0

            for cubo in self.cubos:
                cubo.desenhar(cores, superficies, vertices, animacao, animacao_angulo, *acao)
            if animacao:
                animacao_angulo += animacao_velocidade

            pygame.display.flip()
            pygame.time.wait(10)
