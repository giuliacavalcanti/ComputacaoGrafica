package com.br.falcao.asteroid;

import java.awt.event.KeyEvent;

import com.br.falcao.asteroid.*;


public class Controles {
	private Jogo jogo;
	
	private int anguloY;
	private int anguloX;
	private int zoom;
	
	private float naveX;
	private float naveZ;	
	
	public Controles(Jogo jogo) {
		this.jogo = jogo;
		
		naveX = naveZ = 0.0f;		
		anguloY = 0;
		anguloX = Jogo.ANGULO_X_INICIO;
		anguloY = Jogo.ANGULO_Y_INICIO;
	}
	
	protected void processKey(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_SPACE) {
			jogo.setShoot(true);
		}	
		
		if(key == KeyEvent.VK_UP && !e.isShiftDown()) {
			deslocaParaCima();
		}

		if(key == KeyEvent.VK_DOWN && !e.isShiftDown()) {
			deslocaBaixo();
		}
		
		if(key == KeyEvent.VK_LEFT && !e.isShiftDown()) {
			deslocaParaEsquerda();
		}
		
		if(key == KeyEvent.VK_RIGHT && !e.isShiftDown()) {
			deslocaParaDireita();
		}
		
		if(key == KeyEvent.VK_UP && e.isShiftDown()) {
			deslocaAnguloParaCima();
		}

		if(key == KeyEvent.VK_DOWN && e.isShiftDown()) {
			deslocaAnguloParaBaixo();
		}
		
		if(key == KeyEvent.VK_LEFT && e.isShiftDown()) {
			deslocaAnguloParaEsquerda();
		}
		
		if(key == KeyEvent.VK_RIGHT && e.isShiftDown()) {
			deslocaAnguloParaDireita();
		}						
	}
	
	private void deslocaParaCima() {
		if(naveZ > -(Base.TAMANHO_BASE / 2 - Nave.NAVE_Z/1.5f)) {
			naveZ--;
		}
	}
	
	private void deslocaBaixo() {
		if(naveZ < Base.TAMANHO_BASE / 2 - Nave.NAVE_Z/1.5f) {
			naveZ++;
		}	
	}
	
	private void deslocaParaEsquerda() {
		if(naveX >= -(Base.TAMANHO_BASE/2.0f - Nave.NAVE_LARGURA/1.5f)) {		
			naveX--;
		}
	}
	
	private void deslocaParaDireita() {
		if(naveX < Base.TAMANHO_BASE/2.0f - Nave.NAVE_LARGURA/1.5f) {		
			naveX++;
		}
	}

	private void deslocaAnguloParaEsquerda() {
		anguloY = (anguloY - 1) % 360;
	}
	
	private void deslocaAnguloParaDireita() {
		anguloY = (anguloY + 1) % 360;
	}
	
	private void deslocaAnguloParaCima() {
		if(anguloX < Jogo.ANGULO_X_FINAL) {
			anguloX = (anguloX + 1) % 360;
		}
	}
	
	private void deslocaAnguloParaBaixo() {
		if(anguloX > Jogo.ANGULO_X_INICIO) {
			anguloX = (anguloX - 1) % 360;		
		}
	}

	public int getAnguloY() {
		return anguloY;
	}

	public int getAnguloX() {
		return anguloX;
	}

	public int getZoom() {
		return zoom;
	}

	public float getNaveX() {
		return naveX;
	}

	public float getNaveZ() {
		return naveZ;
	}		
	
	

}
