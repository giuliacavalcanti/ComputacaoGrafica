from PyQt5.QtWidgets import (QWidget, QToolTip, QCheckBox, QInputDialog,
                             QDesktopWidget, QMainWindow, QAction, qApp, QMenu,
                             QHBoxLayout, QSizePolicy)
from PyQt5.QtGui import (QIcon, QFont, QColor)
import pickle

from canvas import Canvas

class MainWidget(QMainWindow):
    def __init__(self):
        QWidget.__init__(self)
        self.initUI()

    def initUI(self):
        QToolTip.setFont(QFont('Lato', 12))

        # WINDOW
        homeWidget = QWidget()
        self.setCentralWidget(homeWidget)
        self.resize(800, 600)
        self.center()
        self.setWindowTitle('Bezier Program!')
        self.canvas = Canvas(self)

        # MENU
        menuBar = self.menuBar()
        showLinesMenu = menuBar.addMenu('                                    ')
        self.guideBox = QCheckBox('Show guide', self)

        bezierMenu = menuBar.addMenu('Bezier menu')
        bezierAction = QAction('Bézier curve', self)
        
        bezierMenu.addAction(bezierAction)

        bezierAction.triggered.connect(self.canvas.addBCurve)
        self.guideBox.stateChanged.connect(self.canvas.toggleGuide)
        

        # AREA DE DESENHO
        mainLayout = QHBoxLayout()

        self.canvas.setStyleSheet("QWidget { background-color: %s }" %
                                 QColor(255, 255, 255).name())
        self.canvas.setSizePolicy(QSizePolicy.Expanding,
                                 QSizePolicy.Expanding)
        mainLayout.addWidget(self.canvas)
        homeWidget.setLayout(mainLayout)
        self.show()

    def center(self):
        qr = self.frameGeometry()
        cp = QDesktopWidget().availableGeometry().center()
        qr.moveCenter(cp)
        self.move(qr.topLeft())

    def closeEvent(self, event):
        event.accept()

    def flush(self):
        pass  # TODO
