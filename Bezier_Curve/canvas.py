from PyQt5.QtCore import QObject, Qt
from PyQt5.QtWidgets import QFrame, QCheckBox
from PyQt5.QtGui import (QColor, QPainter, QPen, QBrush)

from curve import Curve


class Canvas(QFrame, QObject):
    def __init__(self, parent):
        super().__init__(parent)
        self.setMouseTracking(True)
        self.curves = {}
        self.activeCurve = None
        self.pointDragged = None
        self.pointSelected = None
        self.selectedX = None
        self.selectedY = None
        self.is_guide = False


    def loadCurves(self, curves):
        self.curves = curves
        print(self.curves.keys())
        self.curves = curves
        self.activeCurve = list(self.curves.keys())[0]
        self.pointDragged = None
        self.pointSelected = None
        self.selectedX = None
        self.selectedY = None
        self.update()

    def selectCurve(self, cname):
        self.activeCurve = cname
        self.pointSelected = None
        self.update()

    def mousePressEvent(self, event):
        if self.activeCurve is not None:
            ex = event.x()
            ey = event.y()
            for (i, (x, y)) in enumerate(self.curves[self.activeCurve].points):
                if L2Dist(x * self.width() + 5, y * self.height() + 5,
                          ex, ey) < 8:
                    self.pointDragged = i
                    self.selectedX = ex
                    self.selectedY = ey
            if self.pointDragged is None:
                screenX = event.x() / self.width()
                screenY = event.y() / self.height()
                self.curves[self.activeCurve].add_point(screenX, screenY)
                self.selectedX = screenX
                self.selectedY = screenY
                self.pointSelected = self.curves[self.activeCurve].points_no - 1
        self.update()

    def mouseMoveEvent(self, event):
        x = event.x() / self.width()
        y = event.y() / self.height()
        if self.pointDragged is not None:
            distance = L2Dist(self.selectedX, self.selectedY, x, y)
            if distance > 5 / self.height():
                self.pointSelected = None
                i = self.pointDragged
                sw5 = 5 / self.width()
                sh5 = 5 / self.height()
                self.curves[self.activeCurve].move_point_to(i,
                                                            x - sw5,
                                                            y - sh5)
                self.update()

    def mouseReleaseEvent(self, event):
        if self.pointSelected:
            distance = L2Dist(self.selectedX, self.selectedY,
                              event.x() / self.width(),
                              event.y() / self.height())
            if distance < 5:
                self.pointDragged = None
        if self.pointDragged is not None:
            self.pointSelected = self.pointDragged
        self.pointDragged = None
        if self.activeCurve is not None:
            for (i, (x, y)) in enumerate(self.curves[self.activeCurve].points):
                if L2Dist(x * self.width() + 5, y * self.height() + 5,
                          event.x(), event.y()) < 8:
                    self.pointSelected = i
                    self.selectedX = event.x() / self.width()
                    self.selectedY = event.y() / self.height()
        self.update()

    def toggleGuide(self, state):
        is_guide = True if state == Qt.Checked else False
        self.curves[self.activeCurve].toggle_guide(is_guide)
        self.update()

    def addCurve(self, ctype, cname=''):
        if cname == '':
            cname = "Curve {}".format(len(self.curves) + 1)
        self.curves[cname] = Curve(ctype=ctype)
        self.selectCurve(cname)
        self.update()
    
    def addBCurve(self):
         self.addCurve('bezier')

    def paintEvent(self, event):
        painter = QPainter(self)

        print(self.curves)
        for curve_name in self.curves:
            self.curves[curve_name].make_plot(self.width(), self.height())
            if self.activeCurve != curve_name:
                painter.setPen(QPen(QColor(120, 120, 120)))
                painter.drawPolyline(self.curves[curve_name].plot)

        if self.activeCurve is not None:
            print(self.curves[self.activeCurve].points)
            if self.curves[self.activeCurve].is_guide:
                painter.setPen(QPen(QColor(255, 0, 0)))
                painter.drawPolyline(self.curves[self.activeCurve].guide)
            #  potem aktywna
            painter.setPen(QPen(QColor(0, 0, 0)))
            print(self.curves[self.activeCurve].plot)
            painter.drawPolyline(self.curves[self.activeCurve].plot)

            #  potem zaznaczone punkty
            if self.pointSelected is not None:
                painter.setPen(QPen(QColor(255, 0, 0)))
                x, y = self.curves[self.activeCurve].points[self.pointSelected]
                painter.drawRect(self.width() * x, self.height() * y, 10, 10)

            #  i same punkty
            painter.setPen(QPen(QColor(0, 0, 0)))
            painter.setBrush(QBrush(QColor(0, 154, 0)))
            for (i, (x, y)) in enumerate(self.curves[self.activeCurve].points):
                painter.drawEllipse(self.width() * x, self.height() * y, 10, 10)
                painter.drawText(self.width() * x + 10,
                                 self.height() * y + 20, str(i))

def L2Dist(x1, y1, x2, y2):
    return ((x1 - x2) ** 2 + (y1 - y2) ** 2) ** 0.5