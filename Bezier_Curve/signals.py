from PyQt5.QtCore import pyqtSignal, QObject


class Signals(QObject):
    updateStatusBar = pyqtSignal(str)
    updateSelectedPoint = pyqtSignal(str, str)
    gotoPoint = pyqtSignal(int)
    reorderPoint = pyqtSignal(int)
    moveXPoint = pyqtSignal(int)
    moveXPointTo = pyqtSignal(int)
    moveYPoint = pyqtSignal(int)
    moveYPointTo = pyqtSignal(int)
    toggleGuide = pyqtSignal(bool)
    addCurve = pyqtSignal(str)
    selectCurve = pyqtSignal(str)
    selectedCurveName = pyqtSignal(str)
