from PyQt5.QtWidgets import (QWidget, QToolTip, QMessageBox, QInputDialog,
                             QDesktopWidget, QMainWindow, QAction, qApp, QMenu,
                             QHBoxLayout, QSizePolicy)
from PyQt5.QtGui import (QIcon, QFont, QColor)
import pickle

from canvas import Canvas
from signals import Signals
from sideBarFunctions import SideBarFunctions


class MainWidget(QMainWindow):
    def __init__(self):
        QWidget.__init__(self)
        self.c = Signals()
        self.initUI()

    def initUI(self):
        QToolTip.setFont(QFont('Lato', 12))

        # SET UP THE WINDOW
        homeWidget = QWidget()
        self.setCentralWidget(homeWidget)
        self.resize(800, 600)
        self.center()
        self.setWindowTitle('Bezier Program!')
        self.board = Canvas(self)

        # TOP MENU
        menubar = self.menuBar()
        curveMenu = menubar.addMenu('Bezier')
        bezierAction = QAction('Bézier curve', self)
        curveMenu.addAction(bezierAction)

        bezierAction.triggered.connect(self.board.addBCurve)

        # SET UP DRAWING SPACE AND TOOLBAR
        mainLayout = QHBoxLayout()

        self.board.setStyleSheet("QWidget { background-color: %s }" %
                                 QColor(255, 255, 255).name())
        self.board.setSizePolicy(QSizePolicy.Expanding,
                                 QSizePolicy.Expanding)
        self.SideBarFunctions = SideBarFunctions(self)

        self.SideBarFunctions.setFixedWidth(250)
        self.SideBarFunctions.setStyleSheet("QWidget { background-color: %s }" %
                                       QColor(128, 128, 128).name())
        self.SideBarFunctions.setSizePolicy(QSizePolicy.Fixed,
                                       QSizePolicy.Expanding)
        mainLayout.addWidget(self.board)
        mainLayout.addWidget(self.SideBarFunctions)
        homeWidget.setLayout(mainLayout)
        self.c.updateStatusBar.connect(self.updateStatusBar)
        self.c.updateSelectedPoint.connect(self.SideBarFunctions.updateSelectedPoint)
        self.c.gotoPoint.connect(self.board.gotoPoint)
        self.c.moveXPoint.connect(self.board.moveXPoint)
        self.c.moveXPointTo.connect(self.board.moveXPointTo)
        self.c.moveYPoint.connect(self.board.moveYPoint)
        self.c.moveYPointTo.connect(self.board.moveYPointTo)
        self.c.toggleGuide.connect(self.board.toggleGuide)
        self.c.addCurve.connect(self.SideBarFunctions.addCurve)
        self.c.selectCurve.connect(self.board.selectCurve)
        self.c.selectedCurveName.connect(self.SideBarFunctions.selectedCurveName)
        self.board.connectEvents(self.c)
        self.SideBarFunctions.connectEvents(self.c)
        self.show()

    def updateStatusBar(self, text):
        self.statusBar().showMessage(text)

    def center(self):
        qr = self.frameGeometry()
        cp = QDesktopWidget().availableGeometry().center()
        qr.moveCenter(cp)
        self.move(qr.topLeft())

    def closeEvent(self, event):
        event.accept()

    def flush(self):
        pass  # TODO
