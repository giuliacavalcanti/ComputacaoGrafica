#include <QApplication>
#include "MainBoard.h"
#include <QGraphicsView>
#include <QDesktopWidget>
int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    MainBoard mainBoard(QApplication::desktop()->width()/2,QApplication::desktop()->height()/2);
    QGraphicsView view(&mainBoard);
    view.setGeometry(view.x(),view.y(),QApplication::desktop()->width()/2,QApplication::desktop()->height()/2);
    view.move(QApplication::desktop()->width()/2-view.width()/2,QApplication::desktop()->height()/2-view.height()/2);
    view.setHorizontalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
    view.setVerticalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
    view.show();
    return a.exec();
}
