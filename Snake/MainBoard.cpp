#include "MainBoard.h"

#include <QMessageBox>
MainBoard::MainBoard(int w, int h) {
    this->selectedLevel = 0;
    this->drawTitleScene(w,h);

}

MainBoard::~MainBoard() {

}
void MainBoard::drawBoard(int i) {
    this->releaseBG();
    this->releasePreviews();

    this->selectedLevel = i;

    int nh = this->height()/5*4;
    int w = this->width();

    this->bgLayout = new QGridLayout();
    this->leftLayout = new QGridLayout();
    this->rightLayout = new QGridLayout();
    this->centerLayout = new QGridLayout();
    this->buttonBack = new QPushButton();
    this->buttonPause = new QPushButton();
    this->leftLayout->addWidget(this->buttonPause, 0, 0);
    this->leftLayout->addWidget(this->buttonBack, 0, 1);
    this->buttonBack->setText(QObject::tr("Play"));
    this->buttonPause->setText(QObject::tr("Pausa"));
    this->buttonBack->setFont(QFont("Timew",26,QFont::Bold));
    this->buttonPause->setFont(QFont("Timew",26,QFont::Bold));
    this->bgLayout->addLayout(this->leftLayout, 0, 0, Qt::AlignLeft);
    this->bgLayout->addLayout(this->centerLayout, 0, 1, Qt::AlignCenter);
    this->bgLayout->addLayout(this->rightLayout, 0, 2, Qt::AlignRight);
    this->bonusAteNumber = new QLCDNumber();
    this->ateNumber = new QLCDNumber();
    this->bonusTimeLeftNumber = new QLCDNumber();
    this->lifeNumber = new QLCDNumber();
    this->scoreNumber = new QLCDNumber();
    this->bonusAteNumber->display(0);
    this->ateNumber->display(0);
    this->lifeNumber->display(1);
    this->scoreNumber->display(0);
    this->bonusTimeLeftNumber->display(0);
    this->bonusAteLabel = new QLabel();
    this->ateLabel = new QLabel();
    this->bonusTimeLeftLabel = new QLabel();
    this->scoreLabel = new QLabel();
    this->lifeLabel = new QLabel();
    this->bonusAteLabel->setPixmap(QPixmap(":/images/images/Bonus.png"));
    this->ateLabel->setPixmap(QPixmap(":/images/images/Food.jpg"));
    this->bonusTimeLeftLabel->setText(QObject::tr("O bônus desaparecerá em"));
    this->scoreLabel->setText(QObject::tr("<b><big>Pontos:</b>"));
    this->lifeLabel->setPixmap(QPixmap(":/images/images/Heart.png"));
    this->rightLayout->addWidget(this->scoreLabel, 0, 0);
    this->rightLayout->addWidget(this->scoreNumber, 0, 1);
    this->rightLayout->addWidget(this->lifeLabel, 0, 2);
    this->rightLayout->addWidget(this->lifeNumber, 0, 3);
    this->rightLayout->addWidget(this->ateLabel, 1, 0);
    this->rightLayout->addWidget(this->ateNumber, 1, 1);
    this->rightLayout->addWidget(this->bonusAteLabel, 1, 2);
    this->rightLayout->addWidget(this->bonusAteNumber, 1, 3);
    this->centerLayout->addWidget(this->bonusTimeLeftLabel, 0, 0);
    this->centerLayout->addWidget(this->bonusTimeLeftNumber, 1, 0);

    this->bonusTimeLeftLabel->setVisible(false);
    this->bonusTimeLeftNumber->setVisible(false);

    this->menuWidget = new QWidget();
    this->menuWidget->setLayout(this->bgLayout);
    this->menuProxyWidget = this->addWidget(this->menuWidget);
    this->menuProxyWidget->setGeometry(QRectF(0, 0, w, nh/4));
    this->menuProxyWidget->moveBy(0, 0);



    this->gameWidget = new QWidget();
    this->board = new Board(w,nh);
    this->gameView = new QGraphicsView(this->board, this->gameWidget);
    this->gameView->setGeometry(this->gameView->x(), this->gameView->y(), this->board->width(), this->board->height());
    this->gameView->setHorizontalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
    this->gameView->setVerticalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
    this->gameProxyWidget = this->addWidget(this->gameWidget);
    this->setActivePanel(this->gameProxyWidget);
    this->gameView->setFocus();
    this->gameProxyWidget->moveBy(0, nh/4);
    this->board->setUI(this->scoreNumber, this->ateNumber, this->bonusAteNumber, this->lifeNumber,
                           this->bonusTimeLeftNumber, this->bonusTimeLeftLabel);


    QObject::connect(this->buttonBack, SIGNAL(clicked()), this, SLOT(closeboard()));
    QObject::connect(this->buttonPause, SIGNAL(clicked()), this, SLOT(pauseOrResumeGame()));
    board->drawLevel(maps.at(this->selectedLevel));
    board->drawSnake(this->snakeHead.at(this->selectedLevel));
    board->startGame();

}
void MainBoard::releaseBoard() {
    this->removeItem(this->menuProxyWidget);
    this->removeItem(this->gameProxyWidget);
}

void MainBoard::releaseBG() {
    this->removeItem(this->bgItem);
    this->removeItem(this->headerItem);

}
void MainBoard::releasePreviews() {
    for (int i = 0;i<this->previewScenes->length();i++) {
        this->removeItem(this->previewProxyWidgets->at(i));
    }
}
void MainBoard::drawTitleScene(int w, int h) {
    this->drawBG(w, h);
    if (maps.length() < 1) this->loadMaps();
    this->drawPreviews();
}
void MainBoard::closeBoard() {
    this->releaseBoard();
    this->drawTitleScene(this->width(), this->height());
}
void MainBoard::pauseOrResumeGame() {
    if (this->board->getGameStatus() == this->board->STARTED) {
        this->board->pauseGame();
        this->buttonPause->setText(QObject::tr("Continuar"));
    } else if (this->board->getGameStatus() == this->board->PAUSED) {
        this->board->setFocus();
        this->board->resumeGame();
        this->buttonPause->setText(QObject::tr("Pausa"));

    }
}

void MainBoard::drawBG(int w, int h) {
    this->setSceneRect(0, 0, w, h);
    QBrush brush(QColor(255, 255, 255), QPixmap(":/images/images/fon.jpg"));
    QPen pen(Qt::NoPen);
    this->bgItem = this->addRect(0, 0, this->width(), this->height(), pen, brush);

    QPixmap pix(":/images/images/Caption.png");
    pix=pix.scaled(this->width()/3,this->height()/8,Qt::IgnoreAspectRatio,Qt::SmoothTransformation);
    this->headerItem = this->addPixmap(pix);
    this->headerItem->setPos(this->width()/2-pix.width()/2,40);


}

void MainBoard::loadMaps() {

    int nh = this->height()/5*4;
    int w = this->width();
    QPixmap pix(":/images/images/rightHead.png");
    int h = pix.height()/2;
    QVector<QRect> v1;
    v1.append(QRect(0,0,w,h));
    v1.append(QRect(0,0,h, nh));
    v1.append(QRect(0,nh-h/2,w,h/2));
    v1.append(QRect(w-h,0,h,nh));

    v1.append(QRect(h*8, h*6, h*2.5, h*2.5));
    v1.append(QRect(h*8, nh - h*8, h*2.5, h*2.5));
    v1.append(QRect(w - h*14, h*6, h*2.5 ,h*2.5 ));
    v1.append(QRect(w - h*14, nh - h*8, h*2.5 ,h*2.5));

    QVector<QRect> v3;
    v3.append(QRect(0,0,w,h));
    v3.append(QRect(0,0,h, nh));
    v3.append(QRect(0,nh-h,w,h));
    v3.append(QRect(w-h,0,h,nh));

    v3.append(QRect(h*6, h*6, w-h*12, h*4));
    v3.append(QRect(h*6, nh - h*6-h*4,w-h*12, h*4));

    maps.append(v1);
    maps.append(v3);
    maps.append(v3);
    maps.append(v3);
    maps.append(v3);
    maps.append(v3);


    QPoint v2(this->width()/2, this->height()/4);
    QPoint v4(this->width()/2, this->height()/3);
    this->snakeHead.append(v2);
    this->snakeHead.append(v4);
    this->snakeHead.append(v4);
    this->snakeHead.append(v4);
    this->snakeHead.append(v4);
    this->snakeHead.append(v4);


    h /=4;
    w = (this->width()-this->width()/4)/(maps.length()<=3?maps.length():3);
    nh = (this->height()/4);

    QVector<QRect> v5;
    v5.append(QRect(0,0,w,h));
    v5.append(QRect(0,0,h, nh));
    v5.append(QRect(0,nh-h,w,h));
    v5.append(QRect(w-h,0,h,nh));

    v5.append(QRect(h*8, h*6, h*2.5, h*2.5));
    v5.append(QRect(h*8, nh - h*8, h*2.5, h*2.5));
    v5.append(QRect(w - h*14, h*6, h*2.5 ,h*2.5 ));
    v5.append(QRect(w - h*14, nh - h*8, h*2.5 ,h*2.5));

    QVector<QRect> v6;
    v6.append(QRect(0,0,w,h));
    v6.append(QRect(0,0,h, nh));
    v6.append(QRect(0,nh-h,w,h));
    v6.append(QRect(w-h,0,h,nh));

    v6.append(QRect(h*6, h*6, w-h*12, h*4));
    v6.append(QRect(h*6, nh - h*6-h*4,w-h*12, h*4));

    smallMaps.append(v5);
    smallMaps.append(v6);
    smallMaps.append(v6);
    smallMaps.append(v6);
    smallMaps.append(v6);
    smallMaps.append(v6);

}
void MainBoard::drawPreviews() {
    QPalette p;

    QBrush brush1(QColor(255, 255, 255), QPixmap(":/images/images/fon.jpg"));
    QBrush brush(QColor(255, 255, 255), QPixmap(":/images/images/Wall.jpg"));
    QPen pen(Qt::NoPen);
    int w = (this->width()-this->width()/4)/(maps.length()<=3?maps.length():3);
    int h = (this->height()/4);
    qDebug()<<w<<h;
    this->v = new QVector<Preview*>();
    this->previewProxyWidgets = new QVector<QGraphicsProxyWidget*>();
    this->previewScenes = new QVector<QGraphicsScene*>();
    this->previewWidgets = new QVector<QWidget*>();

    for (int i = 0;i<maps.length();i++) {

        this->previewScenes->append(new QGraphicsScene());

        this->previewScenes->last()->setSceneRect(QRectF(0,0,w,h));
        this->previewScenes->last()->addRect(0, 0, this->previewScenes->last()->width(), this->previewScenes->last()->height(), pen, brush1);

        this->v->append(new Preview(this->previewScenes->last(), i));

        this->previewWidgets->append(new QWidget());

        this->previewWidgets->last()->setGeometry(this->previewWidgets->last()->x(),this->previewWidgets->last()->y(),this->previewScenes->last()->width(),this->previewScenes->last()->height());

        v->at(i)->setParent(this->previewWidgets->last());
        v->at(i)->setGeometry(1,1, this->previewWidgets->last()->width()-2,this->previewWidgets->last()->height()-2);
        p.setBrush(this->previewWidgets->last()->backgroundRole(),QBrush(QPixmap(":/images/images/fon.jpg")));
        v->at(i)->setHorizontalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
        v->at(i)->setVerticalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
        this->previewWidgets->last()->setStyleSheet("QWidget{border: 2px solid #831212;border-radius: 5px;}QWidget::hover{border: 2px solid black;}");
        this->previewWidgets->last()->setPalette(p);

        for (int j = 0;j<maps.at(i).length();j++) {
            this->previewScenes->last()->addRect(smallMaps.at(i).at(j),pen, brush);
        }
        this->previewProxyWidgets->append(this->addWidget(this->previewWidgets->last()));

        if (i<3) this->previewProxyWidgets->last()->setPos(this->width()/8+w*i+20*i, h+10);
        else this->previewProxyWidgets->last()->setPos(this->width()/8+w*(i-3)+20*(i-3),h*2 +30);
        QObject::connect(v->at(i),SIGNAL(clicked(int)),this,SLOT(drawBoard(int)));

    }

}

void MainBoard::keyPressEvent(QKeyEvent *keyEvent) {
    if (this->board->getGameStatus() == this->board->STARTED) {
        this->board->keyPress(keyEvent);
    }

}
