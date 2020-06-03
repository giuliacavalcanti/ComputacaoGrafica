#ifndef MAINBOARD_H
#define MAINBOARD_H
#include <QGraphicsScene>
#include <QFile>
#include <QGraphicsPixmapItem>
#include <QGraphicsProxyWidget>
#include "Preview.h"
#include "Board.h"

class MainBoard : public QGraphicsScene {
    Q_OBJECT
public:
    MainBoard(int w, int h);
    ~MainBoard();

private:

    int selectedLevel;
    void drawBG(int w, int h);
    QWidget* getPreLevel(int lvl);
    QVector<QVector<QRect> > maps, smallMaps;
    QVector<QPoint> snakeHead;
    void loadMaps();
    void drawPreviews();
    void releaseBG();
    void releasePreviews();
    void releaseBoard();

    QVector<Preview*> *v;

    QGraphicsRectItem *bgItem;
    QGraphicsPixmapItem *headerItem;
    QVector<QGraphicsScene*> *previewScenes;
    QVector<QWidget*> *previewWidgets;
    QVector<QGraphicsProxyWidget*> *previewProxyWidgets;


    //__
    QGraphicsView *gameView;
    Board *board;
    QGraphicsProxyWidget *gameProxyWidget;
    QWidget *gameWidget;

    QGridLayout *bgLayout, *rightLayout, *leftLayout, *centerLayout;
    QPushButton *buttonBack, *buttonPause;
    QWidget *menuWidget;
    QGraphicsProxyWidget *menuProxyWidget;
    QLCDNumber *scoreNumber, *ateNumber, *bonusAteNumber, *lifeNumber, *bonusTimeLeftNumber;
    QLabel *scoreLabel, *ateLabel, *bonusAteLabel, *lifeLabel, *bonusTimeLeftLabel;



public slots:
    void drawBoard(int i);
    void drawTitleScene(int w, int h);

private slots:
    void closeBoard();
    void pauseOrResumeGame();

protected:
    virtual void keyPressEvent(QKeyEvent *keyEvent);


};


#endif // MAINBOARD_H
