from PyQt5.QtCore import Qt
from PyQt5.QtWidgets import (QPushButton,
                             QFrame, QLabel, QLineEdit,
                             QHBoxLayout, QVBoxLayout, QSplitter,
                             QCheckBox, QComboBox)
from PyQt5.QtGui import (QFont, QColor)

from sideBar import SideBar


class SideBarFunctions(QFrame):
    def __init__(self, parent):
        super().__init__(parent)
        self.initUI()
        self.c = None

    def connectEvents(self, c):
        self.c = c

    def updateSelectedPoint(self, x, y):
        self.xFrame.textField.setText(x)
        self.yFrame.textField.setText(y)
        self.update()

    def initUI(self):
        self.cbox = QComboBox(self)
        self.guideBox = QCheckBox('Show guide lines', self)
        lineLayout = QVBoxLayout()
        lineLayout.addWidget(self.cbox)
        lineLayout.addWidget(self.guideBox)
        self.cbox.activated[str].connect(self.selectCurve)
        self.guideBox.stateChanged.connect(self.toggleGuide)
        lineZone = QFrame()
        lineZone.setLayout(lineLayout)

        self.xFrame = SideBar(self, "X:")
        self.xFrame.textField.textEdited.connect(self.changeX)
        self.xFrame.plus1.clicked.connect(self.increaseX)
        self.xFrame.minus1.clicked.connect(self.reduceX)
        self.yFrame = SideBar(self, "Y:")
        self.yFrame.textField.textEdited.connect(self.changeY)
        self.yFrame.plus1.clicked.connect(self.increaseY)
        self.yFrame.minus1.clicked.connect(self.reduceY)

        pointLayout = QVBoxLayout()
        pointLayout.addStretch(1)
        pointLayout.addWidget(self.xFrame)
        pointLayout.addWidget(self.yFrame)
        pointZone = QFrame()
        pointZone.setLayout(pointLayout)

        splitter = QSplitter(Qt.Vertical)
        splitter.addWidget(lineZone)
        splitter.addWidget(pointZone)
        vbox = QVBoxLayout()
        vbox.addWidget(splitter)
        self.setLayout(vbox)
        self.show()

    def reduceX(self):
        self.c.moveXPoint.emit(-1)
        x = int(float(self.xFrame.textField.text()))
        self.xFrame.textField.setText(str(x - 1))
        self.update()

    def increaseX(self):
        self.c.moveXPoint.emit(1)
        x = int(float(self.xFrame.textField.text()))
        self.xFrame.textField.setText(str(x + 1))
        self.update()

    def changeX(self, text):
        self.c.moveXPointTo.emit(0 if text == '' else int(float(text)))
        self.update()

    def reduceY(self):
        self.c.moveYPoint.emit(-1)
        y = int(float((self.xFrame.textField.text())))
        self.yFrame.textField.setText(str(y - 1))
        self.update()

    def increaseY(self):
        self.c.moveYPoint.emit(1)
        y = int(float(self.yFrame.textField.text()))
        self.yFrame.textField.setText(str(y + 1))
        self.update()

    def changeY(self, text):
        self.c.moveYPointTo.emit(0 if text == '' else int(float(text)))
        self.update()

    def gotoPoint(self, text):
        self.c.gotoPoint.emit(0 if text == '' else int(float(text)))
        self.update()

    def toggleGuide(self, state):
        is_guide = True if state == Qt.Checked else False
        self.c.toggleGuide.emit(is_guide)
        self.update()

    def selectCurve(self, cname):
        self.c.selectCurve.emit(cname)
        self.update()

    def addCurve(self, cname):
        self.cbox.addItem(cname)
        self.update()

    def selectedCurveName(self, cname):
        self.cbox.setCurrentText(cname)
        self.update()
