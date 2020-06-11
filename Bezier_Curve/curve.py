from PyQt5.QtCore import QPointF
from PyQt5.QtGui import QPolygonF

class Curve:
    def __init__(self, ctype, name=''):
        self.name = name
        self.points = []
        self.points_no = 0
        self.ctype = ctype
        self.is_changed = True
        self.is_guide = False
        self.plot = None
        self.guide = None

    def add_point(self, x, y, z=0):
        self.points.append((x, y))
        self.points_no += 1
        self.is_changed = True

    def move_point_to(self, i, x=None, y=None):
        (old_x, old_y) = self.points[i]
        self.points[i] = (x if x is not None else old_x,
                          y if y is not None else old_y)
        self.is_changed = True

    def move_point_by(self, i, dx, dy):
        (x, y) = self.points[i]
        self.points[i] = (x + dx, y + dy)
        self.is_changed = True

    def make_plot(self, scx, scy):
        if self.is_changed:
            if self.is_guide:
                self.make_guide(scx, scy)
            plot_size = 100. + 10. * self.points_no
            if self.ctype == 'bezier' and self.points_no > 0:
                points = [deCasteljau(self.points, t / plot_size)
                          for t in range(int(plot_size + 1))]
            elif self.ctype == 'rbezier' and self.points_no > 0:
                points = [deCasteljauRat(self.points, self.weights,
                                         t / plot_size)
                          for t in range(int(plot_size + 1))]
            else:
                points = []
            self.plot = QPolygonF()
            for (x, y) in points:
                self.plot.append(QPointF(scx * x + 5, scy * y + 5))
        self.is_changed = False

    def make_guide(self, scx, scy):
        self.guide = QPolygonF()
        for (x, y) in self.points:
            self.guide.append(QPointF(scx * x + 5, scy * y + 5))

    def toggle_guide(self, is_guide):
        self.is_guide = is_guide
        self.is_changed = True

def deCasteljau(points, t):
    return deCasteljauRat(points, [1.0] * len(points), t)


def deCasteljauRat(points, weights, t):
    cpPoints = [points[i] for i in range(len(points))]
    t1 = 1.0 - t
    for k in range(1, len(cpPoints) + 1):
        for i in range(len(cpPoints) - k):
            u = t1 * weights[i]
            v = t * weights[i + 1]
            weights[i] = u + v
            u = u / weights[i]
            v = 1 - u
            cpPoints[i] = combine_pairs([u, v], [cpPoints[i], cpPoints[i + 1]])
    return cpPoints[0]

def combine_pairs(weights, points):
    result = (0.0, 0.0)
    for (i, (x, y)) in enumerate(points):
        result = (result[0] + weights[i] * x, result[1] + weights[i] * y)
    return result
