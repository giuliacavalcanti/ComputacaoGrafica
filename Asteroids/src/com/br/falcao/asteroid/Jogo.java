package com.br.falcao.asteroid;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import com.br.falcao.asteroid.*;


public class Jogo extends Frame implements KeyListener, GLEventListener {

    public static final int JOGO_LARGURA = 120; 
    public static final int ANGULO_X_INICIO = 20;
    public static final int ANGULO_X_FINAL = 80;
    public static final int ANGULO_Y_INICIO = -20;

    public static final String JANELA_TITULO = "Asteroids";
    public static final int JANELA_LARGURA = 800;
    public static final int JANELA_ALTURA = 400;
    public static final int JANELA_DESLOCAMENTO = 40;

    public static float CAMERA_X = 0.0f;
    public static float CAMERA_Y = 0.0f;
    public static float CAMERA_Z = 32.0f;

    public static final int TEXTURA_TAMANHO = 50;

    private Base base;
    private Nave nave;
    private Alvo[] alvos;
    private ArrayList<Tiro> tiros;
    private Set<Integer> tirosParaRemover;
    private boolean shoot;

    private Controles controles;

    private Pontuacao pontuacao;

    private long fimDeTempo;

    private GLCanvas canvas;

    public Jogo() {
        super(JANELA_TITULO);
        setSize(JANELA_LARGURA, JANELA_ALTURA);
        setLocation(JANELA_DESLOCAMENTO, JANELA_DESLOCAMENTO);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
        intiOpengl();
    }

    private void intiOpengl() {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        caps.setDoubleBuffered(true);
        caps.setHardwareAccelerated(true);

        canvas = new GLCanvas(caps);
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);

        add(canvas, BorderLayout.CENTER);
        canvas.requestFocus(); 

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0, 0, 0, 0);
        gl.glMatrixMode(GL2.GL_PROJECTION);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);  
        gl.glDepthFunc(GL2.GL_LEQUAL);   
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);  
        gl.glEnable(GL2.GL_LIGHT1);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glEnable(GL2.GL_NORMALIZE);        
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);       
        gl.glLoadIdentity();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        initiJogo(gl);
    }

    /**
     */
    private void initiJogo(GL2 gl) {
        fimDeTempo = System.currentTimeMillis() + 10000 * JOGO_LARGURA;
        pontuacao = new Pontuacao(gl, fimDeTempo, canvas.getWidth(), canvas.getHeight());

        controles = new Controles(this);

        alvos = new Alvo[10];
        criacaoDeAlvos(gl);

        tiros = new ArrayList<>();
        tirosParaRemover = new HashSet<>();
        shoot = false;

        base = new Base(gl);
        nave = new Nave(gl);
    }


    private void criacaoDeAlvos(GL2 gl) {
        Random random = new Random();
        int novosAlvos = 0;

        float limiteX = Base.TAMANHO_BASE - Nave.NAVE_LARGURA;
        float limiteZ = Base.TAMANHO_BASE - Nave.NAVE_Z;

        while (novosAlvos < 10) {
            float x = (random.nextFloat() * limiteX) - limiteX / 2;
            float z = (random.nextFloat() * limiteZ) - limiteZ / 2;

            if (alvoExiste(x, z, 2.0f) == -1) {
                alvos[novosAlvos] = new Alvo(gl, x, z);
                novosAlvos++;
            }

        }

        pontuacao.adicionaNovosAlvos(10);	
    }

    private void geraUmAlvo(GL2 gl, int i) {
        if (System.currentTimeMillis() < fimDeTempo) {
            Random random = new Random();
            boolean foiGerado = false;

            float limiteX = Base.TAMANHO_BASE - Nave.NAVE_LARGURA;
            float limiteZ = Base.TAMANHO_BASE - Nave.NAVE_Z;

            while (!foiGerado) {
                float x = (random.nextFloat() * limiteX) - limiteX / 2; 
                float z = (random.nextFloat() * limiteZ) - limiteZ / 2;

                if (alvoExiste(x, z, 2.0f) == -1) {
                    alvos[i] = new Alvo(gl, x, z);
                    foiGerado = true;
                }
            }

            pontuacao.adicionaNovosAlvos();
        }
    }

    private int alvoExiste(float x, float z, float tolerance) {
        for (int i = 0; i < alvos.length; i++) {
            if (alvos[i] == null) {
                continue;
            }

            if (Math.pow(x - alvos[i].getX(), 2) + Math.pow(z - alvos[i].getZ(), 2) < Math.pow(Alvo.RAIO_EXTERNO + tolerance, 2)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);	

        gl.glPushMatrix();

        if (shoot) {
            shoot(gl);
        }

        gl.glTranslatef(0.0f, 0.0f, controles.getZoom());
        gl.glRotatef(controles.getAnguloY(), 0.0f, 1.0f, 0.0f);
        gl.glRotatef(controles.getAnguloX(), 1.0f, 0.0f, 0.0f);

        light(gl);

        geraAlvos(gl);
        geraTiros(gl);
        base.render();
        gerarNavio(gl);
        pontuacao.render();

        gl.glPopMatrix();

    }

    private void geraAlvos(GL2 gl) {
        for (int i = 0; i < alvos.length; i++) {
            if (alvos[i].estaAtivo()) {
                alvos[i].render();
            } else {
                geraUmAlvo(gl, i);
            }
        }
    }

    private void geraTiros(GL2 gl) {
        int i = 0;
        for (Tiro tiro : tiros) { 
            if (!tiro.estaAtivo()) { 
                tirosParaRemover.add(i);
                continue;
            }

            int index = alvoExiste(tiro.getX(), tiro.getZ(), 1);
            if (index >= 0 && tiro.getY() >= Alvo.ALVO_Y) { 
                alvos[index] = new Alvo(gl, 1000, 1000);
                tiro.setAtivo(false);
                pontuacao.contaAlvosDestruidos();
            }

            tiro.render();
            i++;
        }

        removerTiros();
    }

    private void removerTiros() {
        for (int i : tirosParaRemover) {
            tiros.remove(i);
        }

        tirosParaRemover.clear();
    }

    private void gerarNavio(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(controles.getNaveX(), 0, controles.getNaveZ());
        nave.render();
        gl.glPopMatrix();
    }

    public void light(GL2 gl) {
        float[] pontoIluminacao = {0.0f, -1.0f, 0.0f};
        float[] posicaoIluminacao = {controles.getNaveX(), Alvo.ALVO_Y * 3.5f, controles.getNaveZ(), 1};
        float[] iluminacaoAmbiente = {0.5f, 0.5f, 0.5f, 0.1f};

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoIluminacao, 0); 
        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF, 45.0f); 
        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_EXPONENT, 30.0f);  
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION, pontoIluminacao, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, iluminacaoAmbiente, 0);

        float[] luzDifusa = {0.7f, 0.7f, 0.7f, 0.1f};         
        float[] light1Posicao = {00f, Alvo.ALVO_Y, CAMERA_Z, 1.0f};         
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, luzDifusa, 0); 
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, light1Posicao, 0); 
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int lagura, int altura) {
        pontuacao.setJanelaAltura(canvas.getHeight());
        pontuacao.setJanelaLargura(canvas.getWidth());

        GL2 gl = drawable.getGL().getGL2();
        GLU glu = GLU.createGLU(gl);

        if (altura == 0) {
            altura = 1;
        }
        float aspecto = (float) lagura / (float) altura;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glViewport(0, 0, lagura, altura);

        glu.gluPerspective(45, aspecto, 0.1f, 100.0f);
        glu.gluLookAt(CAMERA_X, CAMERA_Y, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_HOME || key == KeyEvent.VK_ESCAPE) {
            closeWindow();
        }

        if (System.currentTimeMillis() <= fimDeTempo) {
            controles.processKey(e);
        }
    }

    private void shoot(GL2 gl) {
        shoot = false;
        Tiro novoTiro = new Tiro(gl, controles.getNaveX(), controles.getNaveZ());
        tiros.add(novoTiro);
        pontuacao.contaTirosPerdidos();
    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    private void closeWindow() {
        System.exit(0);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void dispose(GLAutoDrawable arg0) {}

    public static void main(String[] args) {
        Jogo g = new Jogo();
        g.setVisible(true);
    }

}
