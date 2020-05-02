import sys
from PyQt5 import QtCore, QtGui, QtWidgets, uic
from PyQt5.QtCore import Qt


class MainWindow(QtWidgets.QMainWindow):
    def __init__(self):
        super().__init__()

        self.label = QtWidgets.QLabel()
        canvas = QtGui.QPixmap(400, 300)
        self.label.setPixmap(canvas)
        self.setCentralWidget(self.label)
        self.draw_rectangle_texture()


    def draw_line(self):
        painter = QtGui.QPainter(self.label.pixmap())        
        painter.setPen(Qt.white)
        painter.drawLine(10, 10, 300, 200)
        painter.end()
 
    def draw_rectangle(self):
        painter = QtGui.QPainter(self.label.pixmap())
        pen = QtGui.QPen()
        pen.setWidth(40)
        pen.setColor(QtGui.QColor('red'))
        painter.setPen(pen)
        painter.drawPoint(200, 150)
        painter.end()

    def draw_rectangle_texture(self):
        from random import randint
        painter = QtGui.QPainter(self.label.pixmap())
        pen = QtGui.QPen()
        pen.setWidth(3)
        pen.setColor(QtGui.QColor('red'))
        painter.setPen(pen)

        for n in range(10000):
            painter.drawPoint(
                200+randint(-100, 100),  # x
                150+randint(-100, 100)   # y
                )
        painter.end()
    
    def draw_something(self):
        from random import randint, choice
        colors = ['#FFD141', '#376F9F', '#0D1F2D', '#E9EBEF', '#EB5160']

        painter = QtGui.QPainter(self.label.pixmap())
        pen = QtGui.QPen()
        pen.setWidth(3)
        painter.setPen(pen)

        for n in range(10000):
            # pen = painter.pen() you could get the active pen here
            pen.setColor(QtGui.QColor(choice(colors)))
            painter.setPen(pen)
            painter.drawPoint(
                200+randint(-100, 100),  # x
                150+randint(-100, 100)   # y
                )
        painter.end()

app = QtWidgets.QApplication(sys.argv)
window = MainWindow()
window.show()
app.exec_()