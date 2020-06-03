import pygame
import random
from pygame.locals import *

from OpenGL.GL import *
from OpenGL.GLU import *
from CuboInteiro import CuboInteiro

def main():

    pygame.init()
    display = (800,600)
    pygame.display.set_mode(display, DOUBLEBUF|OPENGL)
    glEnable(GL_DEPTH_TEST) 

    glMatrixMode(GL_PROJECTION)
    gluPerspective(45, (display[0]/display[1]), 0.1, 50.0)

    NovoCubo = CuboInteiro(3, 1.5)
    NovoCubo.mainloop()

if __name__ == '__main__':
    main()
    pygame.quit()
    quit()